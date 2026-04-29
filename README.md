# AxoLib

![Version](https://img.shields.io/badge/version-1.0-blue)
![Minecraft](https://img.shields.io/badge/minecraft-26.1-green)
![NeoForge](https://img.shields.io/badge/neoforge-26.1-orange)
![License](https://img.shields.io/badge/license-MIT-lightgrey)
![Java](https://img.shields.io/badge/java-25-red)

> Lightweight animated model library for NeoForge — one file, no compromises.

---

## ✨ Features

- **Single `.axo.json` format** — model geometry and animations live in one file, no split `.geo` + `.animation` files
- **Full Molang support** — Bedrock-compatible expression system with 30+ math functions
- **30+ easing curves** — linear, catmullRom, bezier, bounce, elastic, back, expo, circ and more
- **Bone visibility keyframes** — show/hide bones at specific animation frames
- **Sound / Particle / Custom event keyframes** — fire callbacks at precise points in the timeline
- **Animation layering** — blend multiple animations simultaneously with independent weights
- **Entity, Item, Block, Armor** animatable types out of the box
- **Convention-based model resolution** — zero boilerplate path configuration

---

## 📦 Installation

Add AxoLib to your `build.gradle`:

```groovy
repositories {
    maven { url = "https://repo.lunazstudios.com/releases" } // or your local repo
}

dependencies {
    implementation "com.lunazstudios.axolib:axolib:1.0"
}
```

---

## 🚀 Quick Start

### Animated Entity

```java
public class MyEntity extends PathfinderMob implements AxoEntity {
    private final AxoAnimatableInstanceCache axoCache = AxoLibUtil.createInstanceCache(this);

    @Override
    public void registerControllers(AxoAnimationControllerRegistrar controllers) {
        controllers.add(new AxoAnimationController<>(this, "main", 5, state -> {
            if (state.animatable().isAttacking()) return "attack";
            return state.animatable().isMoving() ? "walk" : "idle";
        }));
    }

    @Override
    public AxoAnimatableInstanceCache getAnimatableInstanceCache() { return axoCache; }
}
```

```java
// Client: register the renderer
event.registerEntityRenderer(MY_ENTITY.get(), ctx ->
    new AxoEntityRenderer<>(new DefaultedAxoEntityModel<>(
            Identifier.fromNamespaceAndPath("mymod", "my_entity"))));
```

Resolves automatically to:
```
assets/mymod/axolib/models/entity/my_entity.axo.json
assets/mymod/textures/entity/my_entity.png
```

### Animated Block Entity

```java
public class MyBlockEntity extends BlockEntity implements AxoBlockEntity {
    private final AxoAnimatableInstanceCache axoCache = AxoLibUtil.createInstanceCache(this);

    @Override
    public void registerControllers(AxoAnimationControllerRegistrar controllers) {
        controllers.add(new AxoAnimationController<>(this, "main", 3, "idle"));
    }

    @Override
    public AxoAnimatableInstanceCache getAnimatableInstanceCache() { return axoCache; }

    public void onInteract() { triggerAxoAnimation("open"); }
}
```

### Animated Armor

```java
public class MyHelmetItem extends ArmorItem implements AxoArmor {
    private final AxoAnimatableInstanceCache axoCache = AxoLibUtil.createInstanceCache(this);

    @Override
    public void registerControllers(AxoAnimationControllerRegistrar controllers) {
        controllers.add(new AxoAnimationController<>(this, "main", 0, "idle"));
    }

    @Override
    public AxoAnimatableInstanceCache getAnimatableInstanceCache() { return axoCache; }
}
```

```java
// Inside a RenderLayer:
AxoArmorRenderer<MyHelmetItem> renderer = new AxoArmorRenderer<>("mymod", "my_helmet");

renderer.renderArmorPiece(helmetItem, entity, EquipmentSlot.HEAD,
        helmetItem.getAxoRenderTick(entity, partialTick),
        partialTick, poseStack, buffers, light);
```

---

## 📐 .axo.json Format

```json
{
  "version": "0.7.2",
  "model": {
    "texture": [64, 64],
    "groups": {
      "root": {
        "origin": [0, 0, 0],
        "cubes": [...]
      },
      "lid": {
        "origin": [0, 14, 0],
        "parent": "root",
        "cubes": [...]
      }
    }
  },
  "animations": {
    "idle": {
      "duration": 3,
      "loop": true,
      "groups": {
        "lid": {
          "rotate":     [[0, "catmullrom", 0, 0, 0], [1.5, "catmullrom", -15, 0, 0], [3, "catmullrom", 0, 0, 0]],
          "visibility": [[0.0, true], [2.5, false]]
        }
      },
      "events": [
        { "time": 0.5, "type": "sound",    "sound": "minecraft:block.chest.open" },
        { "time": 1.0, "type": "particle", "particle": "minecraft:smoke", "bone": "lid", "offset": [0, 1, 0] },
        { "time": 1.5, "type": "custom",   "event": "my_event", "data": "extra" }
      ]
    }
  }
}
```

### Keyframe syntax

| Channel | Format |
|---|---|
| `translate` / `rotate` / `scale` | `[[timeSec, "easing", x, y, z], ...]` |
| `visibility` | `[[timeSec, boolean], ...]` |
| `events[].type` | `"sound"` · `"particle"` · `"custom"` |

---

## 🎵 Animation Events

Register a handler on any controller:

```java
controller.setEventHandler(event -> switch (event) {
    case AxoAnimationEvent.Sound   s -> level.playSound(null, getX(), getY(), getZ(),
                                            SoundEvents.CHEST_OPEN, SoundSource.BLOCKS, 1f, 1f);
    case AxoAnimationEvent.Particle p -> level.addParticle(ParticleTypes.SMOKE,
                                            getX() + p.offsetX(), getY() + p.offsetY(), getZ() + p.offsetZ(),
                                            0, 0.05, 0);
    case AxoAnimationEvent.Custom  c -> handleCustomEvent(c.event(), c.data());
});
```

---

## 🎭 Animation Layering

Play multiple animations on top of each other with independent weights:

```java
// In your controller setup or anywhere during gameplay:
controller.playAdditional("attack_upper", 1.0f);  // fully overrides upper body
controller.playAdditional("head_look",    0.5f);  // blends 50% with primary

// Later:
controller.stopAdditional("attack_upper");
controller.stopAllAdditional();
```

---

## ⚖️ AxoLib vs GeckoLib

| Feature | AxoLib | GeckoLib |
|---|:---:|:---:|
| Unified model + animation file | ✅ `.axo.json` | ❌ split files |
| Molang expression support | ✅ | ✅ |
| 30+ easing functions | ✅ | ✅ |
| Sound keyframes | ✅ | ✅ |
| Particle keyframes | ✅ | ✅ |
| Custom event keyframes | ✅ | ✅ |
| Bone visibility keyframes | ✅ | ✅ |
| Animation layering | ✅ | ✅ |
| Animated armor | ✅ | ✅ |
| Multi-loader (Fabric / Forge) | ❌ NeoForge only | ✅ |
| Blockbench plugin | ❌ planned | ✅ |

---

## 🗂️ Asset Layout

```
assets/<modid>/axolib/models/entity/<name>.axo.json   ← entity model
assets/<modid>/axolib/models/block/<name>.axo.json    ← block model
assets/<modid>/axolib/models/item/<name>.axo.json     ← item model
assets/<modid>/axolib/models/armor/<name>.axo.json    ← armor model

assets/<modid>/textures/entity/<name>.png
assets/<modid>/textures/block/<name>.png
assets/<modid>/textures/item/<name>.png
assets/<modid>/textures/armor/<name>.png
```

---

## 📄 License

[MIT](TEMPLATE_LICENSE.txt) © LunazStudios
