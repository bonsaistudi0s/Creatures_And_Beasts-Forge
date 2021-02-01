package com.cgessinger.creaturesandbeasts.common.util;

import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModItems;
import com.google.common.collect.Multimap;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;

@Mod.EventBusSubscriber
public class ModEventHandler {
	@SubscribeEvent
	public static void onBlockActivate(PlayerInteractEvent.RightClickBlock event) {
		TileEntity te = event.getWorld().getTileEntity(event.getPos());
		if (te instanceof JukeboxTileEntity) {
			Item heldItem = event.getPlayer().getHeldItem(event.getHand()).getItem();
			if (heldItem instanceof MusicDiscItem) {
				List<LizardEntity> lizards = event.getWorld().getEntitiesWithinAABB(LizardEntity.class,
						event.getPlayer().getBoundingBox().grow(15));
				for (LizardEntity lizard : lizards) {
					lizard.setPartying(true, event.getPos());
				}
			}
		}
	}

	/*
	* Using this event for the hide upgrades, beacuse it gets fired for every ItemStack indiviudally. Let me know if you know a better one
	* (Needed for Netherite items to update attributes when upgraded in smithing table)
	*/
	@SubscribeEvent
	public static void onPlayerTick (TickEvent.PlayerTickEvent event)
	{
		if(event.player.world.isRemote())
			return;

		PlayerInventory inv = event.player.inventory;
		Stream<ItemStack> playerItems = Stream.concat(inv.armorInventory.stream(), inv.mainInventory.stream());
		Stream<ItemStack> conatinerItems = Stream.empty();
		if(event.player.openContainer != null)
			conatinerItems = event.player.openContainer.getInventory().stream();

		Stream.concat(playerItems, conatinerItems).forEach(stack -> {
			if(stack.getItem() instanceof ArmorItem)
			{
				checkAndUpdateItemArmor(stack);
			}
		});
		
	}

	private static void checkAndUpdateItemArmor (ItemStack input)
	{
		if(input.hasTag() && input.getTag().contains("hide_amount"))
		{			
			if(input.getTag().contains("AttributeModifiers", 9))
				input.removeChildTag("AttributeModifiers");
			
			for(EquipmentSlotType slot : EquipmentSlotType.values())
			{
				Multimap<Attribute, AttributeModifier> modOld = input.getItem().getAttributeModifiers(slot, input);
				int hideAmount = input.getTag().getInt("hide_amount");
				
				for(Entry<Attribute, AttributeModifier> entry : modOld.entries())
				{
					AttributeModifier modifier = entry.getValue();
					if(entry.getKey().equals(Attributes.ARMOR))
					{
						modifier = new AttributeModifier(modifier.getID(), modifier.getName(), modifier.getAmount() * Math.pow(1.01D, hideAmount), modifier.getOperation());
					}
					input.addAttributeModifier(entry.getKey(), modifier, slot);
				}
			}			
		}
	}

	/*
	* I'll leave that in here as a better version for the hide reinforcements. Cannot use it to make it compatible with 1.16.3 :(
	*
	@SubscribeEvent
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
	*/

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

				if(hideAmount > 5)
					return;
			}
			
			nbt.putInt("hide_amount", hideAmount);
			event.setCost(1);
			event.setMaterialCost(1);
			event.setOutput(output);
		}
	}
}
