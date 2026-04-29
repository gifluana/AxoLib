package com.lunazstudios.axolib.client;

import com.lunazstudios.axolib.AxoLib;
import com.lunazstudios.axolib.test.AxoLibTestContent;
import com.lunazstudios.axolib.test.client.AxoTestBlockEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = AxoLib.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = AxoLib.MODID, value = Dist.CLIENT)
public class AxoLibClient {


    public AxoLibClient(ModContainer container) {
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(AxoLibTestContent.AXO_TEST_BLOCK_ENTITY.get(), AxoTestBlockEntityRenderer::new);
    }

}
