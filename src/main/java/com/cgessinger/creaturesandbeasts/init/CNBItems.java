package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.items.AppleSliceItem;
import com.cgessinger.creaturesandbeasts.items.CindershellShellShardItem;
import com.cgessinger.creaturesandbeasts.items.EntityNetItem;
import com.cgessinger.creaturesandbeasts.items.LilytadFlowerItem;
import com.cgessinger.creaturesandbeasts.items.LizardEgg;
import com.cgessinger.creaturesandbeasts.items.ModEntityBucket;
import com.cgessinger.creaturesandbeasts.items.CNBSpawnEggItem;
import com.cgessinger.creaturesandbeasts.items.SporelingSpawnEgg;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CNBItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreaturesAndBeasts.MOD_ID);

    public static final RegistryObject<ModEntityBucket> CINDERSHELL_BUCKET = ITEMS.register("cindershell_bucket", () -> new ModEntityBucket(CNBEntityTypes.CINDERSHELL, Fluids.LAVA, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static final RegistryObject<AppleSliceItem> APPLE_SLICE = ITEMS.register("apple_slice", AppleSliceItem::new);
    public static final RegistryObject<LizardEgg> LIZARD_EGG = ITEMS.register("lizard_egg", LizardEgg::new);
    public static final RegistryObject<EntityNetItem> ENTITY_NET = ITEMS.register("entity_net", EntityNetItem::new);
    public static final RegistryObject<CindershellShellShardItem> CINDERSHELL_SHELL_SHARD = ITEMS.register("cindershell_shell_shard", CindershellShellShardItem::new);
    public static final RegistryObject<LilytadFlowerItem> LILYTAD_FLOWER_PINK = ITEMS.register("lilytad_flower_pink", LilytadFlowerItem::new);
    public static final RegistryObject<Item> YETI_ANTLER = ITEMS.register("yeti_antler", () -> new Item(new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static final RegistryObject<Item> YETI_HIDE = ITEMS.register("yeti_hide", () -> new Item(new Item.Properties().tab(CreaturesAndBeasts.TAB)));

    // Spawn Eggs
    public static RegistryObject<CNBSpawnEggItem> GREBE_SPAWN_EGG = ITEMS.register("little_grebe_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.LITTLE_GREBE, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> CINDERSHELL_SPAWN_EGG = ITEMS.register("cindershell_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.CINDERSHELL, 0x0D0403, 0xC64500, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> LILYTAD_SPAWN_EGG = ITEMS.register("lilytad_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.LILYTAD, 0x37702E, 0x102417, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> YETI_SPAWN_EGG = ITEMS.register("yeti_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.YETI, 0xD7E1E7, 0x887E96, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> LIZARD_SPAWN_EGG = ITEMS.register("lizard_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> LIZARD_ITEM_0 = ITEMS.register("lizard_item_0", () -> new CNBSpawnEggItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> LIZARD_ITEM_1 = ITEMS.register("lizard_item_1", () -> new CNBSpawnEggItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> LIZARD_ITEM_2 = ITEMS.register("lizard_item_2", () -> new CNBSpawnEggItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> LIZARD_ITEM_3 = ITEMS.register("lizard_item_3", () -> new CNBSpawnEggItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> SPORELING_OVERWORLD_EGG = ITEMS.register("sporeling_overworld_egg", () -> new SporelingSpawnEgg(CNBEntityTypes.SPORELING, 0x522F1D, 0x23AD17, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> SPORELING_NETHER_EGG = ITEMS.register("sporeling_nether_egg", () -> new SporelingSpawnEgg(CNBEntityTypes.SPORELING, 0x5C0306, 0xD6351C, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
}
