package com.cgessinger.creaturesandbeasts.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.level.Level;


public class FindWaterOneDeepGoal extends TryFindWaterGoal {
    private final PathfinderMob creature;

    public FindWaterOneDeepGoal(PathfinderMob creature) {
        super(creature);
        this.creature = creature;
    }

    @Override
    public void start() {
        int fromX = Mth.floor(this.creature.getX() - 10.0D);
        int fromY = Mth.floor(this.creature.getY() - 3.0D);
        int fromZ = Mth.floor(this.creature.getZ() - 10.0D);

        int toX = Mth.floor(this.creature.getX() + 10.0D);
        int toY = Mth.floor(this.creature.getY());
        int toZ = Mth.floor(this.creature.getZ() + 10.0D);

        Iterable<BlockPos> allBlocks = BlockPos.randomBetweenClosed(this.creature.getRandom(), 50, fromX, fromY, fromZ, toX, toY, toZ);

        for (BlockPos blockpos1 : allBlocks) {
            Level world = this.creature.level;

            if (world.getFluidState(blockpos1).is(FluidTags.WATER) && world.getBlockState(blockpos1.below()).canOcclude() && world.getBlockState(blockpos1.above()).isAir()) {
                this.creature.getNavigation().moveTo(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(), 1.0D);
                break;
            }
        }
    }
}
