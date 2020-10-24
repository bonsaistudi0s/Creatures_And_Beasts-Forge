package com.cgessinger.creaturesandbeasts.common.items;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.interfaces.IModNetable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

public class EntityNetItem extends Item
{
	public EntityNetItem ()
	{
		super(new Item.Properties()
				.group(CreaturesAndBeasts.TAB)
		);
	}

	@Override
	public ActionResultType itemInteractionForEntity (ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand)
	{
		if(target instanceof IModNetable && target.isAlive())
		{
			IModNetable entity = (IModNetable) target;
			ItemStack itemstack = entity.getItem();
			if(itemstack != null)
			{
				playerIn.addItemStackToInventory(itemstack);
				entity.spawnParticleFeedback();
				target.remove();
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.FAIL;
	}
}
