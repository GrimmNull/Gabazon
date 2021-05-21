package com.grimmyboi.javaengineering.setup;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Config {
    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_POWER = "power";
    public static final String SUBCATEGORY_THERMALGENERATOR = "thermal_generator";

    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.IntValue THERMALGENERATOR_MAXPOWER;
    public static ForgeConfigSpec.IntValue THERMALGENERATOR_GENERATE;
    public static ForgeConfigSpec.IntValue THERMALGENERATOR_SEND;
    public static ForgeConfigSpec.IntValue THERMALGENERATOR_TICKS;

    static {

        ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder();
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        CLIENT_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
        CLIENT_BUILDER.pop();

        SERVER_BUILDER.comment("Power settings").push(CATEGORY_POWER);

        setupFirstBlockConfig(SERVER_BUILDER, CLIENT_BUILDER);

        SERVER_BUILDER.pop();


        SERVER_CONFIG = SERVER_BUILDER.build();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    private static void setupFirstBlockConfig(ForgeConfigSpec.Builder SERVER_BUILDER, ForgeConfigSpec.Builder CLIENT_BUILDER) {
        SERVER_BUILDER.comment("Thermal generator settings").push(SUBCATEGORY_THERMALGENERATOR);

        THERMALGENERATOR_MAXPOWER = SERVER_BUILDER.comment("Maximum power for the Thermal generator")
                .defineInRange("maxPower", 100000, 0, Integer.MAX_VALUE);
        THERMALGENERATOR_GENERATE = SERVER_BUILDER.comment("Power generation per lava bucket")
                .defineInRange("generate", 10000, 0, Integer.MAX_VALUE);
        THERMALGENERATOR_SEND = SERVER_BUILDER.comment("Power generation to send per tick")
                .defineInRange("send", 100, 0, Integer.MAX_VALUE);
        THERMALGENERATOR_TICKS = SERVER_BUILDER.comment("Ticks per lava bucket")
                .defineInRange("ticks", 20, 0, Integer.MAX_VALUE);

        SERVER_BUILDER.pop();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }
}
