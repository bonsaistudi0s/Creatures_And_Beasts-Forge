package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
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

import javax.annotation.Nullable;

public abstract class AbstractSporelingEntity extends CreatureEntity implements IAnimatable
{
	private static final DataParameter<Integer> SPORELING_VARIANT = EntityDataManager.createKey(AbstractSporelingEntity.class, DataSerializers.VARINT);
	private final AnimationFactory factory = new AnimationFactory(this);

	protected AbstractSporelingEntity (EntityType<? extends CreatureEntity> type, World worldIn)
	{
		super(type, worldIn);
		this.dataManager.register(SPORELING_VARIANT, 0);
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

	public <E extends IAnimatable> PlayState animationPredicate (AnimationEvent<E> event)
	{
		if (!(limbSwingAmount > -0.15F && limbSwingAmount < 0.15F))
		{
			event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.walk", true));
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

	public static AttributeModifierMap.MutableAttribute setCustomAttributes ()
	{
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 10.0D) // Max Health
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2D); // Movement Speed
	}

	@Override
	protected void registerGoals ()
	{
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
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

	public enum SporelingType{
		OVERWORLD_BROWN(createLocation("geo/overworld_brown_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_brown_overworld.png")),
		OVERWORLD_RED(createLocation("geo/overworld_red_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_red_overworld.png")),
		NETHER_BROWN(createLocation("geo/nether_brown_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_brown_nether.png")),
		NETHER_RED(createLocation("geo/nether_red_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_red_nether.png")),
		WARPED_FUNGI(createLocation("geo/warped_fungi_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_warped_fungi.png")),
		CRIMSON_FUNGUS(createLocation("geo/warped_fungi_sporeling.geo.json"), createLocation("textures/model/entity/sporeling_crimson_fungus.png"));

		public final ResourceLocation modelLocation;
		public final ResourceLocation textureLocation;
		SporelingType(ResourceLocation ml, ResourceLocation tl){
			this.modelLocation = ml;
			this.textureLocation = tl;
		}

		private static ResourceLocation createLocation(String pathPart){
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
