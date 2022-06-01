package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.items.CNBEntityBucketItem;
import com.cgessinger.creaturesandbeasts.items.CNBFuelItem;
import com.cgessinger.creaturesandbeasts.items.CNBSpawnEggItem;
import com.cgessinger.creaturesandbeasts.items.CinderSwordItem;
import com.cgessinger.creaturesandbeasts.items.FlowerCrownItem;
import com.cgessinger.creaturesandbeasts.items.GlowingFlowerCrownItem;
import com.cgessinger.creaturesandbeasts.items.LizardEggItem;
import com.cgessinger.creaturesandbeasts.items.LizardItem;
import com.cgessinger.creaturesandbeasts.items.MinipadFlowerGlowItem;
import com.cgessinger.creaturesandbeasts.items.SporelingSpawnEggItem;
import com.cgessinger.creaturesandbeasts.items.WaterlilyBlockItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
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

    public static final RegistryObject<Item> PINK_MINIPAD_FLOWER = ITEMS.register("pink_minipad_flower", () -> new Item(new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static final RegistryObject<Item> LIGHT_PINK_MINIPAD_FLOWER = ITEMS.register("light_pink_minipad_flower", () -> new Item(new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static final RegistryObject<Item> YELLOW_MINIPAD_FLOWER = ITEMS.register("yellow_minipad_flower", () -> new Item(new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static final RegistryObject<Item> PINK_MINIPAD_FLOWER_GLOW = ITEMS.register("pink_minipad_flower_glow", () -> new MinipadFlowerGlowItem(new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static final RegistryObject<Item> LIGHT_PINK_MINIPAD_FLOWER_GLOW = ITEMS.register("light_pink_minipad_flower_glow", () -> new MinipadFlowerGlowItem(new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static final RegistryObject<Item> YELLOW_MINIPAD_FLOWER_GLOW = ITEMS.register("yellow_minipad_flower_glow", () -> new MinipadFlowerGlowItem(new Item.Properties().tab(CreaturesAndBeasts.TAB)));

    public static final RegistryObject<Item> HEAL_SPELL_BOOK = ITEMS.register("heal_spell_book", () -> new Item(new Item.Properties().tab(CreaturesAndBeasts.TAB)));

    // Armor
    public static final RegistryObject<FlowerCrownItem> FLOWER_CROWN = ITEMS.register("flower_crown", () -> new FlowerCrownItem(CNBArmorMaterials.FLOWER_CROWN, Ingredient.of(PINK_MINIPAD_FLOWER.get(), LIGHT_PINK_MINIPAD_FLOWER.get(), YELLOW_MINIPAD_FLOWER.get()), EquipmentSlot.HEAD, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static final RegistryObject<GlowingFlowerCrownItem> GLOWING_FLOWER_CROWN = ITEMS.register("glowing_flower_crown", () -> new GlowingFlowerCrownItem(CNBArmorMaterials.FLOWER_CROWN, Ingredient.of(PINK_MINIPAD_FLOWER_GLOW.get(), LIGHT_PINK_MINIPAD_FLOWER_GLOW.get(), YELLOW_MINIPAD_FLOWER_GLOW.get()), EquipmentSlot.HEAD, new Item.Properties().tab(CreaturesAndBeasts.TAB)));

    // Tools
    public static final RegistryObject<CinderSwordItem> CINDER_SWORD = ITEMS.register("cinder_sword", () -> new CinderSwordItem(CNBItemTiers.CINDER, 3, -2.4F, new Item.Properties().tab(CreaturesAndBeasts.TAB)));

    // Spawn Eggs
    public static RegistryObject<CNBSpawnEggItem> GREBE_SPAWN_EGG = ITEMS.register("little_grebe_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.LITTLE_GREBE, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> CINDERSHELL_SPAWN_EGG = ITEMS.register("cindershell_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.CINDERSHELL, 0x0D0403, 0xC64500, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> LILYTAD_SPAWN_EGG = ITEMS.register("lilytad_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.LILYTAD, 0x37702E, 0x102417, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> YETI_SPAWN_EGG = ITEMS.register("yeti_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.YETI, 0xD7E1E7, 0x887E96, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> MINIPAD_SPAWN_EGG = ITEMS.register("minipad_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.MINIPAD, 0x3EA62E, 0x194F28, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> LIZARD_SPAWN_EGG = ITEMS.register("lizard_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> END_WHALE_SPAWN_EGG = ITEMS.register("end_whale_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.END_WHALE, 0x5609AD, 0xD4AD5F, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<CNBSpawnEggItem> CACTEM_SPAWN_EGG = ITEMS.register("cactem_spawn_egg", () -> new CNBSpawnEggItem(CNBEntityTypes.CACTEM, 0x1A6E23, 0xDCEBAB, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<LizardItem> LIZARD_ITEM_DESERT = ITEMS.register("lizard_item_desert", () -> new LizardItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB), CNBLizardTypes.DESERT));
    public static RegistryObject<LizardItem> LIZARD_ITEM_DESERT_2 = ITEMS.register("lizard_item_desert_2", () -> new LizardItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB), CNBLizardTypes.DESERT_2));
    public static RegistryObject<LizardItem> LIZARD_ITEM_JUNGLE = ITEMS.register("lizard_item_jungle", () -> new LizardItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB), CNBLizardTypes.JUNGLE));
    public static RegistryObject<LizardItem> LIZARD_ITEM_JUNGLE_2 = ITEMS.register("lizard_item_jungle_2", () -> new LizardItem(CNBEntityTypes.LIZARD, 0x00FFFFFF, 0x00FFFFFF, new Item.Properties().tab(CreaturesAndBeasts.TAB), CNBLizardTypes.JUNGLE_2));
    public static RegistryObject<SporelingSpawnEggItem> SPORELING_OVERWORLD_EGG = ITEMS.register("sporeling_overworld_egg", () -> new SporelingSpawnEggItem(CNBEntityTypes.SPORELING, 0x522F1D, 0x23AD17, new Item.Properties().tab(CreaturesAndBeasts.TAB)));
    public static RegistryObject<SporelingSpawnEggItem> SPORELING_NETHER_EGG = ITEMS.register("sporeling_nether_egg", () -> new SporelingSpawnEggItem(CNBEntityTypes.SPORELING, 0x5C0306, 0xD6351C, new Item.Properties().tab(CreaturesAndBeasts.TAB)));

    // Block Items
    public static RegistryObject<BlockItem> CINDERSHELL_FURNACE = ITEMS.register("cinder_furnace", () -> new BlockItem(CNBBlocks.CINDER_FURNACE.get(), new Item.Properties().tab(CreaturesAndBeasts.TAB)));
}
