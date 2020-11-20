package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;

import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class LittleGrebeEntity extends AbstractGrebeEntity
{
	public LittleGrebeEntity (EntityType<? extends AnimalEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes ()
	{
		return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 10.0D) // Max Health
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D); // Movement Speed
	}

	@Override
	protected void registerGoals ()
	{
		super.registerGoals();
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LittleGrebeEntity.GoToWaterGoal(this, 0.8D));
	}

	@Nullable
	@Override
	public AgeableEntity func_241840_a (ServerWorld serverIn, AgeableEntity entityIn)
	{
		LittleGrebeChickEntity child = ModEntityTypes.LITTLE_GREBE_CHICK.get().create(serverIn);
		assert child != null;
		serverIn.summonEntity(child);
		child.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
		return child;
	}

	@Override
	public double getMountedYOffset ()
	{
		return this.getHeight() * 0.3D;
	}

	@Override
	public boolean isBreedingItem (ItemStack stack)
	{
		return TEMPTATION_ITEMS.test(stack);
	}

	@Override
	public boolean isChild ()
	{
		return false;
	}

	@Nullable
	@Override
	protected SoundEvent getAmbientSound ()
	{
		return ModSoundEventTypes.LITTLE_GREBE_AMBIENT.get();
	}

	@Nullable
	@Override
	protected SoundEvent getHurtSound (DamageSource damageSourceIn)
	{
		return ModSoundEventTypes.LITTLE_GREBE_HURT.get();
	}

	static class GoToWaterGoal extends MoveToBlockGoal
	{
		private final LittleGrebeEntity turtle;

		private GoToWaterGoal (LittleGrebeEntity turtle, double speedIn)
		{
			super(turtle, turtle.isChild() ? 2.0D : speedIn, 24);
			this.turtle = turtle;
			this.field_203112_e = -1;
		}

		@Override
		public boolean shouldContinueExecuting ()
		{
			return !this.turtle.isInWater() && this.timeoutCounter <= 1200 && this.shouldMoveTo(this.turtle.world, this.destinationBlock);
		}

		@Override
		public boolean shouldExecute ()
		{
			return !this.turtle.isInWater();
		}

		@Override
		public boolean shouldMove ()
		{
			return this.timeoutCounter % 160 == 0;
		}

		@Override
		protected boolean shouldMoveTo (IWorldReader worldIn, BlockPos pos)
		{
			return worldIn.getBlockState(pos).isIn(Blocks.WATER);
		}
	}
}
