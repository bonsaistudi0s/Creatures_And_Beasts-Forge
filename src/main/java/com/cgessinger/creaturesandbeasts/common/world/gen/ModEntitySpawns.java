package com.cgessinger.creaturesandbeasts.common.world.gen;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.config.EntityConfig;
import com.cgessinger.creaturesandbeasts.common.config.CNBConfig.ServerConfig;
import com.cgessinger.creaturesandbeasts.common.entites.CindershellEntity;
import com.cgessinger.creaturesandbeasts.common.entites.FriendlySporelingEntity;
import com.cgessinger.creaturesandbeasts.common.entites.GrebeEntity;
import com.cgessinger.creaturesandbeasts.common.entites.HostileSporelingEntity;
import com.cgessinger.creaturesandbeasts.common.entites.LilytadEntity;
import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import com.cgessinger.creaturesandbeasts.common.entites.NeutralSporelingEntity;
import com.cgessinger.creaturesandbeasts.common.entites.YetiEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.levelgen.Heightmap;
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

        checkAndAddSpawn(event, biomeName, ServerConfig.GREBE_CONFIG, ModEntityTypes.LITTLE_GREBE.get(), MobCategory.CREATURE, 2, 3);

        checkAndAddSpawn(event, biomeName, ServerConfig.LIZARD_CONFIG, ModEntityTypes.LIZARD.get(), MobCategory.CREATURE, 1, 4);

        checkAndAddSpawn(event, biomeName, ServerConfig.CINDERSHELL_CONFIG, ModEntityTypes.CINDERSHELL.get(), MobCategory.MONSTER, 1, 2);

        checkAndAddSpawn(event, biomeName, ServerConfig.FRIENDLY_SPORELING_CONFIG, ModEntityTypes.FRIENDLY_SPORELING.get(), MobCategory.CREATURE, 3, 5);

        checkAndAddSpawn(event, biomeName, ServerConfig.HOSTILE_SPORELING_CONFIG, ModEntityTypes.HOSTILE_SPORELING.get(), MobCategory.MONSTER, 2, 4);

        checkAndAddSpawn(event, biomeName, ServerConfig.NEUTRAL_SPORELING_CONFIG, ModEntityTypes.NEUTRAL_SPORELING.get(), MobCategory.MONSTER, 2, 4);

        checkAndAddSpawn(event, biomeName, ServerConfig.LILYTAD_CONFIG, ModEntityTypes.LILYTAD.get(), MobCategory.CREATURE, 1, 1);

        checkAndAddSpawn(event, biomeName, ServerConfig.YETI_CONFIG, ModEntityTypes.YETI.get(), MobCategory.CREATURE, 2, 3);
	}

    private static void checkAndAddSpawn (BiomeLoadingEvent event, String biomeName, EntityConfig config, EntityType<? extends Entity> type, MobCategory classification, int min, int max)
    {
        if(config.spawnBiomes.contains(biomeName))
        {
            MobSpawnSettings.SpawnerData spawnInfo = new SpawnerData(type, config.spawnRate, min, max);
            event.getSpawns().getSpawner(classification).add(spawnInfo);
        }
    }

	public static void entitySpawnPlacementRegistry ()
	{
		SpawnPlacements.register(ModEntityTypes.LITTLE_GREBE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GrebeEntity::canGrebeSpawn);

		SpawnPlacements.register(ModEntityTypes.LIZARD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LizardEntity::canLizardSpawn);

		SpawnPlacements.register(ModEntityTypes.CINDERSHELL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CindershellEntity::canCindershellSpawn);

		SpawnPlacements.register(ModEntityTypes.HOSTILE_SPORELING.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, HostileSporelingEntity::canSporelingSpawn);

		SpawnPlacements.register(ModEntityTypes.NEUTRAL_SPORELING.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NeutralSporelingEntity::canSporelingSpawn);

		SpawnPlacements.register(ModEntityTypes.FRIENDLY_SPORELING.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FriendlySporelingEntity::canSporelingSpawn);
        
		SpawnPlacements.register(ModEntityTypes.LILYTAD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LilytadEntity::canLilytadSpawn);
		
		SpawnPlacements.register(ModEntityTypes.YETI.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, YetiEntity::canYetiSpawn);
	}
}
