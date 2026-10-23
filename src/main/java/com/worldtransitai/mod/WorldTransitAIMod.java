package com.worldtransitai.mod;

import com.mojang.logging.LogUtils;
import com.worldtransitai.mod.command.TransitAICommand;
import com.worldtransitai.mod.item.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(WorldTransitAIMod.MOD_ID)
public class WorldTransitAIMod {
    public static final String MOD_ID = "worldtransitai";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WorldTransitAIMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("World Transit AI Mod initialized! Ready for AI Metro & Global Railway Generation.");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        TransitAICommand.register(event.getDispatcher());
    }
}
