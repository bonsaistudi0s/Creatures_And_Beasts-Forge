package com.cgessinger.creaturesandbeasts.util;

import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface IModNetable {
    @Nullable
    ItemStack getItem();

    void spawnParticleFeedback();
}
