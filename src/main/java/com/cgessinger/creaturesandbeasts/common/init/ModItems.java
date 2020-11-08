package com.cgessinger.creaturesandbeasts.common.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.items.AppleSliceItem;
import com.cgessinger.creaturesandbeasts.common.items.EntityNetItem;
import com.cgessinger.creaturesandbeasts.common.items.ModSpawnEggItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class ModItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreaturesAndBeasts.MOD_ID);

	public static RegistryObject<ModSpawnEggItem> GREBE_SPAWN_EGG = ITEMS.register("little_grebe_spawn_egg",
			() -> new ModSpawnEggItem(ModEntityTypes.LITTLE_GREBE,0x00FFFFFF, 0x00FFFFFF, new Item.Properties().group(CreaturesAndBeasts.TAB)));

	public static RegistryObject<ModSpawnEggItem> CYNDERSHELL_SPAWN_EGG = ITEMS.register("cyndershell_spawn_egg",
			() -> new ModSpawnEggItem(ModEntityTypes.CYNDERSHELL,0x0D0403, 0xC64500, new Item.Properties().group(CreaturesAndBeasts.TAB)));
	
	public static RegistryObject<ModSpawnEggItem> LIZARD_SPAWN_EGG = ITEMS.register("lizard_spawn_egg",
			() -> new ModSpawnEggItem(ModEntityTypes.LIZARD,0x00FFFFFF, 0x00FFFFFF, new Item.Properties().group(CreaturesAndBeasts.TAB)));

	public static Map<Integer, RegistryObject<ModSpawnEggItem>> LIZARD_SPAWN_MAP = new HashMap<>();
	public static RegistryObject<ModSpawnEggItem> LIZARD_ITEM_0 = ITEMS.register("lizard_item_0",
			() -> new ModSpawnEggItem(ModEntityTypes.LIZARD,0x00FFFFFF, 0x00FFFFFF, new Item.Properties().group(CreaturesAndBeasts.TAB)).spawnsVariant(0));

	public static RegistryObject<ModSpawnEggItem> LIZARD_ITEM_1 = ITEMS.register("lizard_item_1",
			() -> new ModSpawnEggItem(ModEntityTypes.LIZARD,0x00FFFFFF, 0x00FFFFFF, new Item.Properties().group(CreaturesAndBeasts.TAB)).spawnsVariant(1));

	public static RegistryObject<ModSpawnEggItem> LIZARD_ITEM_2 = ITEMS.register("lizard_item_2",
			() -> new ModSpawnEggItem(ModEntityTypes.LIZARD,0x00FFFFFF, 0x00FFFFFF, new Item.Properties().group(CreaturesAndBeasts.TAB)).spawnsVariant(2));

	public static RegistryObject<ModSpawnEggItem> LIZARD_ITEM_3 = ITEMS.register("lizard_item_3",
			() -> new ModSpawnEggItem(ModEntityTypes.LIZARD,0x00FFFFFF, 0x00FFFFFF, new Item.Properties().group(CreaturesAndBeasts.TAB)).spawnsVariant(3));

	static {
		LIZARD_SPAWN_MAP.put(0, LIZARD_ITEM_0);
		LIZARD_SPAWN_MAP.put(1, LIZARD_ITEM_1);
		LIZARD_SPAWN_MAP.put(2, LIZARD_ITEM_2);
		LIZARD_SPAWN_MAP.put(3, LIZARD_ITEM_3);
	}

	public static final RegistryObject<AppleSliceItem> APPLE_SLICE = ITEMS.register("apple_slice", AppleSliceItem::new);
	public static final RegistryObject<EntityNetItem> ENTITY_NET = ITEMS.register("entity_net", EntityNetItem::new);
	public static final RegistryObject<Item> CYNDERSHELL_SHELL_SHARD = ITEMS.register("cyndershell_shell_shard", () -> new Item(new Item.Properties().group(CreaturesAndBeasts.TAB)));
}
