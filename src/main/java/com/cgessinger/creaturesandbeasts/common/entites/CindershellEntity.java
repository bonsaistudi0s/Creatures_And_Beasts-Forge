package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class CindershellEntity extends AnimalEntity
{
	public CindershellEntity (EntityType<? extends AnimalEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	@Override
	protected void registerGoals ()
	{
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
		this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
	}

	static public AttributeModifierMap.MutableAttribute setCustomAttributes ()
	{
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 80.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.15D)
				.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 100D);
	}

	@Override
	public float getEyeHeight (Pose pose)
	{
		return this.getHeight() * 0.2F;
	}

	/*
	 * This sets what blocks it can't spawn on and for monsters set it to not spawn
	 * in peaceful. You can always do light checks and time times here.
	 */
	public static boolean canCindershellSpawn(EntityType<CindershellEntity> p_234418_0_, IWorld p_234418_1_, SpawnReason p_234418_2_, BlockPos p_234418_3_, Random p_234418_4_) {
		return !p_234418_1_.getBlockState(p_234418_3_.down()).isIn(Blocks.NETHERRACK) || !p_234418_1_.getBlockState(p_234418_3_.down()).isIn(Blocks.BASALT)
				|| !p_234418_1_.getBlockState(p_234418_3_.down()).isIn(Blocks.WARPED_NYLIUM) || !p_234418_1_.getBlockState(p_234418_3_.down()).isIn(Blocks.CRIMSON_NYLIUM)
				|| !p_234418_1_.getBlockState(p_234418_3_.down()).isIn(Blocks.SOUL_SOIL) || !p_234418_1_.getBlockState(p_234418_3_.down()).isIn(Blocks.SOUL_SAND);
	   }

	@Nullable
	@Override
	public AgeableEntity func_241840_a (ServerWorld p_241840_1_, AgeableEntity p_241840_2_)
	{
		return null;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound ()
	{
		return ModSoundEventTypes.CINDERSHELL_AMBIENT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound (DamageSource damageSourceIn)
	{
		return ModSoundEventTypes.CINDERSHELL_HURT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getDeathSound ()
	{
		return ModSoundEventTypes.CINDERSHELL_HURT.get();
	}

	@Override
	protected float getSoundVolume ()
	{
		return super.getSoundVolume() * 2;
	}

	@Override
	public int getTalkInterval() {
		return 120;
	}

	@Override
	public boolean canDespawn(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}
}
