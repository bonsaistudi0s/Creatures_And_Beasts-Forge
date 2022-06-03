package com.cgessinger.creaturesandbeasts.events;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.config.CNBConfig.ServerConfig;
import com.cgessinger.creaturesandbeasts.entities.CactemEntity;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import com.cgessinger.creaturesandbeasts.entities.EndWhaleEntity;
import com.cgessinger.creaturesandbeasts.entities.LilytadEntity;
import com.cgessinger.creaturesandbeasts.entities.LittleGrebeEntity;
import com.cgessinger.creaturesandbeasts.entities.LizardEntity;
import com.cgessinger.creaturesandbeasts.entities.MinipadEntity;
import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import com.cgessinger.creaturesandbeasts.entities.YetiEntity;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBItems;
import com.cgessinger.creaturesandbeasts.init.CNBLootModifiers;
import com.cgessinger.creaturesandbeasts.items.HealSpellBookItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

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
        event.put(CNBEntityTypes.MINIPAD.get(), MinipadEntity.createAttributes().build());
        event.put(CNBEntityTypes.END_WHALE.get(), EndWhaleEntity.createAttributes().build());
        event.put(CNBEntityTypes.CACTEM.get(), CactemEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onRegisterLootModifiers(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(new CNBLootModifiers.NetherBridgeLootSerializer().setRegistryName(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "nether_bridge_loot_modifier")));
    }

	@SubscribeEvent
	public void onItemAttributeModifierCalculate(ItemAttributeModifierEvent event) {
		ItemStack input = event.getItemStack();
        CompoundTag tag = input.getTag();
        EquipmentSlot equipmentSlot = null;
        if (input.getItem() instanceof ArmorItem) {
            ArmorItem armorItem = (ArmorItem) input.getItem();
            equipmentSlot = armorItem.getSlot();
        }

		if (equipmentSlot != null && tag != null && event.getSlotType().equals(equipmentSlot) && tag.contains("HideAmount")) {
            int hideAmount = tag.getInt("HideAmount");

            if (equipmentSlot.equals(EquipmentSlot.HEAD)) {
                event.addModifier(Attributes.ARMOR, new AttributeModifier(UUID.fromString("96a6b318-81f1-475a-b4a4-b3da41d2711e"), "yeti_hide", ServerConfig.HIDE_AMOUNT.value * hideAmount, AttributeModifier.Operation.MULTIPLY_TOTAL));
            } else if (equipmentSlot.equals(EquipmentSlot.CHEST)) {
                event.addModifier(Attributes.ARMOR, new AttributeModifier(UUID.fromString("3f3136ff-4f04-4d62-a9cc-8d1f4175c1e2"), "yeti_hide", ServerConfig.HIDE_AMOUNT.value * hideAmount, AttributeModifier.Operation.MULTIPLY_TOTAL));
            } else if (equipmentSlot.equals(EquipmentSlot.LEGS)) {
                event.addModifier(Attributes.ARMOR, new AttributeModifier(UUID.fromString("f49d078c-2740-4283-8255-5d1f106efea0"), "yeti_hide", ServerConfig.HIDE_AMOUNT.value * hideAmount, AttributeModifier.Operation.MULTIPLY_TOTAL));
            } else {
                event.addModifier(Attributes.ARMOR, new AttributeModifier(UUID.fromString("b16e7c3f-508d-461d-8868-de6ee2a1314c"), "yeti_hide", ServerConfig.HIDE_AMOUNT.value * hideAmount, AttributeModifier.Operation.MULTIPLY_TOTAL));
            }
		}
	}

    @SubscribeEvent
    public void onAnvilChange(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof ArmorItem && event.getRight().is(CNBItems.YETI_HIDE.get())) {
            ItemStack output = event.getLeft().copy();
            CompoundTag nbt = output.getOrCreateTag();
            int hideAmount = 1;

            if (nbt.contains("HideAmount")) {
                hideAmount += nbt.getInt("HideAmount");

                if (hideAmount > ServerConfig.HIDE_AMOUNT.value) {
                    return;
                }
            }

            nbt.putInt("HideAmount", hideAmount);
            event.setCost(ServerConfig.HIDE_COST.value);
            event.setMaterialCost(1);
            event.setOutput(output);
        } else if (event.getLeft().getItem() instanceof HealSpellBookItem && event.getRight().getItem() instanceof HealSpellBookItem && event.getLeft().sameItem(event.getRight())) {
            ItemStack output;
            int cost;
            if (event.getLeft().is(CNBItems.HEAL_SPELL_BOOK_1.get())) {
                output = new ItemStack(CNBItems.HEAL_SPELL_BOOK_2.get());
                cost = 3;
            } else if (event.getLeft().is(CNBItems.HEAL_SPELL_BOOK_2.get())) {
                output = new ItemStack(CNBItems.HEAL_SPELL_BOOK_3.get());
                cost = 6;
            } else {
                return;
            }

            output.setTag(event.getLeft().getOrCreateTag());
            event.setCost(cost);
            event.setOutput(output);
            event.setMaterialCost(1);
        }
    }
}
