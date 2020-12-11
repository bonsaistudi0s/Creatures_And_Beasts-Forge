package com.cgessinger.creaturesandbeasts.common.entites;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nullable;

public class FriendlySporelingEntity extends AbstractSporelingEntity
{
	public boolean wave;
	private static final DataParameter<Boolean> WAVE = EntityDataManager.createKey(FriendlySporelingEntity.class,
			DataSerializers.BOOLEAN);

	public FriendlySporelingEntity (EntityType<? extends CreatureEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.wave = false;
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn (IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		this.setSporelingType(this.getRNG().nextInt(2));
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	protected void registerGoals()
	{
		super.registerGoals();
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
		this.goalSelector.addGoal(6, new WaveGoal(this, PlayerEntity.class, 6.0F));
	}

	@Override
	protected void registerData ()
	{
		super.registerData();
		this.dataManager.register(WAVE, false);
	}

	public void setWave (boolean wave)
	{
		this.dataManager.set(WAVE, wave);
	}

	@Override
	public <E extends IAnimatable> PlayState animationPredicate (AnimationEvent<E> event)
	{
		if (super.animationPredicate(event) == PlayState.STOP)
		{
			if (this.dataManager.get(WAVE))
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.wave", true));
				return PlayState.CONTINUE;
			}
			return PlayState.STOP;
		}
		return PlayState.CONTINUE;
	}

	static class WaveGoal extends LookAtGoal
	{
		private final FriendlySporelingEntity sporeling;
		private int waveTimer;

		public WaveGoal (FriendlySporelingEntity entityIn, Class<? extends LivingEntity> watchTargetClass, float maxDistance)
		{
			super(entityIn, watchTargetClass, maxDistance);
			sporeling = entityIn;
			this.waveTimer = 0;
		}

		@Override
		public boolean shouldExecute ()
		{
			boolean shouldExec = super.shouldExecute();
			if(shouldExec && this.waveTimer == 0)
			{
				this.waveTimer = 30;
			} else if (this.waveTimer > 0 && this.closestEntity != null)
			{
				this.sporeling.faceEntity(this.closestEntity, 30.0F, 30.0F);
				this.sporeling.setWave(--this.waveTimer > 0);
			}
			return shouldExec;
		}
	}
}
