package com.cgessinger.creaturesandbeasts.items;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.item.Item.Properties;

public class WaterlilyBlockItem extends BlockItem {
    public WaterlilyBlockItem(Block block, Properties properties) {
        super(block, properties.tab(CreaturesAndBeasts.TAB));
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        return false;
    }
}
