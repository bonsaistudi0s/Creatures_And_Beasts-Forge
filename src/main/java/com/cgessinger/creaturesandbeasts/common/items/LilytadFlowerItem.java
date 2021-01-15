package com.cgessinger.creaturesandbeasts.common.items;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.init.ModBlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.world.World;

public class LilytadFlowerItem extends BlockItem
{
	public LilytadFlowerItem ()
	{
		super(ModBlockRegistry.LILYTAD_FLOWER.get(),
				new Item.Properties()
				.group(CreaturesAndBeasts.TAB)
				.food(new Food.Builder()
						.hunger(4)
						.saturation(0.5F)
						.setAlwaysEdible()
						.build())
		);
	}

	@Override
	public ItemStack onItemUseFinish (ItemStack stack, World worldIn, LivingEntity entityLiving)
	{
		entityLiving.heal(4);
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	@Override
	protected boolean canPlace (BlockItemUseContext p_195944_1_, BlockState p_195944_2_)
	{
		return false;
	}
}
