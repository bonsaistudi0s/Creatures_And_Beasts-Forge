package com.cgessinger.creaturesandbeasts.items;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class GlowingFlowerCrownItem extends FlowerCrownItem {
    public GlowingFlowerCrownItem(ArmorMaterial material, Ingredient repairItems, EquipmentSlot slot, Properties properties) {
        super(material, repairItems, slot, properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
