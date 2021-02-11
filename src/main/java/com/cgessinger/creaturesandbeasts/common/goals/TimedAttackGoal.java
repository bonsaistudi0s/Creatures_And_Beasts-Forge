package com.cgessinger.creaturesandbeasts.common.goals;

import com.cgessinger.creaturesandbeasts.common.interfaces.ITimedAttackEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.Difficulty;

public class TimedAttackGoal<E extends CreatureEntity & ITimedAttackEntity> extends MeleeAttackGoal
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
		if (entity.world.getDifficulty() != Difficulty.PEACEFUL &&  distToEnemySqr <= d0 && func_234041_j_() <= 0)
		{
            this.func_234039_g_();
			entity.setAttacking(true);
            this.attacker.attackEntityAsMob(enemy);
		}
	}

    protected void func_234039_g_ () 
    {
        this.field_234037_i_ = animationTime;
    }

    @Override
    public void resetTask() 
    {
        super.resetTask();
		this.entity.setAttacking(false);
    }
}
