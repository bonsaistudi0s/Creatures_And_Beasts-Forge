package com.cgessinger.creaturesandbeasts.common.util;

import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.tileentity.TileEntity;
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
			Item heldItem = event.getPlayer().getHeldItem(event.getHand()).getItem();
			if (te instanceof JukeboxTileEntity)
			{
				JukeboxTileEntity box = (JukeboxTileEntity) te;
				boolean discOut = box.getRecord() != ItemStack.EMPTY;
				boolean discIn = heldItem instanceof MusicDiscItem;
				if (discOut || discIn)
				{
					List<LizardEntity> lizards = event.getWorld().getEntitiesWithinAABB(LizardEntity.class, event.getPlayer().getBoundingBox().grow(15));
					for (LizardEntity lizard : lizards)
					{
						lizard.setPartying(!discOut);
						/* If discOut is false, disIn must be true. If dicOut is true, discIn will not be checked anymore. So !discOut is enough */
					}
				}
			}
	}
}
