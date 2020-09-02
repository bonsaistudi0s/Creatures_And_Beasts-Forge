package com.cgessinger.creaturesandbeasts.common.entites;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib.entity.IAnimatedEntity;
import software.bernie.geckolib.manager.EntityAnimationManager;

import javax.annotation.Nullable;

public class LittleGrebeChickEntity extends AnimalEntity implements IAnimatedEntity
{
	EntityAnimationManager manager = new EntityAnimationManager();

	public LittleGrebeChickEntity (EntityType<? extends AnimalEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes()
	{
		return MobEntity.func_233666_p_()
				.func_233815_a_(Attributes.field_233818_a_, 10.0D) // Max Health
				.func_233815_a_(Attributes.field_233821_d_, 0.25D); // Movement Speed
	}

	@Nullable
	@Override
	public AgeableEntity func_241840_a (ServerWorld p_241840_1_, AgeableEntity p_241840_2_)
	{
		return null;
	}

	@Override
	public EntityAnimationManager getAnimationManager ()
	{
		return manager;
	}
}
