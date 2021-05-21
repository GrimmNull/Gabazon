package com.grimmyboi.javaengineering;

import com.grimmyboi.javaengineering.setup.ClientSetup;
import com.grimmyboi.javaengineering.setup.Registration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MOD_ID)
public class Main {
    public static final String MOD_ID="javaengineering";

    public Main() {
        Registration.register();
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }


}
