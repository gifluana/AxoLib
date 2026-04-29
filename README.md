# AxoLib

AxoLib is a lightweight animated model library for NeoForge, built around a single `.axo.json` file that contains both model and animation data.

The public API intentionally follows the same shape modders expect from GeckoLib:

- implement an animatable interface such as `AxoEntity`, `AxoItem`, or `AxoBlockEntity`
- keep an `AxoAnimatableInstanceCache`
- register `AxoAnimationController`s
- provide an `AxoModel`
- render through an `AxoRenderer`

## Asset Layout

Defaulted Axo models use this convention:

```text
assets/<modid>/axolib/models/entity/<name>.axo.json
assets/<modid>/textures/entity/<name>.png
```

For items and blocks, replace `entity` with `item` or `block`.

## Example

```java
public class ExampleEntity extends PathfinderMob implements AxoEntity {
    private final AxoAnimatableInstanceCache axoCache = AxoLibUtil.createInstanceCache(this);

    @Override
    public void registerControllers(AxoAnimationControllerRegistrar controllers) {
        controllers.add(new AxoAnimationController<>(this, "main", 5, "idle"));
    }

    @Override
    public AxoAnimatableInstanceCache getAnimatableInstanceCache() {
        return axoCache;
    }
}
```

```java
public class ExampleEntityModel extends DefaultedAxoEntityModel<ExampleEntity> {
    public ExampleEntityModel() {
        super(Identifier.fromNamespaceAndPath("examplemod", "example_entity"));
    }
}
```

That resolves:

```text
assets/examplemod/axolib/models/entity/example_entity.axo.json
assets/examplemod/textures/entity/example_entity.png
```
