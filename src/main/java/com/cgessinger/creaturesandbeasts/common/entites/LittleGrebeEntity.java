package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class LittleGrebeEntity extends AbstractGrebeEntity
{
	public LittleGrebeEntity (EntityType<? extends AnimalEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes()
	{
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 10.0D) // Max Health
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D); // Movement Speed
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
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
}
