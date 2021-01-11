package com.cgessinger.creaturesandbeasts.common.goals;

import com.cgessinger.creaturesandbeasts.common.entites.GrebeEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.EntityPredicates;

import java.util.List;

public class MountAdultGoal extends Goal
{
	private final AnimalEntity childAnimal;
	private final double moveSpeed;

	public MountAdultGoal(AnimalEntity child, double speed)
	{
		this.childAnimal = child;
		this.moveSpeed = speed;
	}

	@Override
	public boolean shouldExecute ()
	{
		if(!this.childAnimal.isPassenger() && this.childAnimal.isChild())
		{
			List<GrebeEntity> entities = this.childAnimal.world.getEntitiesWithinAABB(GrebeEntity.class, this.childAnimal.getBoundingBox().grow(10, 3, 10));

			for(GrebeEntity entity : entities)
			{
				if(!entity.isChild() && !entity.isBeingRidden())
				{
					this.childAnimal.getNavigator().setPath(this.childAnimal.getNavigator().getPathToEntity(entity, 0), this.moveSpeed);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void tick()
	{
		List<GrebeEntity> list = this.childAnimal.world.getEntitiesWithinAABB(GrebeEntity.class, this.childAnimal.getBoundingBox());

		for(GrebeEntity grebe : list)
		{
			if(!grebe.equals(this.childAnimal) && !grebe.isChild() && !grebe.isBeingRidden())
			{
				this.childAnimal.startRiding(grebe);
			}
		}
	}
}
