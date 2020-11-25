package com.cgessinger.creaturesandbeasts.common.world.gen;

import java.util.List;
import java.util.Set;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.entites.CyndershellEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
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
		List<Spawners> base = event.getSpawns().getSpawner(EntityClassification.CREATURE);
		if (types.contains(BiomeDictionary.Type.RIVER))
		{
			base.add(new Spawners(ModEntityTypes.LITTLE_GREBE.get(), 30, 2, 3));
		}
		if (types.contains(BiomeDictionary.Type.SANDY) || types.contains(BiomeDictionary.Type.MESA) || types.contains(BiomeDictionary.Type.JUNGLE))
		{
			base.add(new Spawners(ModEntityTypes.LIZARD.get(), 50, 1, 4));
		}
		if (types.contains(BiomeDictionary.Type.NETHER))
		{
			base.add(new Spawners(ModEntityTypes.CYNDERSHELL.get(), 700, 1, 2));
		}
	}

	public static void EntitySpawnPlacementRegistry() {
		/*
		 * This registers the entity to only spawn where it's possible, which is set by
		 * the new method in the Entity class EX: canCyndershellSpawn Only allows it
		 * spawn everything but Netherwarts, while the placement type here always makes
		 * sure it's on soild blocks, not lava.
		 */
		EntitySpawnPlacementRegistry.register(ModEntityTypes.CYNDERSHELL.get(),
				EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
				CyndershellEntity::canCyndershellSpawn);
	}
}
