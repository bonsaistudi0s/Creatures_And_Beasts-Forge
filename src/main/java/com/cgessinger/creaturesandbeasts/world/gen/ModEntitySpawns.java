package com.cgessinger.creaturesandbeasts.world.gen;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.config.CNBConfig.ServerConfig;
import com.cgessinger.creaturesandbeasts.config.EntityConfig;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import com.cgessinger.creaturesandbeasts.entities.LittleGrebeEntity;
import com.cgessinger.creaturesandbeasts.entities.LilytadEntity;
import com.cgessinger.creaturesandbeasts.entities.LizardEntity;
import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import com.cgessinger.creaturesandbeasts.entities.YetiEntity;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreaturesAndBeasts.MOD_ID)
public class ModEntitySpawns {

    @SubscribeEvent
    public static void spawnEntities(BiomeLoadingEvent event) {
        String biomeName = event.getName().toString();

        checkAndAddSpawn(event, biomeName, ServerConfig.GREBE_CONFIG, CNBEntityTypes.LITTLE_GREBE.get(), MobCategory.CREATURE, 2, 3);
        checkAndAddSpawn(event, biomeName, ServerConfig.LIZARD_CONFIG, CNBEntityTypes.LIZARD.get(), MobCategory.CREATURE, 1, 4);
        checkAndAddSpawn(event, biomeName, ServerConfig.CINDERSHELL_CONFIG, CNBEntityTypes.CINDERSHELL.get(), MobCategory.MONSTER, 1, 2);
        checkAndAddSpawn(event, biomeName, ServerConfig.FRIENDLY_SPORELING_CONFIG, CNBEntityTypes.SPORELING.get(), MobCategory.CREATURE, 3, 5);
        checkAndAddSpawn(event, biomeName, ServerConfig.LILYTAD_CONFIG, CNBEntityTypes.LILYTAD.get(), MobCategory.CREATURE, 1, 1);
        checkAndAddSpawn(event, biomeName, ServerConfig.YETI_CONFIG, CNBEntityTypes.YETI.get(), MobCategory.CREATURE, 2, 3);
    }

    private static void checkAndAddSpawn(BiomeLoadingEvent event, String biomeName, EntityConfig config, EntityType<? extends Entity> type, MobCategory classification, int min, int max) {
        if (config.spawnBiomes.contains(biomeName)) {
            MobSpawnSettings.SpawnerData spawnInfo = new SpawnerData(type, config.spawnRate, min, max);
            event.getSpawns().getSpawner(classification).add(spawnInfo);
        }
    }

    public static void entitySpawnPlacementRegistry() {
        SpawnPlacements.register(CNBEntityTypes.LITTLE_GREBE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LittleGrebeEntity::checkGrebeSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.LIZARD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LizardEntity::checkLizardSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.CINDERSHELL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CindershellEntity::checkCindershellSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.SPORELING.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SporelingEntity::checkSporelingSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.LILYTAD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LilytadEntity::checkLilytadSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.YETI.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YetiEntity::checkYetiSpawnRules);
    }
}
