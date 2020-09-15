package com.cgessinger.creaturesandbeasts.common.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.items.ModSpawnEggItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreaturesAndBeasts.MOD_ID);

	public static RegistryObject<ModSpawnEggItem> GREBE_SPAWN_EGG = ITEMS.register("little_grebe_spawn_egg",
			() -> new ModSpawnEggItem(ModEntityTypes.LITTLE_GREBE,0x201d1c, 0x6d3021, new Item.Properties().group(CreaturesAndBeasts.TAB)));
	
	public static RegistryObject<ModSpawnEggItem> LIZARD_SPAWN_EGG = ITEMS.register("lizard_spawn_egg",
			() -> new ModSpawnEggItem(ModEntityTypes.LIZARD,0x00FFFFFF, 0x00FFFFFF, new Item.Properties().group(CreaturesAndBeasts.TAB)));
}
