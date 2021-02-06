package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.common.goals.TimedAttackGoal;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class HostileSporelingEntity extends AbstractSporelingEntity implements IMob
{
	public HostileSporelingEntity (EntityType<? extends CreatureEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	@Override
	protected void registerGoals ()
	{
		super.registerGoals();
		this.goalSelector.addGoal(2, new TimedAttackGoal<>(this, 1.3D, false, 3));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes ()
	{
		return AbstractSporelingEntity.setCustomAttributes()
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 35.0D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D);
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn (IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		this.setSporelingType(this.getRNG().nextInt(2) + 2);
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	public <E extends IAnimatable> PlayState animationPredicate (AnimationEvent<E> event)
	{
		if(this.isAttacking())
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("sporeling.bite"));
			return PlayState.CONTINUE;
		}
		return super.animationPredicate(event);
	}

	public static boolean canSporelingSpawn(EntityType<HostileSporelingEntity> p_234418_0_, IWorld worldIn, SpawnReason p_234418_2_, BlockPos p_234418_3_, Random p_234418_4_)
	{
		Optional<RegistryKey<Biome>> optional = worldIn.func_241828_r().getRegistry(Registry.BIOME_KEY).getOptionalKey(worldIn.getBiome(p_234418_3_));

		return worldIn.getDifficulty() != Difficulty.PEACEFUL && (optional.isPresent() && optional.get() == Biomes.NETHER_WASTES);
	}

	@Override
	protected boolean isDespawnPeaceful ()
	{
		return true;
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound (DamageSource damageSourceIn)
	{
		return ModSoundEventTypes.SPORELING_NETHER_HURT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound ()
	{
		return ModSoundEventTypes.SPORELING_NETHER_AMBIENT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound ()
	{
		return ModSoundEventTypes.SPORELING_NETHER_HURT.get();
	}

    @Override
    public void checkDespawn() 
    {
        if(!CNBConfig.ServerConfig.HOSTILE_SPORELING_CONFIG.shouldExist)
        {
            this.remove();
            return;
        }
        super.checkDespawn();
    }
}
