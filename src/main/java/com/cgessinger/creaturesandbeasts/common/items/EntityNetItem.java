package com.cgessinger.creaturesandbeasts.common.items;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.interfaces.IModNetable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;

public class EntityNetItem extends Item
{
	public EntityNetItem ()
	{
		super(new Item.Properties()
				.tab(CreaturesAndBeasts.TAB)
				.durability(64)
		);
	}

	@Override
	public InteractionResult interactLivingEntity (ItemStack stackIn, Player playerIn, LivingEntity target, InteractionHand hand)
	{
		if(target instanceof IModNetable && target.isAlive())
		{
			IModNetable entity = (IModNetable) target;
			ItemStack entityItem = entity.getItem();
			if(entityItem != null)
			{
				playerIn.addItem(entityItem);
				entity.spawnParticleFeedback();
				target.remove();
				if (!playerIn.level.isClientSide()) {
					stackIn.hurtAndBreak(1, playerIn, (p_220000_1_) -> {
						p_220000_1_.broadcastBreakEvent(hand);
					});
				}
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.FAIL;
	}

	@Override
	public int getEnchantmentValue() {
		return 1;
	}
}
