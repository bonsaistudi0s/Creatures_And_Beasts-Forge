package com.cgessinger.creaturesandbeasts.common.util;

import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModItems;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;
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

	public static void getItemAttributes (ItemAttributeModifierEvent event)
	{
		ItemStack input = event.getItemStack();
		if(input.hasTag() && input.getTag().contains("hide_amount"))
		{
			Collection<AttributeModifier> modOld = event.getOriginalModifiers().get(Attributes.ARMOR);
			int hideAmount = input.getTag().getInt("hide_amount");
			for(AttributeModifier modifier : modOld)
			{
				event.removeModifier(Attributes.ARMOR, modifier);
				AttributeModifier modNew = new AttributeModifier(modifier.getID(), modifier.getName(), modifier.getAmount() * Math.pow(1.03D, hideAmount), modifier.getOperation());
				event.addModifier(Attributes.ARMOR, modNew);
			}
		}
	}

	@SubscribeEvent
	public static void onAnvilChange (AnvilUpdateEvent event)
	{
		if(event.getLeft().getItem() instanceof ArmorItem && event.getRight().getItem() == ModItems.YETI_HIDE.get())
		{
			ItemStack output = event.getLeft().copy();
			CompoundNBT nbt = output.getOrCreateTag();
			int hideAmount = 1;

			if(nbt.contains("hide_amount"))
			{
				hideAmount += nbt.getInt("hide_amount");
			}
			
			nbt.putInt("hide_amount", hideAmount);
			event.setCost(1);
			event.setMaterialCost(1);
			event.setOutput(output);
		}
	}
}
