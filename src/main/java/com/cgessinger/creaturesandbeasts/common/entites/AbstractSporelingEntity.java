package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import com.cgessinger.creaturesandbeasts.common.interfaces.IRunningEntity;
import com.cgessinger.creaturesandbeasts.common.interfaces.ITimedAttackEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public abstract class AbstractSporelingEntity extends PathfinderMob implements IAnimatable, ITimedAttackEntity, IRunningEntity
{
	private static final EntityDataAccessor<Integer> SPORELING_VARIANT = SynchedEntityData.defineId(AbstractSporelingEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(AbstractSporelingEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> RUNNING = SynchedEntityData.defineId(AbstractSporelingEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<ItemStack> HOLDING = SynchedEntityData.defineId( FriendlySporelingEntity.class, EntityDataSerializers.ITEM_STACK );
	protected int attackTimer;
	private final AnimationFactory factory = new AnimationFactory(this);

	protected AbstractSporelingEntity (EntityType<? extends PathfinderMob> type, Level worldIn)
	{
		super(type, worldIn);
		this.entityData.define(SPORELING_VARIANT, 0);
		this.entityData.define(ATTACKING, false);
		this.entityData.define(RUNNING, false);
		this.entityData.define(HOLDING, ItemStack.EMPTY);
		this.attackTimer = 0;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn (ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag)
	{
		if (dataTag != null && dataTag.contains("variant"))
		{
			this.setSporelingType(dataTag.getInt("variant"));
		}
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	public void travel (Vec3 travelVector)
	{
		if(!this.level.isClientSide())
		{
			this.setRunning(this.getMoveControl().getSpeedModifier() >= this.getRunThreshold());
		}
		super.travel(travelVector);
	}

	public <E extends IAnimatable> PlayState animationPredicate (AnimationEvent<E> event)
	{
		if (!(animationSpeed > -0.15F && animationSpeed < 0.15F))
		{
			if(this.isRunning())
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("sporeling.run", true));
			}
			else
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("sporeling.walk", true));
			}
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	@Override
	protected BodyRotationControl createBodyControl ()
	{
		return super.createBodyControl();
	}

	@Override
	public void registerControllers (AnimationData animationData)
	{
		animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
	}

	@Override
	public AnimationFactory getFactory ()
	{
		return this.factory;
	}

	public SporelingType getSporelingType ()
	{
		return SporelingType.values()[this.entityData.get(SPORELING_VARIANT)];
	}

	public void setSporelingType (int variant)
	{
		this.entityData.set(SPORELING_VARIANT, variant);
	}

	@Override
	public void setAttacking (boolean attacking)
	{
		this.entityData.set(ATTACKING, attacking);
	}

	@Override
	public boolean isAttacking ()
	{
		return this.entityData.get(ATTACKING);
	}

	@Override
	public void setRunning (boolean running)
	{
		this.entityData.set(RUNNING, running);
	}

	@Override
	public boolean isRunning ()
	{
		return this.entityData.get(RUNNING);
	}

	public void setHolding (ItemStack stack)
	{
		this.entityData.set(HOLDING, stack);
	}

	public ItemStack getHolding ()
	{
		return this.entityData.get(HOLDING);
	}

	@Override
	public double getRunThreshold ()
	{
		return 1.3D;
	}

	@Override
	protected void registerGoals ()
	{
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
	}

	@Override
	public void addAdditionalSaveData (CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("variant", this.entityData.get(SPORELING_VARIANT));
	}

	@Override
	public void readAdditionalSaveData (CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		if (compound.contains("variant"))
		{
			setSporelingType(compound.getInt("variant"));
		}
	}

	@Override
	public boolean doHurtTarget (Entity entityIn)
	{
		this.playSound(ModSoundEventTypes.SPORELING_BITE.get(), this.getSoundVolume()*2, this.getVoicePitch());
		return super.doHurtTarget(entityIn);
	}

	@Override
	public int getMaxHeadYRot ()
	{
		return 5;
	}

    @Override
    public boolean removeWhenFarAway( double distanceToClosestPlayer )
    {
        return false;
    }

	public enum SporelingType
	{
		OVERWORLD_BROWN(createLocation("geo/overworld_brown_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_brown_overworld.png")), OVERWORLD_RED(createLocation("geo/overworld_red_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_red_overworld.png")), NETHER_BROWN(createLocation("geo/nether_brown_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_brown_nether.png")), NETHER_RED(createLocation("geo/nether_red_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_red_nether.png")), WARPED_FUNGI(createLocation("geo/warped_fungi_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_warped_fungi.png")), CRIMSON_FUNGUS(createLocation("geo/warped_fungi_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_crimson_fungus.png"));

		public final ResourceLocation modelLocation;
		public final ResourceLocation textureLocation;

		SporelingType (ResourceLocation ml, ResourceLocation tl)
		{
			this.modelLocation = ml;
			this.textureLocation = tl;
		}

		private static ResourceLocation createLocation (String pathPart)
		{
			return new ResourceLocation(CreaturesAndBeasts.MOD_ID, pathPart);
		}

		public ResourceLocation getModelLocation ()
		{
			return modelLocation;
		}

		public ResourceLocation getTextureLocation ()
		{
			return textureLocation;
		}
	}
}
