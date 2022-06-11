package com.cgessinger.creaturesandbeasts.config;

import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
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
            EntitySpawnData.of(CNBEntityTypes.YETI.getId(), Biomes.SNOWY_PLAINS, 1, 2, 3),
            EntitySpawnData.of(CNBEntityTypes.YETI.getId(), Biomes.ICE_SPIKES, 1, 2, 3),
            EntitySpawnData.of(CNBEntityTypes.YETI.getId(), Biomes.SNOWY_TAIGA, 1, 2, 3),
            EntitySpawnData.of(CNBEntityTypes.YETI.getId(), Biomes.FROZEN_PEAKS, 1, 2, 3),
            EntitySpawnData.of(CNBEntityTypes.YETI.getId(), Biomes.SNOWY_SLOPES, 1, 2, 3),
            EntitySpawnData.of(CNBEntityTypes.LITTLE_GREBE.getId(), Biomes.RIVER, 30, 2, 3),
            EntitySpawnData.of(CNBEntityTypes.CACTEM.getId(), Biomes.BADLANDS, 8, 6, 13),
            EntitySpawnData.of(CNBEntityTypes.CACTEM.getId(), Biomes.WOODED_BADLANDS, 8, 6, 13),
            EntitySpawnData.of(CNBEntityTypes.CACTEM.getId(), Biomes.ERODED_BADLANDS, 8, 6, 13),
            EntitySpawnData.of(CNBEntityTypes.CACTEM.getId(), Biomes.DESERT, 8, 6, 13),
            EntitySpawnData.of(CNBEntityTypes.SPORELING.getId(), Biomes.MUSHROOM_FIELDS, 40, 3, 5),
            EntitySpawnData.of(CNBEntityTypes.SPORELING.getId(), Biomes.SWAMP, 40, 3, 5),
            EntitySpawnData.of(CNBEntityTypes.SPORELING.getId(), Biomes.LUSH_CAVES, 40, 3, 5),
            EntitySpawnData.of(CNBEntityTypes.SPORELING.getId(), Biomes.DARK_FOREST, 40, 3, 5),
            EntitySpawnData.of(CNBEntityTypes.SPORELING.getId(), Biomes.NETHER_WASTES, 10, 3, 5),
            EntitySpawnData.of(CNBEntityTypes.SPORELING.getId(), Biomes.WARPED_FOREST, 5, 3, 5),
            EntitySpawnData.of(CNBEntityTypes.SPORELING.getId(), Biomes.CRIMSON_FOREST, 5, 3, 5),
            EntitySpawnData.of(CNBEntityTypes.LILYTAD.getId(), Biomes.SWAMP, 35, 1, 1),
            EntitySpawnData.of(CNBEntityTypes.MINIPAD.getId(), Biomes.SWAMP, 10, 3, 6),
            EntitySpawnData.of(CNBEntityTypes.END_WHALE.getId(), Biomes.THE_END, 1, 1, 1),
            EntitySpawnData.of(CNBEntityTypes.END_WHALE.getId(), Biomes.END_BARRENS, 1, 1, 1),
            EntitySpawnData.of(CNBEntityTypes.END_WHALE.getId(), Biomes.END_HIGHLANDS, 1, 1, 1),
            EntitySpawnData.of(CNBEntityTypes.END_WHALE.getId(), Biomes.END_MIDLANDS, 1, 1, 1),
            EntitySpawnData.of(CNBEntityTypes.END_WHALE.getId(), Biomes.SMALL_END_ISLANDS, 1, 1, 1),
            EntitySpawnData.of(CNBEntityTypes.CINDERSHELL.getId(), Biomes.NETHER_WASTES, 10, 1, 2),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.BADLANDS, 50, 1, 4),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.WOODED_BADLANDS, 50, 1, 4),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.ERODED_BADLANDS, 50, 1, 4),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.DESERT, 50, 1, 4),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.JUNGLE, 50, 1, 4),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.BAMBOO_JUNGLE, 50, 1, 4),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.SPARSE_JUNGLE, 50, 1, 4),
            EntitySpawnData.of(CNBEntityTypes.LIZARD.getId(), Biomes.MUSHROOM_FIELDS, 50, 1, 4)
    )) {
        @Override
        public String toString() {
            return "";
        }
    };
}
