package com.cgessinger.creaturesandbeasts.common.goals;

import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import sun.security.ssl.Debug;

public class MountAdultGoal extends Goal
{
	private static final EntityPredicate entityPredicate = (new EntityPredicate()).setDistance(6.0D).allowFriendlyFire().allowInvulnerable();
	private final AnimalEntity childAnimal;
	private final double moveSpeed;
	private final World world;

	public MountAdultGoal(AnimalEntity child, double speed)
	{
		this.childAnimal = child;
		this.moveSpeed = speed;
		this.world = child.getEntityWorld();
	}

	@Override
	public boolean shouldExecute ()
	{
		return !this.childAnimal.isPassenger();
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void tick() {
		Vector3d pos = this.childAnimal.getPositionVec();
		LivingEntity closestEntity = world.getClosestEntityWithinAABB(LittleGrebeEntity.class, entityPredicate, this.childAnimal, pos.getX(), pos.getY(), pos.getZ(), this.childAnimal.getBoundingBox().grow(20.0D, 6.0D, 20.0D));
		if(closestEntity != null && !closestEntity.isBeingRidden())
		{
			this.childAnimal.getNavigator().tryMoveToEntityLiving(closestEntity, this.moveSpeed);
			double dist = this.childAnimal.getDistance(closestEntity);
			if(dist <= 0.6)
			{
				this.childAnimal.startRiding(closestEntity);
			}
			else if (dist <= 1.5)
			{
				this.childAnimal.getLookController().setLookPosition(closestEntity.getPositionVec());
				this.childAnimal.moveRelative(0.2F, this.childAnimal.getLookVec());
			}
		}
	}

}
