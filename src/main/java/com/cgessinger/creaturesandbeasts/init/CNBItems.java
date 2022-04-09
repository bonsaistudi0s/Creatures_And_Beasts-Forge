package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.items.CNBEntityBucketItem;
import com.cgessinger.creaturesandbeasts.items.CNBFuelItem;
import com.cgessinger.creaturesandbeasts.items.CNBSpawnEggItem;
import com.cgessinger.creaturesandbeasts.items.LizardEggItem;
import com.cgessinger.creaturesandbeasts.items.LizardItem;
import com.cgessinger.creaturesandbeasts.items.SporelingSpawnEggItem;
import com.cgessinger.creaturesandbeasts.items.WaterlilyBlockItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CNBItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreaturesAndBeasts.MOD_ID);

    // Food
    public static final RegistryObject<Item> APPLE_SLICE = ITEMS.register("apple_slice", () -> new Item(new Item.Properties().tab(CreaturesAndBeasts.TAB)
            .food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).build())));
    public static final RegistryObject<WaterlilyBlockItem> PINK_WATERLILY = ITEMS.register("pink_waterlily", () -> new WaterlilyBlockItem(CNBBlocks.PINK_WATERLILY_BLOCK.get(), new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.5F).alwaysEat()
                    .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1), 1.0F).build())));
    public static final RegistryObject<WaterlilyBlockItem> LIGHT_PINK_WATERLILY = ITEMS.register("light_pink_waterlily", () -> new WaterlilyBlockItem(CNBBlocks.LIGHT_PINK_WATERLILY_BLOCK.get(), new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.5F).alwaysEat()
                    .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1), 1.0F).build())));
    public static final RegistryObject<WaterlilyBlockItem> YELLOW_WATERLILY = ITEMS.register("yellow_waterlily", () -> new WaterlilyBlockItem(CNBBlocks.YELLOW_WATERLILY_BLOCK.get(), new Item.Properties()
            .food(new FoodProperties.Builder().nutrition(4).saturationMod(0.5F).alwaysEat()
                    .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1), 1.0F).build())));

    // Bucketed Mobs
    public static final RegistryObject<CNBEntityBucketItem> CINDERSHELL_BUCKET = ITEMS.register("cindershell_bucket", () -> new CNBEntityBucketItem(CNBEntityTypes.CINDERSHELL::get, Fluids.LAVA, () -> SoundEvents.BUCKET_EMPTY_LAVA, new Item.Properties().stacksTo(1).tab(CreaturesAndBeasts.TAB)));

    // Misc. Items
    public static final RegistryObject<Item> ENTITY_NET = ITEMS.register("entity_net", () -> new Item(new Item.Properties().tab(CreaturesAndBeasts.TAB).durability(64)));
    public static final RegistryObject<LizardEggItem> LIZARD_EGG = ITEMS.register("lizard_egg", () -> new LizardEggItem(CNBBlocks.LIZARD_EGGS.get()));
    public static final RegistryObject<CNBFuelItem> CINDERSHELL_SHELL_SHARD = ITEMS.register("cindershell_shell_shard", () -> new CNBFuelItem(6400));
    public static final RegistryObject<Item> YETI_ANTLER = ITEMS.register("yeti_antler", () -> new Item(new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static final RegistryObject<Item> YETI_HIDE = ITEMS.register("yeti_hide", () -> new Item(new Item.Properties().tab(CreaturesAndBeasts.TAB)));

    // Spawn Eggs
    public static RegistryObject<CNBSpawnEggItem> GREBE_SPAWN_EGG = ITEMS.register("little_grebe_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.LITTLE_GREBE, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> CINDERSHELL_SPAWN_EGG = ITEMS.register("cindershell_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.CINDERSHELL, 0x0D0403, 0xC64500, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> LILYTAD_SPAWN_EGG = ITEMS.register("lilytad_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.LILYTAD, 0x37702E, 0x102417, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> YETI_SPAWN_EGG = ITEMS.register("yeti_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.YETI, 0xD7E1E7, 0x887E96, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> LIZARD_SPAWN_EGG = ITEMS.register("lizard_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<LizardItem> LIZARD_ITEM_DESERT = ITEMS.register("lizard_item_desert", () -> new LizardItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB), CNBLizardTypes.DESERT));
    public static RegistryObject<LizardItem> LIZARD_ITEM_DESERT_2 = ITEMS.register("lizard_item_desert_2", () -> new LizardItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB), CNBLizardTypes.DESERT_2));
    public static RegistryObject<LizardItem> LIZARD_ITEM_JUNGLE = ITEMS.register("lizard_item_jungle", () -> new LizardItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB), CNBLizardTypes.JUNGLE));
    public static RegistryObject<LizardItem> LIZARD_ITEM_JUNGLE_2 = ITEMS.register("lizard_item_jungle_2", () -> new LizardItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB), CNBLizardTypes.JUNGLE_2));
    public static RegistryObject<SporelingSpawnEggItem> SPORELING_OVERWORLD_EGG = ITEMS.register("sporeling_overworld_egg", () -> new SporelingSpawnEggItem(CNBEntityTypes.SPORELING, 0x522F1D, 0x23AD17, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<SporelingSpawnEggItem> SPORELING_NETHER_EGG = ITEMS.register("sporeling_nether_egg", () -> new SporelingSpawnEggItem(CNBEntityTypes.SPORELING, 0x5C0306, 0xD6351C, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
}
