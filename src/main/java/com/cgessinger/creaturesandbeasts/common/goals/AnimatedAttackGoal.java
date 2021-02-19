package com.cgessinger.creaturesandbeasts.common.goals;

import com.cgessinger.creaturesandbeasts.common.interfaces.IAnimationHolder;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.Difficulty;
import software.bernie.geckolib3.core.IAnimatable;

public class AnimatedAttackGoal<E extends CreatureEntity & IAnimatable & IAnimationHolder<E>> extends MeleeAttackGoal 
{
    private final E entity;

    public AnimatedAttackGoal(E entity, double speedIn, boolean useLongMemory) 
    {
        super(entity, speedIn, useLongMemory);
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute() 
    {
        return !this.entity.isChild() && super.shouldExecute();
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) 
    {
        double d0 = this.getAttackReachSqr(enemy);
        boolean isInWater = this.entity.world.getFluidState(this.entity.getPosition().down()).isSource();
        if (!isInWater && this.entity.world.getDifficulty() != Difficulty.PEACEFUL &&  distToEnemySqr <= d0 && func_234041_j_() <= 0 && this.entity.getAnimationHandler().canStart())
        {
            this.entity.getAnimationHandler().startAnimation();
            this.func_234039_g_();
        }            
    }
}
