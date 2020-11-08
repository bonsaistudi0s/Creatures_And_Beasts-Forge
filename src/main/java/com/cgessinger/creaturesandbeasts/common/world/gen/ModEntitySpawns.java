package com.cgessinger.creaturesandbeasts.common.world.gen;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber(modid= CreaturesAndBeasts.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntitySpawns
{
	@SubscribeEvent
	public static void spawnEntities(FMLLoadCompleteEvent event)
	{
		for(Biome biome : ForgeRegistries.BIOMES)
		{
			Biome.Category category = biome.getCategory();
			if(category.equals(Biome.Category.RIVER))
			{
				MobSpawnInfo.Spawners info = new MobSpawnInfo.Spawners(ModEntityTypes.LITTLE_GREBE.get(), 30, 2, 3);
				addMobSpawnToBiome(biome, EntityClassification.CREATURE, info);
			}
			else if (category.equals(Biome.Category.DESERT) || category.equals(Biome.Category.MESA) || category.equals(Biome.Category.JUNGLE))
			{
				MobSpawnInfo.Spawners info = new MobSpawnInfo.Spawners(ModEntityTypes.LIZARD.get(), 50, 1, 4);
				addMobSpawnToBiome(biome, EntityClassification.CREATURE, info);
			}
			else if(category.equals(Biome.Category.NETHER))
			{
				MobSpawnInfo.Spawners info = new MobSpawnInfo.Spawners(ModEntityTypes.CYNDERSHELL.get(), 700, 1, 2);
				addMobSpawnToBiome(biome, EntityClassification.CREATURE, info);
			}
		}
	}

	public static void addMobSpawnToBiome(Biome biome, EntityClassification classification, MobSpawnInfo.Spawners... spawnInfos) {
		convertImmutableSpawners(biome);
		List<MobSpawnInfo.Spawners> spawnersList = new ArrayList<>(
				biome.func_242433_b().field_242554_e.get(classification));
		spawnersList.addAll(Arrays.asList(spawnInfos));
		biome.func_242433_b().field_242554_e.put(classification, spawnersList);
	}

	// Convert the immutable map to a mutable HashMap in order for us to change the data stored in these maps
	private static void convertImmutableSpawners(Biome biome) {
		if (biome.func_242433_b().field_242554_e instanceof ImmutableMap) {
			biome.func_242433_b().field_242554_e = new HashMap<>(biome.func_242433_b().field_242554_e);
		}
	}
}
