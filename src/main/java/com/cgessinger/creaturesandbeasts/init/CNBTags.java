package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CNBTags {
    public static class Items {
        public static final TagKey<Item> MINIPAD_FLOWERS = tag("minipad_flowers");
        public static final TagKey<Item> GLOWING_MINIPAD_FLOWERS = tag("glowing_minipad_flowers");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(CreaturesAndBeasts.MOD_ID, name));
        }
    }

}
