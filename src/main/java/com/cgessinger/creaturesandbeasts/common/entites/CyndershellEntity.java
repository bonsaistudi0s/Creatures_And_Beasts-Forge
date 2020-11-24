package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class CyndershellEntity extends AnimalEntity
{
	public CyndershellEntity (EntityType<? extends AnimalEntity> type, World worldIn)
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
		return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 80.0D) // Max Health
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.15D); // Movement Speed
	}

	@Override
	public float getEyeHeight (Pose pose)
	{
		return this.getHeight() * 0.2F;
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
		return ModSoundEventTypes.CYNDERSHELL_AMBIENT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound (DamageSource damageSourceIn)
	{
		return ModSoundEventTypes.CYNDERSHELL_HURT.get();
	}

	@Override
	protected float getSoundVolume ()
	{
		return super.getSoundVolume() * 2;
	}

	public static boolean canAnimalSpawn (EntityType<? extends AnimalEntity> p_234361_0_, IWorld worldIn, SpawnReason p_234361_2_, BlockPos pos, Random p_234361_4_)
	{
		System.out.println("Cyndershell spawn try -+- decision: " + worldIn.getBlockState(pos.down()).isSolid());
		System.out.println("Position: " + pos);

		BlockPos.Mutable blockpos$mutable = pos.toMutable();
		do
		{
			blockpos$mutable.move(Direction.DOWN);
		} while (worldIn.getBlockState(blockpos$mutable).isAir());

		return !worldIn.getFluidState(blockpos$mutable).isTagged(FluidTags.LAVA);
	}
}