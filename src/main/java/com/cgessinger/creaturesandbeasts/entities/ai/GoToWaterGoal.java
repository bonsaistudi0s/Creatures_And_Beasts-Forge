package com.cgessinger.creaturesandbeasts.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;

// ToDo: Rework This Goal (Look at FindWaterOneDeepGoal)
public class GoToWaterGoal extends MoveToBlockGoal {
    private final Animal creature;

    public GoToWaterGoal(Animal creature, double speedIn) {
        super(creature, creature.isBaby() ? 2.0D : speedIn, 24);
        this.creature = creature;
        this.verticalSearchStart = -1;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.creature.isInWater() && this.tryTicks <= 1200 && this.isValidTarget(this.creature.level, this.blockPos);
    }

    @Override
    public boolean canUse() {
        return !this.creature.isInWater() && !this.creature.isInLove() && this.creature.getRandom().nextInt(200) == 0;
    }

    @Override
    public boolean shouldRecalculatePath() {
        return this.tryTicks % 160 == 0;
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).is(Blocks.WATER);
    }
}
