package com.cgessinger.creaturesandbeasts.config;

import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import org.infernalstudios.config.Config;
import org.infernalstudios.config.annotation.Configurable;
import org.infernalstudios.config.annotation.DoubleRange;
import org.infernalstudios.config.annotation.IntegerRange;

import java.util.ArrayList;
import java.util.List;

public class CNBConfig {
    public static Config CONFIG;

    @Configurable(description = "Determines how many yeti hides can be used to reinforce an item.", category = "General")
    @IntegerRange(min = 0)
    public static int hideAmount = 5;

    @Configurable(description = "Determines the experience cost of applying yeti hide to an item.", category = "General")
    @IntegerRange(min = 0)
    public static int hideCost = 1;

    @Configurable(description = "Determines the multiplier used to add armor per yeti hide on an item.", category = "General")
    @DoubleRange(min = 0)
    public static double hideMultiplier = 0.01D;

    @Configurable(handler = "com.cgessinger.creaturesandbeasts.config.handler.EntitySpawnDataListConfigHandler.INSTANCE", category = "Spawns")
    public static List<EntitySpawnData> spawns = new ArrayList<>(List.of(
            EntitySpawnData.of(CNBEntityTypes.YETI.getId(), Biomes.SNOWY_PLAINS, MobCategory.CREATURE, 1, 2, 3, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.YETI.getId(), Biomes.ICE_SPIKES, MobCategory.CREATURE, 1, 2, 3, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.YETI.getId(), Biomes.SNOWY_TAIGA, MobCategory.CREATURE, 1, 2, 3, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.YETI.getId(), Biomes.FROZEN_PEAKS, MobCategory.CREATURE, 2, 2, 3, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.YETI.getId(), Biomes.SNOWY_SLOPES, MobCategory.CREATURE, 1, 2, 3, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.LITTLE_GREBE.getId(), Biomes.RIVER, MobCategory.CREATURE, 35, 2, 3, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.CACTEM.getId(), Biomes.BADLANDS, MobCategory.CREATURE, 3, 6, 13, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.CACTEM.getId(), Biomes.WOODED_BADLANDS, MobCategory.CREATURE, 3, 6, 13, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.CACTEM.getId(), Biomes.ERODED_BADLANDS, MobCategory.CREATURE, 3, 6, 13, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.CACTEM.getId(), Biomes.DESERT, MobCategory.CREATURE, 3, 6, 13, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.SPORELING.getId(), Biomes.MUSHROOM_FIELDS, MobCategory.CREATURE, 20, 3, 5, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.SPORELING.getId(), Biomes.SWAMP, MobCategory.CREATURE, 25, 3, 5, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.SPORELING.getId(), Biomes.LUSH_CAVES, MobCategory.CREATURE, 60, 3, 5, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.SPORELING.getId(), Biomes.DARK_FOREST, MobCategory.CREATURE, 70, 3, 5, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.SPORELING.getId(), Biomes.NETHER_WASTES, MobCategory.MONSTER, 60, 2, 4, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.SPORELING.getId(), Biomes.WARPED_FOREST, MobCategory.MONSTER, 2, 2, 4, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.SPORELING.getId(), Biomes.CRIMSON_FOREST, MobCategory.MONSTER, 120, 2, 4, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.LILYTAD.getId(), Biomes.SWAMP, MobCategory.CREATURE, 45, 1, 1, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.MINIPAD.getId(), Biomes.SWAMP, MobCategory.CREATURE, 20, 3, 6, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.END_WHALE.getId(), Biomes.THE_END, MobCategory.CREATURE, 1, 1, 1, 400.0D, 1.0D),
            EntitySpawnData.of(CNBEntityTypes.END_WHALE.getId(), Biomes.END_BARRENS, MobCategory.CREATURE, 1, 1, 1, 400.0D, 1.0D),
            EntitySpawnData.of(CNBEntityTypes.END_WHALE.getId(), Biomes.END_HIGHLANDS, MobCategory.CREATURE, 1, 1, 1, 400.0D, 1.0D),
            EntitySpawnData.of(CNBEntityTypes.END_WHALE.getId(), Biomes.END_MIDLANDS, MobCategory.CREATURE, 1, 1, 1, 400.0D, 1.0D),
            EntitySpawnData.of(CNBEntityTypes.END_WHALE.getId(), Biomes.SMALL_END_ISLANDS, MobCategory.CREATURE, 1, 1, 1, 400.0D, 1.0D),
            EntitySpawnData.of(CNBEntityTypes.CINDERSHELL.getId(), Biomes.NETHER_WASTES, MobCategory.CREATURE, 400, 2, 8, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.BADLANDS, MobCategory.CREATURE, 15, 1, 4, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.WOODED_BADLANDS, MobCategory.CREATURE, 15, 1, 4, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.ERODED_BADLANDS, MobCategory.CREATURE, 15, 1, 4, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.DESERT, MobCategory.CREATURE, 15, 1, 4, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.JUNGLE, MobCategory.CREATURE, 100, 1, 4, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.BAMBOO_JUNGLE, MobCategory.CREATURE, 100, 1, 4, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.SPARSE_JUNGLE, MobCategory.CREATURE, 100, 1, 4, 0, 0),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.MUSHROOM_FIELDS, MobCategory.CREATURE, 10, 1, 4, 0, 0)
    )) {
        @Override
        public String toString() {
            return "";
        }
    };
}
