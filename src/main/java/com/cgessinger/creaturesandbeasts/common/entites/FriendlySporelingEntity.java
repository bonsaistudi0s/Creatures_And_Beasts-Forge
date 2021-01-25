package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nullable;
import java.util.Random;

public class FriendlySporelingEntity extends AbstractSporelingEntity
{
	private static final DataParameter<Boolean> WAVE = EntityDataManager.createKey(FriendlySporelingEntity.class,
			DataSerializers.BOOLEAN);

	public FriendlySporelingEntity (EntityType<? extends CreatureEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	@Override
	public void livingTick ()
	{
		super.livingTick();

		if(this.dataManager.get(WAVE))
		{
			this.navigator.clearPath();
			this.getNavigator().setSpeed(0);
		}
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
		this.goalSelector.addGoal(6, new WaveGoal(this, PlayerEntity.class, 8.0F));
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
				event.getController().setAnimation(new AnimationBuilder().addAnimation("sporeling.wave", false));
				return PlayState.CONTINUE;
			}
			return PlayState.STOP;
		}
		return PlayState.CONTINUE;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound (DamageSource damageSourceIn)
	{
		return ModSoundEventTypes.SPORELING_OVERWORLD_HURT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound ()
	{
		return ModSoundEventTypes.SPORELING_OVERWORLD_HURT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound ()
	{
		return ModSoundEventTypes.SPORELING_OVERWORLD_AMBIENT.get();
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
			if(shouldExec && this.waveTimer == 0 && this.sporeling.getRNG().nextInt(9) == 0)
			{
				this.waveTimer = 8;
			} else if (this.waveTimer > 0 && this.closestEntity != null)
			{
				this.sporeling.setWave(--this.waveTimer > 0);
			}
			return shouldExec;
		}

		@Override
		public boolean shouldContinueExecuting ()
		{
			return super.shouldContinueExecuting() && this.sporeling.getLookController().getIsLooking();
		}
	}

	public static boolean canSporelingSpawn(EntityType<FriendlySporelingEntity> p_234418_0_, IWorld worldIn, SpawnReason p_234418_2_, BlockPos pos, Random p_234418_4_)
	{
		return (worldIn.getBlockState(pos.down()).isIn(Blocks.MYCELIUM) || worldIn.getBlockState(pos.down()).isIn(Blocks.GRASS_BLOCK)) &&
				worldIn.getLightSubtracted(pos, 0) > 8;
	}
}
