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
				.maxDamage(64)
		);
	}

	@Override
	public ActionResultType itemInteractionForEntity (ItemStack stackIn, PlayerEntity playerIn, LivingEntity target, Hand hand)
	{
		if(target instanceof IModNetable && target.isAlive())
		{
			IModNetable entity = (IModNetable) target;
			ItemStack entityItem = entity.getItem();
			if(entityItem != null)
			{
				playerIn.addItemStackToInventory(entityItem);
				entity.spawnParticleFeedback();
				target.remove();
				if (!playerIn.world.isRemote()) {
					stackIn.damageItem(1, playerIn, (p_220000_1_) -> {
						p_220000_1_.sendBreakAnimation(hand);
					});
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.FAIL;
	}

	@Override
	public int getItemEnchantability() {
		return 1;
	}
}
