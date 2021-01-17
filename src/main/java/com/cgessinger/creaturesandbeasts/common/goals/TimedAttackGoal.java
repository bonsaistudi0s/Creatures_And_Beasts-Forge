package com.cgessinger.creaturesandbeasts.common.goals;

import com.cgessinger.creaturesandbeasts.common.interfaces.ITimedAttackEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.Difficulty;

public class TimedAttackGoal<E extends CreatureEntity & ITimedAttackEntity> extends MeleeAttackGoal
{
	private int attackTimer;
	private final E attacker;
	private final int animationTime;

	public TimedAttackGoal (E attacker, double speedIn, boolean useLongMemory, int animationTime)
	{
		super(attacker, speedIn, useLongMemory);
		this.attackTimer = 0;
		this.attacker = attacker;
		this.animationTime = animationTime;
	}

	@Override
	public boolean shouldExecute ()
	{
		if (this.attackTimer > 0)
		{
			this.attacker.setAttacking(--this.attackTimer > 0);
		}
		return super.shouldExecute();
	}

	@Override
	protected void checkAndPerformAttack (LivingEntity enemy, double distToEnemySqr)
	{
		this.attacker.setAttacking(false);
		double d0 = this.getAttackReachSqr(enemy);
		if (this.attacker.world.getDifficulty() != Difficulty.PEACEFUL &&  distToEnemySqr <= d0 && func_234041_j_() <= 0 && this.attackTimer <= 0)
		{
			this.attackTimer = this.animationTime;
			this.attacker.setAttacking(this.attackTimer > 0);
			super.checkAndPerformAttack(enemy, distToEnemySqr);
		}
	}
}
