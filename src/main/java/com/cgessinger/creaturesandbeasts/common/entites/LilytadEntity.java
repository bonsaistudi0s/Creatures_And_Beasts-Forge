package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.common.goals.FindWaterOneDeepGoal;
import com.cgessinger.creaturesandbeasts.common.init.ModItems;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IForgeShearable;
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

public class LilytadEntity extends AnimalEntity implements IForgeShearable, IAnimatable
{
	private final AnimationFactory factory = new AnimationFactory(this);
	private static final DataParameter<Boolean> SHEARED = EntityDataManager.createKey(LilytadEntity.class, DataSerializers.BOOLEAN);
	private int shearedTimer;

	public LilytadEntity (EntityType<? extends AnimalEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.shearedTimer = 0;
		this.lookController = new LookController(this){
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
	protected void registerData ()
	{
		this.dataManager.register(SHEARED, false);
		super.registerData();
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes ()
	{
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 20.0D) // Max Health
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2D); // Movement Speed
	}

	@Override
	public void readAdditional (CompoundNBT compound)
	{
		super.readAdditional(compound);
		this.shearedTimer = compound.getInt("ShearedTimer");
		this.setSheared(this.shearedTimer > 0);
	}

	@Override
	public void writeAdditional (CompoundNBT compound)
	{
		super.writeAdditional(compound);
		compound.putInt("ShearedTimer", this.shearedTimer);
	}

	@Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(1, new FindWaterOneDeepGoal(this));
		this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1.0D){
			@Override
			public boolean shouldExecute ()
			{
				return !this.creature.world.getFluidState(this.creature.getPosition()).isTagged(FluidTags.WATER) && super.shouldExecute();
			}

			@Override
			public boolean shouldContinueExecuting ()
			{
				return !this.creature.world.getFluidState(this.creature.getPosition()).isTagged(FluidTags.WATER) && super.shouldContinueExecuting();
			}
		});
		this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
		this.goalSelector.addGoal(2, new PanicGoal(this, 1.25D));
	}

	@Override
	public void livingTick ()
	{
		super.livingTick();
		if(!this.world.isRemote() && this.shearedTimer > 0)
		{
			this.setSheared(--this.shearedTimer > 0);
		}
	}

	@Override
	protected void collideWithNearbyEntities()
	{
		List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox().grow(0.2, 0, 0.2), EntityPredicates.pushableBy(this));
		if (!list.isEmpty())
		{
			int i = this.world.getGameRules().getInt(GameRules.MAX_ENTITY_CRAMMING);
			if (i > 0 && list.size() > i - 1 && this.rand.nextInt(4) == 0)
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
					this.attackEntityFrom(DamageSource.CRAMMING, 6.0F);
				}
			}

			for (Entity entity : list)
			{
				this.collideWithEntity(entity);
			}
		}

	}

	@Override
	public boolean func_241845_aY() {
		return this.isAlive();
	}

	@Override
	public boolean canBreatheUnderwater ()
	{
		return true;
	}

	@Override
	public boolean isPushedByWater ()
	{
		return false;
	}

	private <E extends IAnimatable> PlayState animationPredicate (AnimationEvent<E> event)
	{
		if (!(limbSwingAmount > -0.15F && limbSwingAmount < 0.15F))
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("lilytad.walk", true));
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	@Nullable
	@Override
	public AgeableEntity func_241840_a (ServerWorld p_241840_1_, AgeableEntity p_241840_2_)
	{
		return null;
	}

	public boolean getSheared ()
	{
		return this.dataManager.get(SHEARED);
	}

	@Override
	public boolean isShearable (@Nonnull ItemStack item, World world, BlockPos pos)
	{
		return !this.getSheared();
	}

	public void setSheared (boolean sheared)
	{
		this.dataManager.set(SHEARED, sheared);
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared (@Nullable PlayerEntity player, @Nonnull ItemStack item, World world, BlockPos pos, int fortune)
	{
		if (!world.isRemote)
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
		return !this.world.getFluidState(this.getPosition()).isTagged(FluidTags.WATER);
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
            this.remove();
            return;
        }
        super.checkDespawn();
    }

    public static boolean canLilytadSpawn( EntityType<LilytadEntity> animal, IWorld worldIn,
                                             SpawnReason reason, BlockPos pos, Random randomIn )
    {
        return true;
    }
}
