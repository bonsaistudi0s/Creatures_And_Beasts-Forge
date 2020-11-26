package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.init.ModItems;
import com.cgessinger.creaturesandbeasts.common.interfaces.IModNetable;
import com.cgessinger.creaturesandbeasts.common.items.AppleSliceItem;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.animation.builder.AnimationBuilder;
import software.bernie.geckolib.animation.controller.EntityAnimationController;
import software.bernie.geckolib.entity.IAnimatedEntity;
import software.bernie.geckolib.event.AnimationTestEvent;
import software.bernie.geckolib.manager.EntityAnimationManager;

import javax.annotation.Nullable;
import java.util.List;

public class LizardEntity extends AnimalEntity implements IAnimatedEntity, IModNetable
{
	EntityAnimationManager manager = new EntityAnimationManager();
	EntityAnimationController<LizardEntity> controller = new EntityAnimationController<>(this, "moveController", 0.1F, this::animationPredicate);
	private static final DataParameter<Integer> LIZARD_VARIANT = EntityDataManager.createKey(LizardEntity.class, DataSerializers.VARINT);
	private boolean partyLizard;
	private BlockPos jukeboxPosition;

	public LizardEntity (EntityType<? extends AnimalEntity> type, World worldIn)
	{
		super(type, worldIn);
		getAnimationManager().addAnimationController(controller);
	}

	@Override
	protected void registerData ()
	{
		super.registerData();
		this.dataManager.register(LIZARD_VARIANT, 0);
	}

	@Override
	public ILivingEntityData onInitialSpawn (IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag)
	{
		Biome.Category biomeCategory = worldIn.getBiome(this.getPosition()).getCategory();
		int variant;
		boolean forceNotSad = false;
		if (dataTag != null && dataTag.contains("variant"))
		{
			variant = dataTag.getInt("variant");
			forceNotSad = true;
		} else if (biomeCategory.equals(Biome.Category.DESERT) || biomeCategory.equals(Biome.Category.MESA))
		{
			variant = this.getRNG().nextInt(2);
		} else if (biomeCategory.equals(Biome.Category.JUNGLE))
		{
			variant = this.getRNG().nextInt(2) + 2;
		} else
		{
			variant = this.getRNG().nextInt(4);
		}
		// 1/10 chance to change variant to sad lizard variant
		if (!forceNotSad && this.getRNG().nextInt(10) == 1)
		{
			variant += 4;  // Skip the first 4 entries in texture list to get to sad lizard textures (look at lizard render)
		}

		if (dataTag != null && dataTag.contains("health"))
		{
			this.setHealth(dataTag.getFloat("health"));
		}

		if (dataTag != null && dataTag.contains("name"))
		{
			this.setCustomName(ITextComponent.getTextComponentOrEmpty(dataTag.getString("name")));
		}

		setVariant(variant);

		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	private <E extends Entity> boolean animationPredicate (AnimationTestEvent<E> event)
	{
		if (event.isWalking())
		{
			this.controller.setAnimation((new AnimationBuilder()).addAnimation("WALK"));
			return true;
		}
		return false;
	}

	@Override
	public void livingTick ()
	{
		if (this.jukeboxPosition == null || !this.jukeboxPosition.withinDistance(this.getPositionVec(), 3.46D) || !this.world.getBlockState(this.jukeboxPosition).isIn(Blocks.JUKEBOX))
		{
			this.partyLizard = false;
			this.jukeboxPosition = null;
		}
		super.livingTick();
	}

	@Override
	public ActionResultType func_230254_b_ (PlayerEntity player, Hand hand) // on right click
	{
		ActionResultType result = super.func_230254_b_(player, hand);
		ItemStack item = player.getHeldItem(hand);
		if (item.getItem() instanceof AppleSliceItem && this.isSad())
		{
			this.setVariant(this.getVariant() - 4);
			item.shrink(1);
			spawnParticles(ParticleTypes.HEART);
			return ActionResultType.SUCCESS;
		}
		return result;
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

	public static AttributeModifierMap.MutableAttribute setCustomAttributes ()
	{
		return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 12.0D) // Max Health
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.4D); // Movement Speed
	}

	@Nullable
	@Override
	public AgeableEntity func_241840_a (ServerWorld p_241840_1_, AgeableEntity p_241840_2_)
	{
		return null;
	}

	@Override
	public EntityAnimationManager getAnimationManager ()
	{
		return this.manager;
	}

	@Override
	public void writeAdditional (CompoundNBT compound)
	{
		super.writeAdditional(compound);
		compound.putInt("variant", getVariant());
	}

	@Override
	public void readAdditional (CompoundNBT compound)
	{
		super.readAdditional(compound);
		if (compound.contains("variant"))
		{
			setVariant(compound.getInt("variant"));
		}
	}

	public int getVariant ()
	{
		return this.dataManager.get(LIZARD_VARIANT);
	}

	public void setVariant (int variant)
	{
		this.dataManager.set(LIZARD_VARIANT, variant);
	}

	public void spawnParticles (IParticleData data)
	{
		for (int i = 0; i < 7; ++i)
		{
			double d0 = this.rand.nextGaussian() * 0.02D;
			double d1 = this.rand.nextGaussian() * 0.02D;
			double d2 = this.rand.nextGaussian() * 0.02D;
			this.world.addParticle(data, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void setPartying (BlockPos pos, boolean isPartying)
	{
		this.jukeboxPosition = pos;
		this.partyLizard = isPartying;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean isPartying ()
	{
		return this.partyLizard;
	}

	public boolean isSad ()
	{
		return this.dataManager.get(LIZARD_VARIANT) > 3;
	}

	@Override
	public ItemStack getItem ()
	{
		int variant = this.getVariant();
		if (!(variant > 3))
		{
			Item item = ModItems.LIZARD_SPAWN_MAP.get(variant).get();
			ItemStack stack = new ItemStack(item);
			CompoundNBT nbt = stack.getOrCreateTag();
			nbt.putInt("variant", variant);
			nbt.putFloat("health", this.getHealth());
			if(this.hasCustomName())
			{
				nbt.putString("name", this.getCustomName().getString());
			}
			return stack;
		}
		return null;
	}

	@Override
	public void spawnParticleFeedback ()
	{
		spawnParticles(ParticleTypes.HAPPY_VILLAGER);
	}
}
