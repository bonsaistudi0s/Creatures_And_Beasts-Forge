package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.common.config.CNBConfig.ServerConfig;
import com.cgessinger.creaturesandbeasts.common.goals.AnimatedAttackGoal;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import com.cgessinger.creaturesandbeasts.common.interfaces.IAnimationHolder;
import com.cgessinger.creaturesandbeasts.common.util.AnimationHandler;
import com.cgessinger.creaturesandbeasts.common.util.AnimationHandler.ExecutionData;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

public class YetiEntity
    extends AnimalEntity
    implements IAnimatable, IAnimationHolder<YetiEntity>, IMob
{
    private final AnimationFactory factory = new AnimationFactory( this );

    private final UUID healthReductionUUID = UUID.fromString( "189faad9-35de-4e15-a598-82d147b996d7" );

    public static final DataParameter<Boolean> ANIMATING =
        EntityDataManager.createKey( YetiEntity.class, DataSerializers.BOOLEAN );

    public AnimationHandler<YetiEntity> animationHandler;

    public YetiEntity( EntityType<? extends AnimalEntity> type, World worldIn )
    {
        super( type, worldIn );
        this.animationHandler = new AnimationHandler<>( this, 35, 12, 5, ANIMATING );
    }

    @Override
    public ILivingEntityData onInitialSpawn( IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason,
                                             ILivingEntityData spawnDataIn, CompoundNBT dataTag )
    {

        if ( spawnDataIn == null )
        {
            spawnDataIn = new AgeableEntity.AgeableData( 1.0F );
        }

        return super.onInitialSpawn( worldIn, difficultyIn, reason, spawnDataIn, dataTag );
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes()
    {
        return MobEntity.func_233666_p_().createMutableAttribute( Attributes.MAX_HEALTH, 80.0D )
        .createMutableAttribute( Attributes.MOVEMENT_SPEED,0.3D )
        .createMutableAttribute( Attributes.ATTACK_DAMAGE, 16.0D )
        .createMutableAttribute( Attributes.ATTACK_SPEED, 0.1D )
        .createMutableAttribute( Attributes.KNOCKBACK_RESISTANCE, 0.7D );
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        this.dataManager.register( ANIMATING, false );
    }

    @Override
    public void setGrowingAge( int age )
    {
        super.setGrowingAge( age );
        if ( isChild() && this.getAttribute( Attributes.MAX_HEALTH ).getValue() > 20.0D )
        {
            Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
            multimap.put( Attributes.MAX_HEALTH,
                          new AttributeModifier( this.healthReductionUUID, "yeti_health_reduction", -60,
                                                 AttributeModifier.Operation.ADDITION ) );
            this.getAttributeManager().reapplyModifiers( multimap );
            this.setHealth( 20.0F );
        }
    }

    @Override
    protected void onGrowingAdult()
    {
        this.getAttribute( Attributes.MAX_HEALTH ).removeModifier( this.healthReductionUUID );
        this.setHealth( 80.0F );
    }

    private <E extends IAnimatable> PlayState animationPredicate( AnimationEvent<E> event )
    {
        if ( this.dataManager.get( ANIMATING ) )
        {
            event.getController().setAnimation( new AnimationBuilder().addAnimation( "yeti.attack", false ) );
            return PlayState.CONTINUE;
        }
        else if ( !( limbSwingAmount > -0.15F && limbSwingAmount < 0.15F ) )
        {
            event.getController().setAnimation( new AnimationBuilder().addAnimation( "yeti.walk", true ) );
            return PlayState.CONTINUE;
        }

        return PlayState.STOP;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a( ServerWorld p_241840_1_, AgeableEntity p_241840_2_ )
    {
        return ModEntityTypes.YETI.get().create( p_241840_1_ );
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal( 1, new SwimGoal( this ) );
        this.goalSelector.addGoal( 2, new AnimatedAttackGoal<YetiEntity>( this, 1.2D, true ) );
        this.goalSelector.addGoal( 3, new FollowParentGoal( this, 1.25D ) );
        this.goalSelector.addGoal( 4, new LookAtGoal( this, PlayerEntity.class, 12.0F ) );
        this.goalSelector.addGoal( 5, new LookRandomlyGoal( this ) );
        this.goalSelector.addGoal( 6, new WaterAvoidingRandomWalkingGoal( this, 1.0D, 60 ) );
        this.targetSelector.addGoal( 1, new HurtByTargetGoal( this ) );
        this.targetSelector.addGoal( 2, new YetiEntity.AttackPlayerGoal() );
    }

    @Override
    public void registerControllers( AnimationData animationData )
    {
        animationData.addAnimationController( new AnimationController<>( this, "controller", 0,
                                                                         this::animationPredicate ) );
    }

    @Override
    public AnimationHandler<YetiEntity> getAnimationHandler()
    {
        return this.animationHandler;
    }

    @Override
    public void livingTick()
    {
        super.livingTick();

        this.animationHandler.process();
    }

    @Override
    public Optional<ExecutionData> onAnimationInit( Optional<ExecutionData> data )
    {
        this.getNavigator().clearPath();
        return IAnimationHolder.super.onAnimationInit( data );
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    public void executeAttack()
    {
        for ( LivingEntity entity : this.world.getEntitiesWithinAABB( LivingEntity.class,
                                                                      this.getBoundingBox().grow( 3.0D, 2.0D, 3.0D ) ) )
        {
            if ( !( entity instanceof YetiEntity ) )
            {
                this.attackEntityAsMob( entity );
            }
        }
    }

    @Override
    public void executeBreakpoint( Optional<ExecutionData> data )
    {
        this.executeAttack();
    }

    @Override
    public boolean canDespawn( double distanceToClosestPlayer )
    {
        return false;
    }

    @Override
    protected void playStepSound( BlockPos pos, BlockState blockIn )
    {
        if ( !blockIn.getMaterial().isLiquid() )
        {
            this.playSound( ModSoundEventTypes.YETI_STEP.get(), this.getSoundVolume() * 0.3F, this.getSoundPitch() );
        }
    }

    @Override
    protected float getSoundPitch()
    {
        float pitch = super.getSoundPitch();
        return this.isChild() ? pitch * 1.5F : pitch;
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return this.isChild() ? null : ModSoundEventTypes.YETI_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return this.isChild() ? null : ModSoundEventTypes.YETI_HURT.get();
    }

    @Override
    protected SoundEvent getHurtSound( DamageSource damageSourceIn )
    {
        return this.isChild() ? null : ModSoundEventTypes.YETI_HURT.get();
    }

    @Override
    public void checkDespawn()
    {
        if ( !CNBConfig.ServerConfig.YETI_CONFIG.shouldExist )
        {
            this.remove();
            return;
        }
        super.checkDespawn();
    }

    public static boolean canYetiSpawn( EntityType<? extends AnimalEntity> animal, IWorld worldIn, SpawnReason reason,
                                        BlockPos pos, Random random )
    {
        return random.nextFloat() >= ServerConfig.YETI_PROP.value
                        && AnimalEntity.canAnimalSpawn( animal, worldIn, reason, pos, random );
    }

    class AttackPlayerGoal
        extends NearestAttackableTargetGoal<PlayerEntity>
    {
        public AttackPlayerGoal()
        {
            super( YetiEntity.this, PlayerEntity.class, 20, true, true, (Predicate<LivingEntity>) null );
        }

        @Override
        public boolean shouldExecute()
        {
            if ( !YetiEntity.this.isChild() && super.shouldExecute() )
            {
                for ( YetiEntity yeti : YetiEntity.this.world.getEntitiesWithinAABB( YetiEntity.class,
                                                                                     YetiEntity.this.getBoundingBox().grow( 8.0D,
                                                                                                                            4.0D,
                                                                                                                            8.0D ) ) )
                {
                    if ( yeti.isChild() )
                    {
                        return true;
                    }
                }

            }
            return false;
        }

        @Override
        protected double getTargetDistance()
        {
            return super.getTargetDistance() * 0.5D;
        }
    }
}
