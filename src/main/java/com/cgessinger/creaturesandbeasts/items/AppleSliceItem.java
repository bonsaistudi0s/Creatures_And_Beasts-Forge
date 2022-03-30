package com.cgessinger.creaturesandbeasts.items;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class AppleSliceItem extends Item {
    public AppleSliceItem() {
        super(new Item.Properties().tab(CreaturesAndBeasts.TAB).food(new FoodProperties.Builder().nutrition(1).saturationMod(0.3F).build()));
    }
}
