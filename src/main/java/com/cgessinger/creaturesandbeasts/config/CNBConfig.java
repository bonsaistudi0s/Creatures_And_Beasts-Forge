package com.cgessinger.creaturesandbeasts.config;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

@EventBusSubscriber(modid = CreaturesAndBeasts.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CNBConfig {
    public static final ServerConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static void bakeConfig() {
        ServerConfig.GREBE_CONFIG.bake();
        ServerConfig.LIZARD_CONFIG.bake();
        ServerConfig.CINDERSHELL_CONFIG.bake();
        ServerConfig.FRIENDLY_SPORELING_CONFIG.bake();
        ServerConfig.HOSTILE_SPORELING_CONFIG.bake();
        ServerConfig.NEUTRAL_SPORELING_CONFIG.bake();
        ServerConfig.LILYTAD_CONFIG.bake();
        ServerConfig.YETI_CONFIG.bake();
        ServerConfig.YETI_PROP.bake();

        ServerConfig.HIDE_AMOUNT.bake();
        ServerConfig.HIDE_MULTIPLIER.bake();
        ServerConfig.HIDE_COST.bake();
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfigEvent configEvent) {
        bakeConfig();
    }

    public static class ServerConfig {
        public static EntityConfig GREBE_CONFIG;
        public static EntityConfig LIZARD_CONFIG;
        public static EntityConfig CINDERSHELL_CONFIG;
        public static EntityConfig FRIENDLY_SPORELING_CONFIG;
        public static EntityConfig HOSTILE_SPORELING_CONFIG;
        public static EntityConfig NEUTRAL_SPORELING_CONFIG;
        public static EntityConfig LILYTAD_CONFIG;
        public static EntityConfig YETI_CONFIG;
        public static OtherConfig<Double> YETI_PROP;

        public static OtherConfig<Integer> HIDE_AMOUNT;
        public static OtherConfig<Double> HIDE_MULTIPLIER;
        public static OtherConfig<Integer> HIDE_COST;

        public ServerConfig(ForgeConfigSpec.Builder builder) {
            builder.push(CreaturesAndBeasts.MOD_ID).comment("Creatures And Beasts common config");

            builder.push(CreaturesAndBeasts.MOD_ID + "_entity").comment("Creatures And Beasts Entity Config");

            GREBE_CONFIG = EntityConfig.createConfigForEntity(builder, "grebe", true, 30, Lists.newArrayList("minecraft:frozen_river", "minecraft:river"));

            LIZARD_CONFIG = EntityConfig.createConfigForEntity(builder, "lizard", true, 50, Lists.newArrayList("minecraft:badlands", "minecraft:wooded_badlands_plateau", "minecraft:badlands_plateau", "minecraft:desert", "minecraft:desert_hills", "minecraft:desert_lakes"));

            CINDERSHELL_CONFIG = EntityConfig.createConfigForEntity(builder, "cindershell", true, 10, Lists.newArrayList("minecraft:soul_sand_valley", "minecraft:crimson_forest", "minecraft:warped_forest", "minecraft:basalt_deltas", "minecraft:nether_wastes"));

            FRIENDLY_SPORELING_CONFIG = EntityConfig.createConfigForEntity(builder, "friendly_sporeling", true, 40, Lists.newArrayList("minecraft:mushroom_fields", "minecraft:mushroom_field_shore", "minecraft:dark_forest", "minecraft:swamp", "minecraft:swamp_hills"));

            HOSTILE_SPORELING_CONFIG = EntityConfig.createConfigForEntity(builder, "hostile_sporeling", true, 10, Lists.newArrayList("minecraft:soul_sand_valley", "minecraft:basalt_deltas", "minecraft:nether_wastes"));

            NEUTRAL_SPORELING_CONFIG = EntityConfig.createConfigForEntity(builder, "neutral_sporeling", true, 5, Lists.newArrayList("minecraft:crimson_forest", "minecraft:warped_forest"));

            LILYTAD_CONFIG = EntityConfig.createConfigForEntity(builder, "lilytad", true, 35, Lists.newArrayList("minecraft:swamp", "minecraft:swamp_hills"));

            YETI_CONFIG = EntityConfig.createConfigForEntity(builder, "yeti", true, 1, Lists.newArrayList("minecraft:snowy_tundra", "minecraft:snowy_mountains", "minecraft:snowy_taiga", "minecraft:snowy_taiga_hills", "minecraft:ice_spikes", "minecraft:snowy_taiga_mountains"));

            YETI_PROP = OtherConfig.withRange(builder, "Define extra chance to spawn yeti. Each time a yeti should spawn it checks random.nextFloat() >= value. Increase this value up to 1.0 to make yetis more rare", "yeti chance", 0.5D, 0D, 1D, Double.class);

            builder.pop();

            builder.push(CreaturesAndBeasts.MOD_ID + "_other").comment("Creatures And Beasts Other Config");

            HIDE_AMOUNT = OtherConfig.with(builder, "Define how often items can be reinforced with the yeti hide", "hide amount", 5);
            HIDE_MULTIPLIER = OtherConfig.with(builder, "Define the yeti hide reinforcement multiplier. The armor attribute value will be multiplied with this value each time", "hide multiplier", 1.01D);
            HIDE_COST = OtherConfig.with(builder, "Define amount of xp needed to upgrade armor with yeti hide", "hide cost", 1);

            builder.pop();

            builder.pop();
        }
    }
}
