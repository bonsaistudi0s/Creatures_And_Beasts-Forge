package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.init.ModItems;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import com.cgessinger.creaturesandbeasts.common.interfaces.IAnimationHolder;
import com.cgessinger.creaturesandbeasts.common.util.AnimationHandler;
import com.cgessinger.creaturesandbeasts.common.util.AnimationHandler.ExecutionData;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

import java.util.Optional;
import java.util.Random;

public class CindershellEntity extends AnimalEntity implements IAnimationHolder<CindershellEntity>
{
    private static final DataParameter<Boolean> EAT =
        EntityDataManager.createKey( LizardEntity.class, DataSerializers.BOOLEAN );

    private final AnimationHandler<CindershellEntity> animationHandler;

	public CindershellEntity (EntityType<? extends AnimalEntity> type, World worldIn)
	{
		super(type, worldIn);
        this.animationHandler = new AnimationHandler<>( "eat_controller", this, 40, 1, 0, EAT );
	}

    @Override
    protected void registerData()
    {
        super.registerData();
        this.dataManager.register(EAT, false);
    }

	@Override
	protected void registerGoals ()
	{
		this.goalSelector.addGoal(0, new SwimGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
		this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D)
        {
            @Override
            protected void spawnBaby()
            {
                int range = this.animal.getRNG().nextInt(4) + 3;
                for (int i = 0; i <= range; i++)
                {
                    super.spawnBaby();
                }
            }
        });
		this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes ()
	{
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 80.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.15D)
				.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 100D);
	}

    @Override
    public ILivingEntityData onInitialSpawn( IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason,
                                             ILivingEntityData spawnDataIn, CompoundNBT dataTag )
    {
        if ( dataTag != null )
        {
            if ( dataTag.contains("age") )
            {
                this.setGrowingAge(dataTag.getInt("age"));
            }
            if ( dataTag.contains( "health" ) )
            {
                this.setHealth( dataTag.getFloat( "health" ) );
            }
            if ( dataTag.contains( "name" ) )
            {
                this.setCustomName( ITextComponent.getTextComponentOrEmpty( dataTag.getString( "name" ) ) );
            }
        }

        return super.onInitialSpawn( worldIn, difficultyIn, reason, spawnDataIn, dataTag );
    }

    @Override
    public void livingTick()
    {
        super.livingTick();
        this.animationHandler.process();
    }

	@Override
	public float getEyeHeight (Pose pose)
	{
		return this.getHeight() * 0.2F;
	}

	public static boolean canCindershellSpawn(EntityType<CindershellEntity> p_234418_0_, IWorld p_234418_1_, SpawnReason p_234418_2_, BlockPos p_234418_3_, Random p_234418_4_) 
    {
		return true;
    }

	@Nullable
	@Override
	public AgeableEntity func_241840_a (ServerWorld p_241840_1_, AgeableEntity p_241840_2_)
	{
		return ModEntityTypes.CINDERSHELL.get().create(p_241840_1_);
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
    
    @Override
    public void checkDespawn() 
    {
        if(!CNBConfig.ServerConfig.CINDERSHELL_CONFIG.shouldExist)
        {
            this.remove();
            return;
        }
        super.checkDespawn();
    }

    @Override
    public boolean isBreedingItem( ItemStack stack )
    {
        return false;
    }

    public ActionResultType tryStartEat ( PlayerEntity player, ItemStack stack )
    {
        if ( stack.getItem() == Items.CRIMSON_FUNGUS || stack.getItem() == Items.WARPED_FUNGUS ) 
        {
            int i = this.getGrowingAge();
            if (!this.world.isRemote && i == 0 && this.canFallInLove()) 
            {
                this.consumeItemFromStack(player, stack);
                this.animationHandler.startAnimation(ExecutionData.create().withPlayer(player).build());
                this.playSound(ModSoundEventTypes.CINDERSHELL_ADULT_EAT.get(), 1.2F, 1F);
                return ActionResultType.SUCCESS;
            }
    
            if (this.isChild()) 
            {
                this.playSound(ModSoundEventTypes.CINDERSHELL_BABY_EAT.get(), 1.3F, 1F);
                this.consumeItemFromStack(player, stack);
                this.ageUp((int)(-i / 20F * 0.1F), true);
                return ActionResultType.func_233537_a_(this.world.isRemote);
            }
    
            if (this.world.isRemote) 
            {
                return ActionResultType.CONSUME;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public ActionResultType func_230254_b_( PlayerEntity player, Hand hand ) // on right click
    {
        ItemStack item = player.getHeldItem( hand );
        if ( item.getItem() == Items.LAVA_BUCKET && this.isChild() )
        {
            //spawnParticles( ParticleTypes.HEART );
            ItemStack stack = new ItemStack(ModItems.CINDERSHELL_BUCKET.get(), item.getCount());
            CompoundNBT nbt = stack.getOrCreateTag();
            nbt.putInt("age", this.getGrowingAge());
            nbt.putFloat( "health", this.getHealth() );
            if ( this.hasCustomName() )
            {
                nbt.putString( "name", this.getCustomName().getString() );
            }

            player.setHeldItem(hand, stack);
            this.remove();
            return ActionResultType.SUCCESS;
        }

        return this.tryStartEat(player, item);
    }

    @Override
    public void executeBreakpoint( Optional<ExecutionData> data )
    {
        if ( data.isPresent() && data.get().player != null )
        {
            this.setInLove(data.get().player);
        }
    }

    @Override
    public AnimationHandler<CindershellEntity> getAnimationHandler (String name)
    {
        return this.animationHandler;
    }
}
