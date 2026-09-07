package com.cgessinger.creaturesandbeasts.modules;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CNBCreativeTabModule {
    public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "tab"));

    public static final CreativeModeTab TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TAB_KEY, CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(CNBItemModule.APPLE_SLICE.get()))
            .title(Component.translatable("tab.cnb"))
            .displayItems((params, output) -> {
                output.accept(CNBItemModule.APPLE_SLICE.get());
                output.accept(CNBItemModule.PINK_WATERLILY.get());
                output.accept(CNBItemModule.LIGHT_PINK_WATERLILY.get());
                output.accept(CNBItemModule.YELLOW_WATERLILY.get());

                output.accept(CNBItemModule.PINK_MINIPAD_FLOWER.get());
                output.accept(CNBItemModule.PINK_MINIPAD_FLOWER_GLOW.get());
                output.accept(CNBItemModule.LIGHT_PINK_MINIPAD_FLOWER.get());
                output.accept(CNBItemModule.LIGHT_PINK_MINIPAD_FLOWER_GLOW.get());
                output.accept(CNBItemModule.YELLOW_MINIPAD_FLOWER.get());
                output.accept(CNBItemModule.YELLOW_MINIPAD_FLOWER_GLOW.get());

                output.accept(CNBItemModule.FLOWER_CROWN.get());
                output.accept(CNBItemModule.GLOWING_FLOWER_CROWN.get());
                output.accept(CNBItemModule.SPORELING_BACKPACK.get());

                output.accept(CNBItemModule.CINDER_SWORD.get());
                output.accept(CNBItemModule.CACTEM_SPEAR.get());

                output.accept(CNBItemModule.HEAL_SPELL_BOOK_1.get());
                output.accept(CNBItemModule.HEAL_SPELL_BOOK_2.get());
                output.accept(CNBItemModule.HEAL_SPELL_BOOK_3.get());

                output.accept(CNBItemModule.CINDERSHELL_FURNACE.get());

                output.accept(CNBItemModule.CINDERSHELL_SHELL_SHARD.get());
                output.accept(CNBItemModule.YETI_ANTLER.get());
                output.accept(CNBItemModule.YETI_HIDE.get());

                output.accept(CNBItemModule.ENTITY_NET.get());

                output.accept(CNBItemModule.CINDERSHELL_BUCKET.get());
                output.accept(CNBItemModule.LIZARD_EGG.get());

                output.accept(CNBItemModule.LIZARD_ITEM_DESERT.get());
                output.accept(CNBItemModule.LIZARD_ITEM_DESERT_2.get());
                output.accept(CNBItemModule.LIZARD_ITEM_JUNGLE.get());
                output.accept(CNBItemModule.LIZARD_ITEM_JUNGLE_2.get());
                output.accept(CNBItemModule.LIZARD_ITEM_MUSHROOM.get());

                output.accept(CNBItemModule.GREBE_SPAWN_EGG.get());
                output.accept(CNBItemModule.CINDERSHELL_SPAWN_EGG.get());
                output.accept(CNBItemModule.LILYTAD_SPAWN_EGG.get());
                output.accept(CNBItemModule.YETI_SPAWN_EGG.get());
                output.accept(CNBItemModule.MINIPAD_SPAWN_EGG.get());
                output.accept(CNBItemModule.LIZARD_SPAWN_EGG.get());
                output.accept(CNBItemModule.END_WHALE_SPAWN_EGG.get());
                output.accept(CNBItemModule.CACTEM_SPAWN_EGG.get());
                output.accept(CNBItemModule.SPORELING_OVERWORLD_EGG.get());
                output.accept(CNBItemModule.SPORELING_NETHER_EGG.get());
            })
            .build());

    // Called in the mod initializer / constructor in order to make sure that items are registered
    public static void load() {}
}
