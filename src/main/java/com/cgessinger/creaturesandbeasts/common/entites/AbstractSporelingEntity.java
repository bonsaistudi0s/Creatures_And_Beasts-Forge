package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.interfaces.IRunningEntity;
import com.cgessinger.creaturesandbeasts.common.interfaces.ITimedAttackEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public abstract class AbstractSporelingEntity extends CreatureEntity implements IAnimatable, ITimedAttackEntity, IRunningEntity
{
	private static final DataParameter<Integer> SPORELING_VARIANT = EntityDataManager.createKey(AbstractSporelingEntity.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> ATTACKING = EntityDataManager.createKey(AbstractSporelingEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> RUNNING = EntityDataManager.createKey(AbstractSporelingEntity.class, DataSerializers.BOOLEAN);
	protected int attackTimer;
	private final AnimationFactory factory = new AnimationFactory(this);

	protected AbstractSporelingEntity (EntityType<? extends CreatureEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.dataManager.register(SPORELING_VARIANT, 0);
		this.dataManager.register(ATTACKING, false);
		this.dataManager.register(RUNNING, false);
		this.attackTimer = 0;
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn (IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		if (dataTag != null && dataTag.contains("variant"))
		{
			this.setSporelingType(dataTag.getInt("variant"));
		}
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	public void travel (Vector3d travelVector)
	{
		if(!this.world.isRemote())
		{
			this.setRunning(this.getMoveHelper().getSpeed() >= this.getRunThreshold());
		}
		super.travel(travelVector);
	}

	public <E extends IAnimatable> PlayState animationPredicate (AnimationEvent<E> event)
	{
		if (!(limbSwingAmount > -0.15F && limbSwingAmount < 0.15F))
		{
			if(this.isRunning())
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("sporeling.run", true));
			}
			else
			{
				event.getController().setAnimation(new AnimationBuilder().addAnimation("sporeling.walk", true));
			}
			return PlayState.CONTINUE;
		}
		return PlayState.STOP;
	}

	@Override
	public void registerControllers (AnimationData animationData)
	{
		animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
	}

	@Override
	public AnimationFactory getFactory ()
	{
		return this.factory;
	}

	public SporelingType getSporelingType ()
	{
		return SporelingType.values()[this.dataManager.get(SPORELING_VARIANT)];
	}

	public void setSporelingType (int variant)
	{
		this.dataManager.set(SPORELING_VARIANT, variant);
	}

	@Override
	public void setAttacking (boolean attacking)
	{
		this.dataManager.set(ATTACKING, attacking);
	}

	@Override
	public boolean isAttacking ()
	{
		return this.dataManager.get(ATTACKING);
	}

	@Override
	public void setRunning (boolean running)
	{
		this.dataManager.set(RUNNING, running);
	}

	@Override
	public boolean isRunning ()
	{
		return this.dataManager.get(RUNNING);
	}

	@Override
	public double getRunThreshold ()
	{
		return 1.3D;
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes ()
	{
		return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 16.0D) // Max Health
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2D); // Movement Speed
	}

	@Override
	protected void registerGoals ()
	{
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
	}

	@Override
	public void writeAdditional (CompoundNBT compound)
	{
		super.writeAdditional(compound);
		compound.putInt("variant", this.dataManager.get(SPORELING_VARIANT));
	}

	@Override
	public void readAdditional (CompoundNBT compound)
	{
		super.readAdditional(compound);
		if (compound.contains("variant"))
		{
			setSporelingType(compound.getInt("variant"));
		}
	}

	public enum SporelingType
	{
		OVERWORLD_BROWN(createLocation("geo/overworld_brown_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_brown_overworld.png")), OVERWORLD_RED(createLocation("geo/overworld_red_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_red_overworld.png")), NETHER_BROWN(createLocation("geo/nether_brown_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_brown_nether.png")), NETHER_RED(createLocation("geo/nether_red_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_red_nether.png")), WARPED_FUNGI(createLocation("geo/warped_fungi_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_warped_fungi.png")), CRIMSON_FUNGUS(createLocation("geo/warped_fungi_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_crimson_fungus.png"));

		public final ResourceLocation modelLocation;
		public final ResourceLocation textureLocation;

		SporelingType (ResourceLocation ml, ResourceLocation tl)
		{
			this.modelLocation = ml;
			this.textureLocation = tl;
		}

		private static ResourceLocation createLocation (String pathPart)
		{
			return new ResourceLocation(CreaturesAndBeasts.MOD_ID, pathPart);
		}

		public ResourceLocation getModelLocation ()
		{
			return modelLocation;
		}

		public ResourceLocation getTextureLocation ()
		{
			return textureLocation;
		}
	}
}
