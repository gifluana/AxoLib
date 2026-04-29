package com.lunazstudios.axolib;

import com.lunazstudios.axolib.server.ModelRepository;
import com.lunazstudios.axolib.test.AxoLibTestContent;
import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

@Mod(AxoLib.MODID)
public class AxoLib {
    public static final String MODID = "axolib";
    public static final Logger LOGGER = LogUtils.getLogger();

    public AxoLib(IEventBus modEventBus, ModContainer modContainer) {
        AxoLibTestContent.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        Path modelsFolder = server.getServerDirectory().resolve("axolib");
        ModelRepository.getInstance().setFolder(modelsFolder);
        LOGGER.info("[AxoLib] Models folder: {}", modelsFolder);

        try {
            String result = ModelRepository.getInstance().reload();
            LOGGER.info("[AxoLib] Auto-loaded on startup: {}", result);
        } catch (IOException e) {
            LOGGER.error("[AxoLib] Failed to auto-load models from disk: {}", e.getMessage());
        }
    }
}
