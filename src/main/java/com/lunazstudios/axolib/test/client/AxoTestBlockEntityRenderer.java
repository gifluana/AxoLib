package com.lunazstudios.axolib.test.client;

import com.lunazstudios.axolib.AxoLib;
import com.lunazstudios.axolib.api.renderer.AxoBlockEntityRenderer;
import com.lunazstudios.axolib.test.AxoTestBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class AxoTestBlockEntityRenderer extends AxoBlockEntityRenderer<AxoTestBlockEntity> {
    public AxoTestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context, AxoLib.MODID, "axo_test_block");
    }
}
