package com.cgessinger.creaturesandbeasts.common.entites;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public abstract class AbstractGrebeEntity extends AnimalEntity
{
	public static final Ingredient TEMPTATION_ITEMS = Ingredient.fromItems(Items.COD, Items.SALMON, Items.TROPICAL_FISH);
	public float wingRotation;
	public float destPos;
	public float oFlapSpeed;
	public float oFlap;
	public float wingRotDelta = 1.0F;

	public AbstractGrebeEntity (EntityType<? extends AnimalEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.setPathPriority(PathNodeType.WATER, 10.0F);
	}

	static public AttributeModifierMap.MutableAttribute setCustomAttributes ()
	{
		return MobEntity.func_233666_p_();
	}

	@Override
	protected void registerGoals ()
	{
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new RandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(2, new PanicGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, false, TEMPTATION_ITEMS));
		this.goalSelector.addGoal(4, new FindWaterGoal(this));
		this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1.0D, 120));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
	}

	@Override
	public void livingTick ()
	{
		super.livingTick();
		this.oFlap = this.wingRotation;
		this.oFlapSpeed = this.destPos;
		this.destPos = (float) ((double) this.destPos + (double) (this.onGround || this.isInWater() || this.isPassenger() ? -1 : 4) * 0.3D);
		this.destPos = MathHelper.clamp(this.destPos, 0.0F, 1.0F);
		if (!this.onGround && this.wingRotDelta < 1.0F && !this.isInWater() && !this.isPassenger())
		{
			this.wingRotDelta = 1.0F;
		}

		this.wingRotDelta *= 0.9F;
		Vector3d motion = this.getMotion();
		if (!this.onGround && motion.y < 0.0D)
		{
			this.setMotion(motion.mul(1.0D, 0.6D, 1.0D));
		}

		this.wingRotation += this.wingRotDelta * 2.0F;
	}

	@Nullable
	@Override
	public AgeableEntity func_241840_a (ServerWorld p_241840_1_, AgeableEntity p_241840_2_)
	{
		return null;
	}

	@Override
	public boolean onLivingFall (float distance, float damageMultiplier)
	{
		return false;
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_CHICKEN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_CHICKEN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_CHICKEN_DEATH;
	}

	/**
	 * Rewrite of the original @applyEntityCollision with code cleanup and ability to be pushed when mounted
	 */
	@Override
	public void applyEntityCollision (Entity entityIn)
	{
		if (!this.isRidingSameEntity(entityIn))
		{
			if (!entityIn.noClip && !this.noClip)
			{
				double d0 = entityIn.getPosX() - this.getPosX();
				double d1 = entityIn.getPosZ() - this.getPosZ();
				double d2 = MathHelper.absMax(d0, d1);
				if (d2 >= 0.01D)
				{
					d2 = MathHelper.sqrt(d2);
					double d3 = 1.0D / d2;
					if (d3 > 1.0D)
					{
						d3 = 1.0D;
					}

					d0 = d0 / d2 * d3 * 0.05D - this.entityCollisionReduction;
					d1 = d1 / d2 * d3 * 0.05D - this.entityCollisionReduction;
					this.addVelocity(-d0, 0.0D, -d1);

					if (!entityIn.isBeingRidden())
					{
						entityIn.addVelocity(d0, 0.0D, d1);
					}
				}

			}
		}
	}
}
