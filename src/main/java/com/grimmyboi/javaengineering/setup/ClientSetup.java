package com.grimmyboi.javaengineering.setup;


import com.grimmyboi.javaengineering.Main;
import com.grimmyboi.javaengineering.block.thermalgenerator.ThermalGeneratorScreen;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.register(ModContainerTypes.THERMAL_GENERATOR_CONTAINER.get(), ThermalGeneratorScreen::new);
    }
}

