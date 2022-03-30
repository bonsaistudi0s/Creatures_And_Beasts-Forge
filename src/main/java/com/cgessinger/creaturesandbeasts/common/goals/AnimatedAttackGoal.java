package com.cgessinger.creaturesandbeasts.common.goals;

import com.cgessinger.creaturesandbeasts.common.interfaces.IAnimationHolder;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.Difficulty;
import software.bernie.geckolib3.core.IAnimatable;

public class AnimatedAttackGoal<E extends PathfinderMob & IAnimatable & IAnimationHolder<E>> extends MeleeAttackGoal 
{
    private final E entity;

    public AnimatedAttackGoal(E entity, double speedIn, boolean useLongMemory) 
    {
        super(entity, speedIn, useLongMemory);
        this.entity = entity;
    }

    @Override
    public boolean canUse() 
    {
        return !this.entity.isBaby() && super.canUse();
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) 
    {
        double d0 = this.getAttackReachSqr(enemy);
        boolean isInWater = this.entity.level.getFluidState(this.entity.blockPosition().below()).isSource();
        if (!isInWater && this.entity.level.getDifficulty() != Difficulty.PEACEFUL &&  distToEnemySqr <= d0 && getTicksUntilNextAttack() <= 0 && this.entity.getAnimationHandler("attack_controller").canStart())
        {
            this.entity.getAnimationHandler("attack_controller").startAnimation();
            this.resetAttackCooldown();
        }            
    }
}
