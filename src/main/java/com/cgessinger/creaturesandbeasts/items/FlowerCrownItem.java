package com.cgessinger.creaturesandbeasts.items;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;

public class FlowerCrownItem extends GeoArmorItem implements IAnimatable {
    private final Ingredient repairItems;
    private final AnimationFactory factory = new AnimationFactory(this);

    public FlowerCrownItem(ArmorMaterial material, Ingredient repairItems, EquipmentSlot slot, Properties properties) {
        super(material, slot, properties);
        this.repairItems = repairItems;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stackInput, ItemStack repairStack) {
        return this.repairItems.test(repairStack);
    }

    @Override
    public void registerControllers(AnimationData data) {
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
