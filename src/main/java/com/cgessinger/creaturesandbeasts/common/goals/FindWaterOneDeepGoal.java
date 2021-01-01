package com.cgessinger.creaturesandbeasts.common.goals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.FindWaterGoal;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


public class FindWaterOneDeepGoal extends FindWaterGoal
{
	private final CreatureEntity creature;

	public FindWaterOneDeepGoal (CreatureEntity creature)
	{
		super(creature);
		this.creature = creature;
	}

	@Override
	public void startExecuting ()
	{
		int fromX = MathHelper.floor(this.creature.getPosX() - 10.0D);
		int fromY = MathHelper.floor(this.creature.getPosY() - 3.0D);
		int fromZ = MathHelper.floor(this.creature.getPosZ() - 10.0D);

		int toX = MathHelper.floor(this.creature.getPosX() + 10.0D);
		int toY = MathHelper.floor(this.creature.getPosY());
		int toZ = MathHelper.floor(this.creature.getPosZ() + 10.0D);

		Iterable<BlockPos> allBlocks = BlockPos.getRandomPositions(this.creature.getRNG(), 50, fromX, fromY, fromZ, toX, toY, toZ);

		for (BlockPos blockpos1 : allBlocks)
		{
			World world = this.creature.world;

			if (world.getFluidState(blockpos1).isTagged(FluidTags.WATER) && world.getBlockState(blockpos1.down()).isSolid() && world.getBlockState(blockpos1.up()).isAir())
			{
				this.creature.getNavigator().tryMoveToXYZ(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(), 1.0D);
				break;
			}
		}
	}
}
