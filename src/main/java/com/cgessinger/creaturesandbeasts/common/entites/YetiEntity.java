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
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
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
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

import java.util.List;
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

    private final float babyHealth = 20.0F;

    public static final DataParameter<Boolean> ATTACKING =
        EntityDataManager.createKey( YetiEntity.class, DataSerializers.BOOLEAN );

    public static final DataParameter<Boolean> EAT =
        EntityDataManager.createKey( YetiEntity.class, DataSerializers.BOOLEAN );

    public static final DataParameter<ItemStack> HOLDING =
        EntityDataManager.createKey( YetiEntity.class, DataSerializers.ITEMSTACK );

    public AnimationHandler<YetiEntity> attackHandler;

    public AnimationHandler<YetiEntity> eatHandler;

    public boolean isPassive;

    public YetiEntity( EntityType<? extends AnimalEntity> type, World worldIn )
    {
        super( type, worldIn );
        this.attackHandler = new AnimationHandler<>( "attack_controller", this, 35, 17, 5, ATTACKING );
        this.eatHandler = new AnimationHandler<>( "breed_controller", this, 40, 10, 20, EAT );
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
        return MobEntity.func_233666_p_().createMutableAttribute( Attributes.MAX_HEALTH,
                                                                  80.0D ).createMutableAttribute( Attributes.MOVEMENT_SPEED,
                                                                                                  0.3D ).createMutableAttribute( Attributes.ATTACK_DAMAGE,
                                                                                                                                 16.0D ).createMutableAttribute( Attributes.ATTACK_SPEED,
                                                                                                                                                                 0.1D ).createMutableAttribute( Attributes.KNOCKBACK_RESISTANCE,
                                                                                                                                                                                                0.7D );
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        this.dataManager.register( ATTACKING, false );
        this.dataManager.register( EAT, false );
        this.dataManager.register( HOLDING, ItemStack.EMPTY );
    }

    @Override
    public void setGrowingAge( int age )
    {
        super.setGrowingAge( age );
        double MAX_HEALTH = this.getAttribute( Attributes.MAX_HEALTH ).getValue();
        if ( isChild() && MAX_HEALTH > this.babyHealth )
        {
            Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
            multimap.put( Attributes.MAX_HEALTH,
                          new AttributeModifier( this.healthReductionUUID, "yeti_health_reduction",
                                                 this.babyHealth - MAX_HEALTH, AttributeModifier.Operation.ADDITION ) );
            this.getAttributeManager().reapplyModifiers( multimap );
            this.setHealth( this.babyHealth );
        }
    }

    @Override
    protected void onGrowingAdult()
    {
        super.onGrowingAdult();
        this.getAttribute( Attributes.MAX_HEALTH ).removeModifier( this.healthReductionUUID );
        this.setHealth( (float) this.getAttribute( Attributes.MAX_HEALTH ).getValue() );
    }

    @Override
    public void writeAdditional( CompoundNBT compound )
    {
        super.writeAdditional( compound );
        compound.putBoolean( "passive", this.isPassive );
    }

    @Override
    public void readAdditional( CompoundNBT compound )
    {
        super.readAdditional( compound );

        if ( compound.contains( "passive" ) )
        {
            this.isPassive = compound.getBoolean( "passive" );
        }
    }

    private <E extends IAnimatable> PlayState animationPredicate( AnimationEvent<E> event )
    {
        if ( this.dataManager.get( EAT ) )
        {
            event.getController().setAnimation( new AnimationBuilder().addAnimation( this.isChild() ? "yeti_baby.eat"
                            : "yeti_adult.eat", false ) );
            return PlayState.CONTINUE;
        }
        else if ( this.dataManager.get( ATTACKING ) )
        {
            event.getController().setAnimation( new AnimationBuilder().addAnimation( "yeti.attack", false ) );
            return PlayState.CONTINUE;
        }
        else if ( !( limbSwingAmount > -0.15F && limbSwingAmount < 0.15F ) )
        {
            event.getController().setAnimation( new AnimationBuilder().addAnimation( "yeti.walk", true ) );
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation( new AnimationBuilder().addAnimation( "yeti.idle", false ) );
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> void soundListener( SoundKeyframeEvent<E> event )
    {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        player.playSound( ModSoundEventTypes.YETI_HIT.get(), 0.4F, 1F );
    }

    private <E extends IAnimatable> void particleListener( ParticleKeyFrameEvent<E> event )
    {
        ParticleManager manager = Minecraft.getInstance().particles;
        BlockPos pos = this.getPosition();

        if ( "hit.ground.particle".equals( event.effect ) )
        {
            for ( int x = pos.getX() - 1; x <= pos.getX() + 1; x++ )
            {
                for ( int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++ )
                {
                    BlockPos newPos = new BlockPos( x, pos.getY() - 1, z );
                    manager.addBlockDestroyEffects( newPos, this.world.getBlockState( newPos ) );
                }
            }
        }
        else if ( "eat.particle".equals( event.effect ) )
        {
            spawnParticles( ParticleTypes.HAPPY_VILLAGER );
        }
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
        this.goalSelector.addGoal( 1, new BreedGoal( this, 1.0D ) );
        this.goalSelector.addGoal( 2, new AnimatedAttackGoal<YetiEntity>( this, 1.2D, true )
        {
            @Override
            public boolean shouldContinueExecuting()
            {
                return super.shouldContinueExecuting() && !( (YetiEntity) this.attacker ).isPassive;
            }

            @Override
            public boolean shouldExecute()
            {
                return super.shouldExecute() && !( (YetiEntity) this.attacker ).isPassive;
            }
        } );
        this.goalSelector.addGoal( 3, new FollowParentGoal( this, 1.25D ) );
        this.goalSelector.addGoal( 4, new LookAtGoal( this, PlayerEntity.class, 12.0F ) );
        this.goalSelector.addGoal( 5, new LookRandomlyGoal( this ) );
        this.goalSelector.addGoal( 6, new WaterAvoidingRandomWalkingGoal( this, 1.0D, 0.01F ) );
        this.targetSelector.addGoal( 1, new HurtByTargetGoal( this ) );
        this.targetSelector.addGoal( 2, new YetiEntity.AttackPlayerGoal() );
    }

    @Override
    public void registerControllers( AnimationData animationData )
    {
        AnimationController<YetiEntity> controller =
            new AnimationController<>( this, "controller", 0, this::animationPredicate );

        controller.registerSoundListener( this::soundListener );
        controller.registerParticleListener( this::particleListener );

        animationData.addAnimationController( controller );
    }

    public boolean isEating()
    {
        return this.dataManager.get( EAT );
    }

    @Override
    public AnimationHandler<YetiEntity> getAnimationHandler( String name )
    {
        if ( name.equals( "attack_controller" ) )
        {
            return this.attackHandler;
        }

        return this.eatHandler;
    }

    @Override
    public void livingTick()
    {
        super.livingTick();

        if ( this.dataManager.get( EAT ) )
        {
            this.navigator.clearPath();
        }

        this.attackHandler.process();
        this.eatHandler.process();
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
        if ( data.isPresent() )
        {
            ExecutionData execData = data.get();
            if ( execData.name.equals( this.attackHandler.name ) )
            {
                this.executeAttack();
            }
            else if ( execData.name.equals( this.eatHandler.name ) )
            {
                if ( this.isChild() )
                {
                    this.ageUp( (int) ( -this.getGrowingAge() / 20F * 0.1F ), true );
                }
                else if ( this.getHolding().getItem() == Items.SWEET_BERRIES )
                {
                    this.setInLove( execData.player );
                }
                else
                {
                    this.setAttackTarget( null );
                    this.isPassive = true;
                }
                this.setHolding( ItemStack.EMPTY );
            }
        }
    }

    public void setHolding( ItemStack stack )
    {
        this.dataManager.set( HOLDING, stack );
    }

    public ItemStack getHolding()
    {
        return this.dataManager.get( HOLDING );
    }

    public ActionResultType tryStartEat( PlayerEntity player, ItemStack stack )
    {
        if ( this.world.isRemote )
            return ActionResultType.CONSUME;

        if ( this.eatHandler.canStart() )
        {
            if ( stack.getItem() == Items.MELON_SLICE && !this.isPassive )
            {
                return this.startEat( player, stack );
            }
            else if ( stack.getItem() == Items.SWEET_BERRIES )
            {
                if ( ( this.getGrowingAge() == 0 && this.canFallInLove() ) || this.isChild() )
                {
                    return this.startEat( player, stack );
                }
            }
        }
        return ActionResultType.PASS;
    }

    private ActionResultType startEat( PlayerEntity player, ItemStack stack )
    {
        this.setHolding( stack );
        this.consumeItemFromStack( player, stack );
        this.eatHandler.startAnimation( ExecutionData.create().withPlayer( player ).build() );
        SoundEvent sound =
            this.isChild() ? ModSoundEventTypes.YETI_BABY_EAT.get() : ModSoundEventTypes.YETI_ADULT_EAT.get();
        this.playSound( sound, 1.1F, 1F );
        return ActionResultType.SUCCESS;

    }

    @Override
    public ActionResultType func_230254_b_( PlayerEntity player, Hand hand ) // on right click
    {
        super.func_230254_b_( player, hand );

        ItemStack item = player.getHeldItem( hand );
        return this.tryStartEat( player, item );
    }

    @Override
    public boolean isBreedingItem( ItemStack stack )
    {
        return false;
    }

    public void spawnParticles( IParticleData data )
    {
        for ( int i = 0; i < 7; ++i )
        {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.world.addParticle( data, this.getPosXRandom( 1.0D ), this.getPosYRandom() + 0.5D,
                                    this.getPosZRandom( 1.0D ), d0, d1, d2 );
        }
    }

    @Override
    public boolean canDespawn( double distanceToClosestPlayer )
    {
        return false;
    }

    @Override
    public boolean attackEntityFrom( DamageSource source, float amount )
    {
        if ( this.isChild() )
        {
            List<YetiEntity> list =
                this.world.getEntitiesWithinAABB( this.getClass(), this.getBoundingBox().grow( 8.0D, 4.0D, 8.0D ) );

            for ( YetiEntity yeti : list )
            {
                if ( !yeti.isChild() )
                {
                    yeti.isPassive = false;
                    break;
                }
            }
        }
        this.isPassive = false;
        return super.attackEntityFrom( source, amount );
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
        return random.nextDouble() >= ServerConfig.YETI_PROP.value;
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
                    if ( yeti.isChild() && !YetiEntity.this.isPassive )
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
