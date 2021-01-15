package com.cgessinger.creaturesandbeasts.common.util;

import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class ModEventHandler
{
	@SubscribeEvent
	public static void onBlockActivate (PlayerInteractEvent.RightClickBlock event)
	{
			TileEntity te = event.getWorld().getTileEntity(event.getPos());
			if (te instanceof JukeboxTileEntity)
			{
				Item heldItem = event.getPlayer().getHeldItem(event.getHand()).getItem();
				if (heldItem instanceof MusicDiscItem)
				{
					List<LizardEntity> lizards = event.getWorld().getEntitiesWithinAABB(LizardEntity.class, event.getPlayer().getBoundingBox().grow(15));
					for (LizardEntity lizard : lizards)
					{
						lizard.setPartying(true, event.getPos());
					}
				}
			}
	}

	@SubscribeEvent
	public static void onAnvilChange (AnvilUpdateEvent event)
	{
	}
}
