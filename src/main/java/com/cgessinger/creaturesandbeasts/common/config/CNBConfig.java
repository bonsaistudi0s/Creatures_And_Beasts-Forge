package com.cgessinger.creaturesandbeasts.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(modid = CreaturesAndBeasts.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CNBConfig 
{
    public static final ServerConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static 
    {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    // Doesn't need to be an inner class
    public static class ServerConfig 
    {
        public static EntityConfig GREBE_CONFIG;
        public static EntityConfig LIZARD_CONFIG;
        public static EntityConfig CINDERSHELL_CONFIG;
        public static EntityConfig FRIENDLY_SPORELING_CONFIG;
        public static EntityConfig HOSTILE_SPORELING_CONFIG;
        public static EntityConfig NEUTRAL_SPORELING_CONFIG;
        public static EntityConfig LILYTAD_CONFIG;
        public static EntityConfig YETI_CONFIG;

        public ServerConfig(ForgeConfigSpec.Builder builder) 
        {
            builder.push(CreaturesAndBeasts.MOD_ID).comment("Creatures And Beasts common config");
            
            GREBE_CONFIG = createConfigForEntity(builder, "grebe", true, 300, 
                Lists.newArrayList("minecraft:frozen_river", "minecraft:river"));
            LIZARD_CONFIG = createConfigForEntity(builder, "lizard", true, 50, 
                Lists.newArrayList("minecraft:badlands", "minecraft:wooded_badlands_plateau", "minecraft:badlands_plateau", "minecraft:desert", "minecraft:desert_hills", "minecraft:desert_lakes"));
            CINDERSHELL_CONFIG = createConfigForEntity(builder, "cindershell", true, 10, 
                Lists.newArrayList("minecraft:soul_sand_valley", "minecraft:crimson_forest", "minecraft:warped_forest", "minecraft:basalt_deltas", "minecraft:nether_wastes"));
            FRIENDLY_SPORELING_CONFIG = createConfigForEntity(builder, "friendly_sporeling", true, 40,
                Lists.newArrayList("minecraft:mushroom_fields", "minecraft:mushroom_field_shore", "minecraft:dark_forest", "minecraft:swamp", "minecraft:swamp_hills"));
            HOSTILE_SPORELING_CONFIG = createConfigForEntity(builder, "hostile_sporeling", true, 10,
                Lists.newArrayList("minecraft:soul_sand_valley", "minecraft:basalt_deltas", "minecraft:nether_wastes"));
            NEUTRAL_SPORELING_CONFIG = createConfigForEntity(builder, "neutral_sporeling", true, 5,
                Lists.newArrayList("minecraft:crimson_forest", "minecraft:warped_forest"));
            LILYTAD_CONFIG = createConfigForEntity(builder, "lilytad", true, 35, 
                Lists.newArrayList("minecraft:swamp", "minecraft:swamp_hills"));
            YETI_CONFIG = createConfigForEntity(builder, "yeti", true, 1, 
                Lists.newArrayList("minecraft:snowy_tundra", "minecraft:snowy_mountains", "minecraft:snowy_taiga", "minecraft:snowy_taiga_hills", "minecraft:ice_spikes", "minecraft:snowy_taiga_mountains"));

            builder.pop();
        }
    }

    public static EntityConfig createConfigForEntity(ForgeConfigSpec.Builder builder, String name, Boolean dfActive, int dfWeight, List<String> dfBiomes)
    {
        ForgeConfigSpec.ConfigValue<Boolean> active = builder.comment(
                "This defines whether or not the " + name + "s should exist (default: " + dfActive + ", when changing this to false EVERY entity of this type will be deleted!)")
                .translation("cnb.configgui." + name + "_active").define(name + " Active", dfActive);

        ForgeConfigSpec.ConfigValue<Integer> spawnWeight = builder.comment("This defines the spawn rate of the " + name + "s (default: " + dfWeight + ")")
                .translation("cnb.configgui." + name+ "_weight").define(name + " SpawnWeight", dfWeight);

        ForgeConfigSpec.ConfigValue<List<? extends String>> biomes = builder.comment("This defines in which biome the " + name + "s will spawn")
                .translation("cnb.configgui." + name + "_biomes").defineList(name + " Biomes", dfBiomes, o -> o instanceof String);

        return new EntityConfig(active, spawnWeight, biomes);
    }

    public static void bakeConfig() 
    {
        ServerConfig.GREBE_CONFIG.bake();
        ServerConfig.LIZARD_CONFIG.bake();
        ServerConfig.CINDERSHELL_CONFIG.bake();
        ServerConfig.FRIENDLY_SPORELING_CONFIG.bake();
        ServerConfig.HOSTILE_SPORELING_CONFIG.bake();
        ServerConfig.NEUTRAL_SPORELING_CONFIG.bake();
        ServerConfig.LILYTAD_CONFIG.bake();
        ServerConfig.YETI_CONFIG.bake();
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) 
    {
        bakeConfig();
    }
}
