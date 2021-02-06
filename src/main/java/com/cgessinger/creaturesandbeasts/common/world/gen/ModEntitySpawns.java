package com.cgessinger.creaturesandbeasts.common.world.gen;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.config.EntityConfig;
import com.cgessinger.creaturesandbeasts.common.config.CNBConfig.ServerConfig;
import com.cgessinger.creaturesandbeasts.common.entites.CindershellEntity;
import com.cgessinger.creaturesandbeasts.common.entites.FriendlySporelingEntity;
import com.cgessinger.creaturesandbeasts.common.entites.HostileSporelingEntity;
import com.cgessinger.creaturesandbeasts.common.entites.NeutralSporelingEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.MobSpawnInfo.Spawners;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreaturesAndBeasts.MOD_ID)
public class ModEntitySpawns 
{

    @SubscribeEvent
	public static void spawnEntities (BiomeLoadingEvent event)
	{
        String biomeName = event.getName().toString();

        checkAndAddSpawn(event, biomeName, ServerConfig.GREBE_CONFIG, ModEntityTypes.LITTLE_GREBE.get(), EntityClassification.CREATURE, 2, 3);

        checkAndAddSpawn(event, biomeName, ServerConfig.LIZARD_CONFIG, ModEntityTypes.LIZARD.get(), EntityClassification.CREATURE, 1, 4);

        checkAndAddSpawn(event, biomeName, ServerConfig.CINDERSHELL_CONFIG, ModEntityTypes.CINDERSHELL.get(), EntityClassification.MONSTER, 1, 2);

        checkAndAddSpawn(event, biomeName, ServerConfig.FRIENDLY_SPORELING_CONFIG, ModEntityTypes.FRIENDLY_SPORELING.get(), EntityClassification.CREATURE, 3, 5);

        checkAndAddSpawn(event, biomeName, ServerConfig.HOSTILE_SPORELING_CONFIG, ModEntityTypes.HOSTILE_SPORELING.get(), EntityClassification.MONSTER, 2, 4);

        checkAndAddSpawn(event, biomeName, ServerConfig.NEUTRAL_SPORELING_CONFIG, ModEntityTypes.NEUTRAL_SPORELING.get(), EntityClassification.MONSTER, 2, 4);

        checkAndAddSpawn(event, biomeName, ServerConfig.LILYTAD_CONFIG, ModEntityTypes.LILYTAD.get(), EntityClassification.CREATURE, 1, 1);

        checkAndAddSpawn(event, biomeName, ServerConfig.YETI_CONFIG, ModEntityTypes.YETI.get(), EntityClassification.CREATURE, 2, 3);
	}

    private static void checkAndAddSpawn (BiomeLoadingEvent event, String biomeName, EntityConfig config, EntityType<? extends Entity> type, EntityClassification classification, int min, int max)
    {
        if(config.spawnBiomes.contains(biomeName))
        {
            MobSpawnInfo.Spawners spawnInfo = new Spawners(type, config.spawnRate, min, max);
            event.getSpawns().getSpawner(classification).add(spawnInfo);
        }
    }

	public static void entitySpawnPlacementRegistry ()
	{
		EntitySpawnPlacementRegistry.register(ModEntityTypes.CINDERSHELL.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CindershellEntity::canCindershellSpawn);

		EntitySpawnPlacementRegistry.register(ModEntityTypes.HOSTILE_SPORELING.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileSporelingEntity::canSporelingSpawn);

		EntitySpawnPlacementRegistry.register(ModEntityTypes.NEUTRAL_SPORELING.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NeutralSporelingEntity::canSporelingSpawn);

		EntitySpawnPlacementRegistry.register(ModEntityTypes.FRIENDLY_SPORELING.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, FriendlySporelingEntity::canSporelingSpawn);
		
		EntitySpawnPlacementRegistry.register(ModEntityTypes.YETI.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
	}
}
