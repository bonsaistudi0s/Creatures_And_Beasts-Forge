package com.cgessinger.creaturesandbeasts.common.interfaces;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public interface IModNetable
{
	@Nullable
	ItemStack getItem();
	void spawnParticleFeedback();
}
