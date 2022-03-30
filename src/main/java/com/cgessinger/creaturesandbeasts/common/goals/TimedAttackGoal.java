package com.cgessinger.creaturesandbeasts.common.goals;

import com.cgessinger.creaturesandbeasts.common.interfaces.ITimedAttackEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.Difficulty;

public class TimedAttackGoal<E extends PathfinderMob & ITimedAttackEntity> extends MeleeAttackGoal
{
    protected int animationTime;
    protected E entity;

	public TimedAttackGoal (E attacker, double speedIn, boolean useLongMemory, int animationTime)
	{
		super(attacker, speedIn, useLongMemory);
        this.animationTime = animationTime;
        this.entity = attacker;
	}

	@Override
	protected void checkAndPerformAttack (LivingEntity enemy, double distToEnemySqr)
	{
		double d0 = this.getAttackReachSqr(enemy);
		if (entity.level.getDifficulty() != Difficulty.PEACEFUL &&  distToEnemySqr <= d0 && getTicksUntilNextAttack() <= 0)
		{
            this.resetAttackCooldown();
			entity.setAttacking(true);
            this.mob.doHurtTarget(enemy);
		}
	}

    protected void resetAttackCooldown () 
    {
        this.ticksUntilNextAttack = animationTime;
    }

    @Override
    public void stop() 
    {
        super.stop();
		this.entity.setAttacking(false);
    }
}
