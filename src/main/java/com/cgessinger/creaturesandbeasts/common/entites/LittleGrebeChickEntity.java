package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.goals.MountAdultGoal;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib.entity.IAnimatedEntity;
import software.bernie.geckolib.manager.EntityAnimationManager;
import sun.security.ssl.Debug;

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
				.func_233815_a_(Attributes.field_233818_a_, 5.0D) // Max Health
				.func_233815_a_(Attributes.field_233821_d_, 0.35D); // Movement Speed
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new MountAdultGoal(this, 1.2D));
	}

	@Override
	protected void onGrowingAdult ()
	{
		if( this.getRidingEntity() instanceof LittleGrebeEntity)
		{
			Debug.println("little grebe chick", "is adult");
			Vector3d pos = this.getPositionVec();
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
}
