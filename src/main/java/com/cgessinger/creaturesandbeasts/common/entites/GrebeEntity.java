package com.cgessinger.creaturesandbeasts.common.entites;

import javax.annotation.Nullable;

import com.cgessinger.creaturesandbeasts.common.goals.GoToWaterGoal;
import com.cgessinger.creaturesandbeasts.common.goals.MountAdultGoal;
import com.cgessinger.creaturesandbeasts.common.goals.SmoothSwimGoal;

import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class GrebeEntity extends AnimalEntity
{
	private static final DataParameter<BlockPos> TRAVEL_POS = EntityDataManager.createKey(GrebeEntity.class, DataSerializers.BLOCK_POS);
	public static final Ingredient TEMPTATION_ITEMS = Ingredient.fromItems(Items.COD, Items.SALMON, Items.TROPICAL_FISH);
	public float wingRotation;
	public float destPos;
	public float oFlapSpeed;
	public float oFlap;
	public float wingRotDelta = 1.0F;

	public GrebeEntity (EntityType<? extends AnimalEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.setPathPriority(PathNodeType.WATER, 10.0F);
	}

	static public AttributeModifierMap.MutableAttribute setCustomAttributes ()
	{
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 10.0D) // Max Health
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D); // Movement Speed
	}

	@Override
	public ILivingEntityData onInitialSpawn (IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		if (spawnDataIn == null)
		{
			spawnDataIn = new AgeableEntity.AgeableData(0.6F);
		}

		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	protected void registerGoals ()
	{
		this.goalSelector.addGoal(1, new MountAdultGoal(this, 1.2D));
		this.goalSelector.addGoal(2, new SmoothSwimGoal(this));
		this.goalSelector.addGoal(3, new PanicGoal(this, 1.0D));
		this.goalSelector.addGoal(3, new GrebeEntity.SwimTravelGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new GrebeEntity.WanderGoal(this, 1.0D, 2));
		this.goalSelector.addGoal(5, new TemptGoal(this, 1.0D, false, TEMPTATION_ITEMS));
		this.goalSelector.addGoal(5, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(8, new GoToWaterGoal(this, 0.8D));
	}

	@Override
	protected void collideWithNearbyEntities ()
	{
		super.collideWithNearbyEntities();
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
		return ModEntityTypes.LITTLE_GREBE.get().create(p_241840_1_);
	}

	@Override
	public boolean onLivingFall (float distance, float damageMultiplier)
	{
		return false;
	}

	@Override
	protected float getSoundVolume ()
	{
		return 0.6F;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound ()
	{
		if(this.isChild())
		{
			return ModSoundEventTypes.LITTLE_GREBE_CHICK_AMBIENT.get();
		}
		return ModSoundEventTypes.LITTLE_GREBE_AMBIENT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound (DamageSource damageSourceIn)
	{
		return ModSoundEventTypes.LITTLE_GREBE_HURT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound ()
	{
		return ModSoundEventTypes.LITTLE_GREBE_HURT.get();
	}

	@Override
	public double getMountedYOffset ()
	{
		return this.getHeight() * 0.3D;
	}

	@Override
	protected void onGrowingAdult ()
	{
		this.stopRiding();
	}

	/**
	 * Rewrite of the original @applyEntityCollision with code cleanup and ability
	 * to be pushed when mounted
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

	@Override
	public boolean isPushedByWater ()
	{
		return false;
	}

	private BlockPos getTravelPos ()
	{
		return this.dataManager.get(TRAVEL_POS);
	}

	@Override
	protected void registerData ()
	{
		super.registerData();
		this.dataManager.register(TRAVEL_POS, new BlockPos(0, 2, 0));
	}

	@Override
	public boolean isBreedingItem (ItemStack stack)
	{
		return TEMPTATION_ITEMS.test(stack);
	}

	@Override
	public void travel (Vector3d travelVector)
	{
		if (this.isServerWorld() && this.isInWater())
		{
			this.moveRelative(0.1F, travelVector);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale(0.5D));
			if (this.getAttackTarget() == null)
			{
				this.setMotion(this.getMotion().add(0.0D, -0.005D, 0.0D));
			}
		} else
		{
			super.travel(travelVector);
		}

	}

	static class WanderGoal extends RandomWalkingGoal
	{
		private WanderGoal (GrebeEntity entity, double speedIn, int chance)
		{
			super(entity, speedIn, chance);
		}

		@Override
		public boolean shouldExecute ()
		{
			return !this.creature.isInWater() && super.shouldExecute();
		}
	}

	static class SwimTravelGoal extends Goal
	{
		private final GrebeEntity turtle;
		private final double speed;
		private boolean field_203139_c;

		SwimTravelGoal (GrebeEntity turtle, double speedIn)
		{
			this.turtle = turtle;
			this.speed = speedIn;
		}

		@Override
		public boolean shouldExecute ()
		{
			return this.turtle.isInWater();
		}

		@Override
		public void startExecuting ()
		{
			this.field_203139_c = false;
		}

		@Override
		public void tick ()
		{
			if (this.turtle.getNavigator().noPath())
			{
				Vector3d vector3d = Vector3d.copyCenteredHorizontally(this.turtle.getTravelPos());
				Vector3d vector3d1 = RandomPositionGenerator.findRandomTargetTowardsScaled(this.turtle, 16, 3, vector3d, ((float) Math.PI / 10F));
				if (vector3d1 == null)
				{
					vector3d1 = RandomPositionGenerator.findRandomTargetBlockTowards(this.turtle, 8, 7, vector3d);
				}

				if (vector3d1 != null)
				{
					int i = MathHelper.floor(vector3d1.x);
					int j = MathHelper.floor(vector3d1.z);
					if (!this.turtle.world.isAreaLoaded(i - 34, 0, j - 34, i + 34, 0, j + 34))
					{
						vector3d1 = null;
					}
				}

				if (vector3d1 == null)
				{
					this.field_203139_c = true;
					return;
				}

				this.turtle.getNavigator().tryMoveToXYZ(vector3d1.x, vector3d1.y, vector3d1.z, this.speed);
			}

		}

		@Override
		public boolean shouldContinueExecuting ()
		{
			return !this.turtle.getNavigator().noPath() && !this.field_203139_c && !this.turtle.isInLove();
		}
	}
}
