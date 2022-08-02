package com.cgessinger.creaturesandbeasts.items;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;

import net.minecraft.world.item.Item.Properties;

public class GlowingFlowerCrownItem extends FlowerCrownItem {
    public GlowingFlowerCrownItem(ArmorMaterial material, Ingredient repairItems, EquipmentSlot slot, Properties properties) {
        super(material, repairItems, slot, properties);
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
