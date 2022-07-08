package com.cgessinger.creaturesandbeasts.world.gen;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.config.EntitySpawnData;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import com.cgessinger.creaturesandbeasts.entities.EndWhaleEntity;
import com.cgessinger.creaturesandbeasts.entities.LilytadEntity;
import com.cgessinger.creaturesandbeasts.entities.LittleGrebeEntity;
import com.cgessinger.creaturesandbeasts.entities.LizardEntity;
import com.cgessinger.creaturesandbeasts.entities.MinipadEntity;
import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import com.cgessinger.creaturesandbeasts.entities.YetiEntity;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;

@Mod.EventBusSubscriber(modid = CreaturesAndBeasts.MOD_ID)
public class ModEntitySpawns {

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        if (event.getCategory() == null) {
            return;
        } else if (event.getName() == null) {
            return;
        }

        ResourceLocation name = event.getName();
        ResourceKey<Biome> biome = ResourceKey.create(Registry.BIOME_REGISTRY, name);

        onAddSpawns(event, biome);
    }

    private static void onAddSpawns(BiomeLoadingEvent event, ResourceKey<Biome> biome) {
        for (EntitySpawnData spawn : CNBConfig.spawns) {
            // For safety ;)
            if (spawn == null) {
                CreaturesAndBeasts.LOGGER.traceExit(
                        "SpawnData is null! This should never happen, please report this to the developers at: %s",
                        ((ModFileInfo) ModList.get().getModFileById(CreaturesAndBeasts.MOD_ID)).getIssueURL().toString());
                continue;
            }
            if (spawn.getEntityType() == null) {
                CreaturesAndBeasts.LOGGER.traceExit(
                        "EntityType is null! This should never happen, please report this to the developers at: %s",
                        ((ModFileInfo) ModList.get().getModFileById(CreaturesAndBeasts.MOD_ID)).getIssueURL().toString());
                continue;
            }
            if (spawn.getBiome() == null) {
                CreaturesAndBeasts.LOGGER.traceExit(
                        "Biome is null! This should never happen, please report this to the developers at: %s",
                        ((ModFileInfo) ModList.get().getModFileById(CreaturesAndBeasts.MOD_ID)).getIssueURL().toString());
                continue;
            }
            if (spawn.getCategory() == null) {
                CreaturesAndBeasts.LOGGER.traceExit(
                        "SpawnCategory is null! This should never happen, please report this to the developers at: %s",
                        ((ModFileInfo) ModList.get().getModFileById(CreaturesAndBeasts.MOD_ID)).getIssueURL().toString());
                continue;
            }
            if (spawn.getBiome().equals(biome)) {
                event.getSpawns().addSpawn(spawn.getCategory(), new MobSpawnSettings.SpawnerData(spawn.getEntityType(), spawn.getSpawnWeight(), spawn.getMinCount(), spawn.getMaxCount()));
                if (spawn.getMobCost() > 0 && spawn.getEnergyBudget() > 0) {
                    event.getSpawns().addMobCharge(spawn.getEntityType(), spawn.getMobCost(), spawn.getEnergyBudget());
                }
            }
        }
    }

    public static void entitySpawnPlacementRegistry() {
        SpawnPlacements.register(CNBEntityTypes.LITTLE_GREBE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LittleGrebeEntity::checkGrebeSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.LIZARD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LizardEntity::checkLizardSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.CINDERSHELL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CindershellEntity::checkCindershellSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.SPORELING.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SporelingEntity::checkSporelingSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.LILYTAD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LilytadEntity::checkLilytadSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.YETI.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YetiEntity::checkMobSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.MINIPAD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MinipadEntity::checkMinipadSpawnRules);
        SpawnPlacements.register(CNBEntityTypes.END_WHALE.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EndWhaleEntity::checkEndWhaleSpawnRules);
    }
}
