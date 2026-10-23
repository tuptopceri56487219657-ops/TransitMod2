package com.worldtransitai.mod.item;

import com.worldtransitai.mod.WorldTransitAIMod;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, WorldTransitAIMod.MOD_ID);

    // Планшет ИИ-Архитектора для выбора страны, города, линий метро/ЖД и мониторинга графиков движения
    public static final RegistryObject<Item> AI_TRANSIT_TABLET = ITEMS.register("ai_transit_tablet",
            () -> new Item(new Item.Properties().stacksTo(1)));

    // Стамеска для микроблоков и кастомной мебели (Bits & Chisels + Furniture Builder)
    public static final RegistryObject<Item> PRECISION_CHISEL = ITEMS.register("precision_chisel",
            () -> new Item(new Item.Properties().stacksTo(1)));

    // Контроллер машиниста с телеметрией (Train Main HUD)
    public static final RegistryObject<Item> DRIVER_TELEMETRY_CONTROLLER = ITEMS.register("driver_telemetry_controller",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        eventBus.addListener(ModItems::addCreative);
    }

    private static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(AI_TRANSIT_TABLET);
            event.accept(PRECISION_CHISEL);
            event.accept(DRIVER_TELEMETRY_CONTROLLER);
        }
    }
}
