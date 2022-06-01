package com.cgessinger.creaturesandbeasts.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HealSpellBookItem extends Item {

    public HealSpellBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int useDuration) {
        super.releaseUsing(stack, level, entity, useDuration);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 7200;
    }
}
