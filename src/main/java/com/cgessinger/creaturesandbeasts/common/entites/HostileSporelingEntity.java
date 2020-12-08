package com.cgessinger.creaturesandbeasts.common.entites;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class HostileSporelingEntity extends AbstractSporelingEntity
{
	public HostileSporelingEntity (EntityType<? extends CreatureEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	@Override
	protected void registerGoals ()
	{
		super.registerGoals();
		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
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

	public static boolean canSporelingSpawn(EntityType<HostileSporelingEntity> p_234418_0_, IWorld worldIn, SpawnReason p_234418_2_, BlockPos p_234418_3_, Random p_234418_4_)
	{
		Optional<RegistryKey<Biome>> optional = worldIn.func_241828_r().getRegistry(Registry.BIOME_KEY).getOptionalKey(worldIn.getBiome(p_234418_3_));

		return worldIn.getDifficulty() != Difficulty.PEACEFUL && (optional.isPresent() && optional.get() == Biomes.NETHER_WASTES);
	}
}
