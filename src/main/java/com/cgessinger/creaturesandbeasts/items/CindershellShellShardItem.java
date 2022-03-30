package com.cgessinger.creaturesandbeasts.items;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class CindershellShellShardItem extends Item {
    public CindershellShellShardItem() {
        super(new Item.Properties().tab(CreaturesAndBeasts.TAB));
    }

    @Override
    public int getBurnTime(ItemStack itemStack, RecipeType<?> recipeType) {
        return 6400;
    }
}
