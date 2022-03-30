package com.cgessinger.creaturesandbeasts.common.items;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CindershellShellShardItem extends Item
{
	public CindershellShellShardItem ()
	{
		super(new Item.Properties().tab(CreaturesAndBeasts.TAB));
	}

	@Override
	public int getBurnTime (ItemStack itemStack)
	{
		return 6400;
	}
}
