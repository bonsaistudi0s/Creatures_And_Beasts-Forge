package com.cgessinger.creaturesandbeasts.common.world.gen;

import java.util.List;
import java.util.Set;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.entites.CindershellEntity;
import com.cgessinger.creaturesandbeasts.common.entites.FriendlySporelingEntity;
import com.cgessinger.creaturesandbeasts.common.entites.HostileSporelingEntity;
import com.cgessinger.creaturesandbeasts.common.entites.NeutralSporelingEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo.Spawners;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreaturesAndBeasts.MOD_ID)
public class ModEntitySpawns
{
	@SubscribeEvent
	public static void spawnEntities (BiomeLoadingEvent event)
	{
		RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName());
		Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);

		//Biome.Category category = event.getCategory();
		List<Spawners> base = event.getSpawns().getSpawner(EntityClassification.CREATURE);
		if (types.contains(BiomeDictionary.Type.RIVER))
		{
			base.add(new Spawners(ModEntityTypes.LITTLE_GREBE.get(), 30, 2, 3));
		}
		if (types.contains(BiomeDictionary.Type.SANDY) || types.contains(BiomeDictionary.Type.MESA) || types.contains(BiomeDictionary.Type.JUNGLE))
		{
			base.add(new Spawners(ModEntityTypes.LIZARD.get(), 50, 1, 4));
		}
		if (types.contains(BiomeDictionary.Type.MUSHROOM))
		{
			base.add(new Spawners(ModEntityTypes.FRIENDLY_SPORELING.get(), 40, 3, 5));
		}
		if (types.contains(BiomeDictionary.Type.SPOOKY))
		{
			base.add(new Spawners(ModEntityTypes.FRIENDLY_SPORELING.get(), 40, 3, 5));
		}
		if (types.contains(BiomeDictionary.Type.SWAMP))
		{
			base.add(new Spawners(ModEntityTypes.FRIENDLY_SPORELING.get(), 40, 3, 5));
			base.add(new Spawners(ModEntityTypes.LILYTAD.get(), 70, 1, 1));
		}
		if (types.contains(BiomeDictionary.Type.NETHER))
		{
			base.add(new Spawners(ModEntityTypes.CINDERSHELL.get(), 200, 1, 2));
			event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new Spawners(ModEntityTypes.HOSTILE_SPORELING.get(), 10, 3, 5));
			event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new Spawners(ModEntityTypes.NEUTRAL_SPORELING.get(), 10, 2, 4));
		}
		if(types.contains(BiomeDictionary.Type.SNOWY))
		{
			base.add(new Spawners(ModEntityTypes.YETI.get(), 1, 2, 3));
		}
	}

	public static void EntitySpawnPlacementRegistry ()
	{
		EntitySpawnPlacementRegistry.register(ModEntityTypes.CINDERSHELL.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CindershellEntity::canCindershellSpawn);

		EntitySpawnPlacementRegistry.register(ModEntityTypes.HOSTILE_SPORELING.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileSporelingEntity::canSporelingSpawn);

		EntitySpawnPlacementRegistry.register(ModEntityTypes.NEUTRAL_SPORELING.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NeutralSporelingEntity::canSporelingSpawn);

		EntitySpawnPlacementRegistry.register(ModEntityTypes.FRIENDLY_SPORELING.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, FriendlySporelingEntity::canSporelingSpawn);
		
		EntitySpawnPlacementRegistry.register(ModEntityTypes.YETI.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::canAnimalSpawn);
	}
}
