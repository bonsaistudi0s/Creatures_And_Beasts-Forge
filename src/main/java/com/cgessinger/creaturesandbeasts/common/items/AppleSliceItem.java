package com.cgessinger.creaturesandbeasts.common.items;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import net.minecraft.item.Food;
import net.minecraft.item.Item;

public class AppleSliceItem extends Item
{
	public AppleSliceItem ()
	{
		super(new Item.Properties()
				.group(CreaturesAndBeasts.TAB)
				.food(new Food.Builder()
						.hunger(1)
						.saturation(0.3F)
						.build())
		);
	}
}
