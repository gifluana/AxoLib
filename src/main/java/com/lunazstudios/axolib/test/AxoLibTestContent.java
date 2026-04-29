package com.lunazstudios.axolib.test;

import com.lunazstudios.axolib.AxoLib;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AxoLibTestContent {
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AxoLib.MODID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AxoLib.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, AxoLib.MODID);

    public static final DeferredBlock<AxoTestBlock> AXO_TEST_BLOCK =
        BLOCKS.registerBlock("axo_test_block", AxoTestBlock::new);

    public static final DeferredItem<BlockItem> AXO_TEST_BLOCK_ITEM =
        ITEMS.registerSimpleBlockItem(AXO_TEST_BLOCK);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AxoTestBlockEntity>> AXO_TEST_BLOCK_ENTITY =
        BLOCK_ENTITIES.register("axo_test_block", () -> new BlockEntityType<>(AxoTestBlockEntity::new, AXO_TEST_BLOCK.get()));

    private AxoLibTestContent() {
    }

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
        ITEMS.register(modBus);
        BLOCK_ENTITIES.register(modBus);
    }
}
