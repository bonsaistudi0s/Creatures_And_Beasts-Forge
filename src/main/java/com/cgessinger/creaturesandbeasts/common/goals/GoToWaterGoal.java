package com.cgessinger.creaturesandbeasts.common.goals;

import net.minecraft.block.Blocks;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public class GoToWaterGoal extends MoveToBlockGoal
{
	private final AnimalEntity creature;

	public GoToWaterGoal (AnimalEntity creature, double speedIn)
	{
		super(creature, creature.isChild() ? 2.0D : speedIn, 24);
		this.creature = creature;
		this.field_203112_e = -1;
	}

	@Override
	public boolean shouldContinueExecuting ()
	{
		return !this.creature.isInWater() && this.timeoutCounter <= 1200 && this.shouldMoveTo(this.creature.world, this.destinationBlock);
	}

	@Override
	public boolean shouldExecute ()
	{
		return !this.creature.isInWater() && !this.creature.isInLove() && this.creature.getRNG().nextInt(200) == 0;
	}

	@Override
	public boolean shouldMove ()
	{
		return this.timeoutCounter % 160 == 0;
	}

	@Override
	protected boolean shouldMoveTo (IWorldReader worldIn, BlockPos pos)
	{
		return worldIn.getBlockState(pos).isIn(Blocks.WATER);
	}
}
