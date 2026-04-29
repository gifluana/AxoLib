package com.lunazstudios.axolib.test;

import com.lunazstudios.axolib.api.AxoLibUtil;
import com.lunazstudios.axolib.api.animatable.AxoAnimatableInstanceCache;
import com.lunazstudios.axolib.api.animatable.AxoAnimationControllerRegistrar;
import com.lunazstudios.axolib.api.animatable.AxoBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class AxoTestBlockEntity extends BlockEntity implements AxoBlockEntity {
    private final AxoAnimatableInstanceCache axoCache = AxoLibUtil.createInstanceCache(this);

    public AxoTestBlockEntity(BlockPos pos, BlockState blockState) {
        super(AxoLibTestContent.AXO_TEST_BLOCK_ENTITY.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, AxoTestBlockEntity blockEntity) {
    }

    public void playClick() {
        triggerAxoAnimation("click");
    }

    @Override
    public void registerControllers(AxoAnimationControllerRegistrar controllers) {
        controllers.add(AxoLibUtil.controller(this, "idle"));
    }

    @Override
    public AxoAnimatableInstanceCache getAnimatableInstanceCache() {
        return axoCache;
    }
}
