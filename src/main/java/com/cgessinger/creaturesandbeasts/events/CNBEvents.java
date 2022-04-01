package com.cgessinger.creaturesandbeasts.events;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.config.CNBConfig.ServerConfig;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import com.cgessinger.creaturesandbeasts.entities.LilytadEntity;
import com.cgessinger.creaturesandbeasts.entities.LittleGrebeEntity;
import com.cgessinger.creaturesandbeasts.entities.LizardEntity;
import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import com.cgessinger.creaturesandbeasts.entities.YetiEntity;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBItems;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = CreaturesAndBeasts.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CNBEvents {

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(CNBEntityTypes.CINDERSHELL.get(), CindershellEntity.createAttributes().build());
        event.put(CNBEntityTypes.SPORELING.get(), SporelingEntity.createAttributes().build());
        event.put(CNBEntityTypes.LITTLE_GREBE.get(), LittleGrebeEntity.createAttributes().build());
        event.put(CNBEntityTypes.LILYTAD.get(), LilytadEntity.createAttributes().build());
        event.put(CNBEntityTypes.LIZARD.get(), LizardEntity.createAttributes().build());
        event.put(CNBEntityTypes.YETI.get(), YetiEntity.createAttributes().build());
    }

    @SubscribeEvent
    public void onBlockActivate(PlayerInteractEvent.RightClickBlock event) {
        BlockEntity te = event.getWorld().getBlockEntity(event.getPos());
        if (te instanceof JukeboxBlockEntity) {
            Item heldItem = event.getPlayer().getItemInHand(event.getHand()).getItem();
            if (heldItem instanceof RecordItem) {
                List<LizardEntity> lizards = event.getWorld().getEntitiesOfClass(LizardEntity.class, event.getPlayer().getBoundingBox().inflate(15));
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
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.level.isClientSide()) {
            return;
        }

        Inventory inv = event.player.getInventory();
        Stream<ItemStack> playerItems = Stream.concat(inv.armor.stream(), inv.items.stream());
        Stream<ItemStack> conatinerItems = event.player.containerMenu.getItems().stream();

        Stream.concat(playerItems, conatinerItems).forEach(stack -> {
            if (stack.getItem() instanceof ArmorItem) {
                checkAndUpdateItemArmor(stack);
            }
        });

    }

    private void checkAndUpdateItemArmor(ItemStack input) {
        if (input.hasTag() && input.getTag().contains("hide_amount")) {
            if (input.getTag().contains("AttributeModifiers", 9)) {
                input.removeTagKey("AttributeModifiers");
            }

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                Multimap<Attribute, AttributeModifier> modOld = input.getItem().getAttributeModifiers(slot, input);
                int hideAmount = input.getTag().getInt("hide_amount");

                for (Entry<Attribute, AttributeModifier> entry : modOld.entries()) {
                    AttributeModifier modifier = entry.getValue();
                    if (entry.getKey().equals(Attributes.ARMOR)) {
                        modifier = new AttributeModifier(modifier.getId(), modifier.getName(), modifier.getAmount() * Math.pow(ServerConfig.HIDE_MULTIPLIER.value, hideAmount), modifier.getOperation());
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
    public void onAnvilChange(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof ArmorItem && event.getRight().getItem() == CNBItems.YETI_HIDE.get()) {
            ItemStack output = event.getLeft().copy();
            CompoundTag nbt = output.getOrCreateTag();
            int hideAmount = 1;

            if (nbt.contains("hide_amount")) {
                hideAmount += nbt.getInt("hide_amount");

                if (hideAmount > ServerConfig.HIDE_AMOUNT.value) {
                    return;
                }
            }

            nbt.putInt("hide_amount", hideAmount);
            event.setCost(ServerConfig.HIDE_COST.value);
            event.setMaterialCost(1);
            event.setOutput(output);
        }
    }
}
