package com.cgessinger.creaturesandbeasts.common.goals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.vector.Vector3d;

public class SmoothSwimGoal extends Goal
{
	private final CreatureEntity entity;

	public SmoothSwimGoal (CreatureEntity entityIn)
	{
		this.entity = entityIn;
		entityIn.getNavigator().setCanSwim(true);
	}

	@Override
	public boolean shouldExecute ()
	{
		return (this.entity.isInWater() && this.entity.func_233571_b_(FluidTags.WATER)+this.entity.getEyeHeight()/2.2 > this.entity.func_233579_cu_() || this.entity.isInLava());
	}

	@Override
	public void tick ()
	{
		this.entity.moveRelative(0.01F, new Vector3d(0, 1, 0));
	}
}
