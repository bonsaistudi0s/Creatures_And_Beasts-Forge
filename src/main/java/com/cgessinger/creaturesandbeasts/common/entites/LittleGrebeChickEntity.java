package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.goals.MountAdultGoal;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Objects;

public class LittleGrebeChickEntity extends AbstractGrebeEntity
{
	public LittleGrebeChickEntity (EntityType<? extends AnimalEntity> type, World worldIn)
	{
		super(type, worldIn);
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes()
	{
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 5.0D) // Max Health
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.3D); // Movement Speed
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new MountAdultGoal(this, 1));
	}

	@Override
	protected void onGrowingAdult ()
	{
		if(!this.isChild())
		{
			Vector3d pos = this.getPositionVec();
			if(this.isPassenger())
			{
				this.stopRiding();
			}
			this.remove();
			if(this.world instanceof ServerWorld)
			{
				ServerWorld serverworld = (ServerWorld) this.world;
				LittleGrebeEntity entity = Objects.requireNonNull(ModEntityTypes.LITTLE_GREBE.get().create(serverworld));
				entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
				serverworld.summonEntity(entity);
			}
		}
	}

	@Override
	public ILivingEntityData onInitialSpawn (IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		ILivingEntityData data = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
		this.setGrowingAge(-800);
		return data;
	}
}
