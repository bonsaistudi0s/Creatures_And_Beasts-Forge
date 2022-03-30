package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.common.goals.FindWaterOneDeepGoal;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.init.ModItems;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class LilytadEntity extends Animal implements IForgeShearable, IAnimatable
{
	private final AnimationFactory factory = new AnimationFactory(this);
	private static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(LilytadEntity.class, EntityDataSerializers.BOOLEAN);
	private int shearedTimer;

	public LilytadEntity (EntityType<? extends Animal> type, Level worldIn)
	{
		super(type, worldIn);
		this.shearedTimer = 0;
		this.lookControl = new LookControl(this){
			@Override
			public void tick ()
			{
				LilytadEntity lilytad = (LilytadEntity)this.mob;
				if(lilytad.shouldLookAround())
				{
					super.tick();
				}
			}
		};
	}

	@Override
	protected void defineSynchedData ()
	{
		this.entityData.define(SHEARED, false);
		super.defineSynchedData();
	}

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event)
    {
        event.add(ModEntityTypes.LILYTAD.get(), Attributes.MAX_HEALTH, 20.0D);
        event.add(ModEntityTypes.LILYTAD.get(), Attributes.MOVEMENT_SPEED, 0.2D);
    }

	@Override
	public void readAdditionalSaveData (CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		this.shearedTimer = compound.getInt("ShearedTimer");
		this.setSheared(this.shearedTimer > 0);
	}

	@Override
	public void addAdditionalSaveData (CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("ShearedTimer", this.shearedTimer);
	}

	@Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(1, new FindWaterOneDeepGoal(this));
		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D){
			@Override
			public boolean canUse ()
			{
				return !this.mob.level.getFluidState(this.mob.blockPosition()).is(FluidTags.WATER) && super.canUse();
			}

			@Override
			public boolean canContinueToUse ()
			{
				return !this.mob.level.getFluidState(this.mob.blockPosition()).is(FluidTags.WATER) && super.canContinueToUse();
			}
		});
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(2, new PanicGoal(this, 1.25D));
	}

	@Override
	public void aiStep ()
	{
		super.aiStep();
		if(!this.level.isClientSide() && this.shearedTimer > 0)
		{
			this.setSheared(--this.shearedTimer > 0);
		}
	}

	@Override
	protected void pushEntities()
	{
		List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate(0.2, 0, 0.2), EntitySelector.pushableBy(this));
		if (!list.isEmpty())
		{
			int i = this.level.getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
			if (i > 0 && list.size() > i - 1 && this.random.nextInt(4) == 0)
			{
				int j = 0;

				for (Entity entity : list)
				{
					if (!entity.isPassenger())
					{
						++j;
					}
				}

				if (j > i - 1) {
					this.hurt(DamageSource.CRAMMING, 6.0F);
				}
			}

			for (Entity entity : list)
			{
				this.doPush(entity);
			}
		}

	}

	@Override
	public boolean canBeCollidedWith() {
		return this.isAlive();
	}

	@Override
	public boolean canBreatheUnderwater ()
	{
		return true;
	}

	@Override
	public boolean isPushedByFluid ()
	{
		return false;
	}

	private <E extends IAnimatable> PlayState animationPredicate (AnimationEvent<E> event)
	{
		if (!(animationSpeed > -0.15F && animationSpeed < 0.15F))
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("lilytad.walk", true));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring (ServerLevel p_241840_1_, AgeableMob p_241840_2_)
	{
		return null;
	}

	public boolean getSheared ()
	{
		return this.entityData.get(SHEARED);
	}

	@Override
	public boolean isShearable (@Nonnull ItemStack item, Level world, BlockPos pos)
	{
		return !this.getSheared();
	}

	public void setSheared (boolean sheared)
	{
		this.entityData.set(SHEARED, sheared);
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared (@Nullable Player player, @Nonnull ItemStack item, Level world, BlockPos pos, int fortune)
	{
		if (!world.isClientSide)
		{
			this.setSheared(true);
			this.shearedTimer = 15 * 60 * 20; // 15 min x 60 sec x 20 ticks per sec
			java.util.List<ItemStack> items = new java.util.ArrayList<>();
			items.add(new ItemStack(ModItems.LILYTAD_FLOWER.get()));

			return items;
		}
		return java.util.Collections.emptyList();
	}

	public boolean shouldLookAround()
	{
		return !this.level.getFluidState(this.blockPosition()).is(FluidTags.WATER);
	}

	@Override
	public void registerControllers (AnimationData animationData)
	{
		animationData.addAnimationController(new AnimationController<LilytadEntity>(this, "controller", 0, this::animationPredicate));
	}

	@Override
	public AnimationFactory getFactory ()
	{
		return this.factory;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound (DamageSource damageSourceIn)
	{
		return ModSoundEventTypes.LILYTAD_HURT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound ()
	{
		return ModSoundEventTypes.LILYTAD_AMBIENT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound ()
	{
		return ModSoundEventTypes.LILYTAD_DEATH.get();
	}

    @Override
    public void checkDespawn() 
    {
        if(!CNBConfig.ServerConfig.LILYTAD_CONFIG.shouldExist)
        {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        super.checkDespawn();
    }

    public static boolean canLilytadSpawn( EntityType<LilytadEntity> animal, LevelAccessor worldIn,
                                             MobSpawnType reason, BlockPos pos, Random randomIn )
    {
        return true;
    }
}
