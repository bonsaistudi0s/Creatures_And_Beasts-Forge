package com.cgessinger.creaturesandbeasts.items;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.init.CNBBlocks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LilytadFlowerItem extends BlockItem {
    public LilytadFlowerItem() {
        super(CNBBlocks.LILYTAD_FLOWER.get(), new Item.Properties().tab(CreaturesAndBeasts.TAB).food(new FoodProperties.Builder().nutrition(4).saturationMod(0.5F).alwaysEat().build()));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        entityLiving.heal(4);
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext p_195944_1_, BlockState p_195944_2_) {
        return false;
    }
}
