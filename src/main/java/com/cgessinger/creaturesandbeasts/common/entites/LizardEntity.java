package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.blocks.LizardEggBlock;
import com.cgessinger.creaturesandbeasts.common.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.common.goals.AnimatedBreedGoal;
import com.cgessinger.creaturesandbeasts.common.init.ModBlockRegistry;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.init.ModItems;
import com.cgessinger.creaturesandbeasts.common.interfaces.IAnimationHolder;
import com.cgessinger.creaturesandbeasts.common.interfaces.IModNetable;
import com.cgessinger.creaturesandbeasts.common.items.AppleSliceItem;
import com.cgessinger.creaturesandbeasts.common.util.AnimationHandler;
import com.cgessinger.creaturesandbeasts.common.util.AnimationHandler.ExecutionData;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.LookController;
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
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class LizardEntity
    extends AnimalEntity
    implements IAnimatable, IModNetable, IAnimationHolder<LizardEntity>
{
    private final AnimationFactory factory = new AnimationFactory( this );

    private static final DataParameter<Boolean> PARTYING =
        EntityDataManager.createKey( LizardEntity.class, DataSerializers.BOOLEAN );

    private static final DataParameter<Boolean> SAD =
        EntityDataManager.createKey( LizardEntity.class, DataSerializers.BOOLEAN );

    private static final DataParameter<Integer> LIZARD_VARIANT =
        EntityDataManager.createKey( LizardEntity.class, DataSerializers.VARINT );

    private static final DataParameter<Boolean> LAY_EGG =
        EntityDataManager.createKey( LizardEntity.class, DataSerializers.BOOLEAN );

    public BlockPos jukeboxPosition;

    private final AnimationHandler<LizardEntity> animationHandler;

    public LizardEntity( EntityType<? extends AnimalEntity> type, World worldIn )
    {
        super( type, worldIn );
        this.lookController = new LookController( this )
        {
            @Override
            public void tick()
            {
                LizardEntity lizard = (LizardEntity) this.mob;
                if ( lizard.shouldLookAround() )
                {
                    super.tick();
                }
            }
        };
        this.animationHandler = new AnimationHandler<>( "breed_controller", this, 110, 1, 0, LAY_EGG );
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        this.dataManager.register( LIZARD_VARIANT, 0 );
        this.dataManager.register( PARTYING, false );
        this.dataManager.register( SAD, false );
        this.dataManager.register( LAY_EGG, false );
    }

    @Override
    public ILivingEntityData onInitialSpawn( IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason,
                                             @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag )
    {
        int variant;
        boolean forceNotSad = false;
        if ( dataTag != null && dataTag.contains( "variant" ) )
        {
            variant = dataTag.getInt( "variant" );
            forceNotSad = true;
        }
        else
        {
            Biome.Category biomeCategory = worldIn.getBiome( this.getPosition() ).getCategory();
            variant = getLizardTypeFromBiome( biomeCategory );
        }

        setVariant( variant );
        // 1/10 chance to change variant to sad lizard variant
        this.setSad( !forceNotSad && this.getRNG().nextInt( 10 ) == 1 );

        if ( dataTag != null && dataTag.contains( "health" ) )
        {
            this.setHealth( dataTag.getFloat( "health" ) );
        }

        if ( dataTag != null && dataTag.contains( "name" ) )
        {
            this.setCustomName( ITextComponent.getTextComponentOrEmpty( dataTag.getString( "name" ) ) );
        }

        return super.onInitialSpawn( worldIn, difficultyIn, reason, spawnDataIn, dataTag );
    }

    @Override
    public void livingTick()
    {
        if ( this.jukeboxPosition != null )
        {
            TileEntity te = this.world.getTileEntity( this.jukeboxPosition );
            if ( !this.jukeboxPosition.withinDistance( this.getPositionVec(), 10.0D )
                            || !( te instanceof JukeboxTileEntity )
                            || ( (JukeboxTileEntity) te ).getRecord() == ItemStack.EMPTY )
            {
                this.setPartying( false, null );
            }
        }

        if ( this.isPartying() || this.dataManager.get( LAY_EGG ) )
        {
            this.navigator.clearPath();
            this.getNavigator().setSpeed( 0 );
        }

        super.livingTick();

        this.animationHandler.process();

        if ( this.isAlive() && this.dataManager.get(LAY_EGG) && this.ticksExisted % 10 == 0)
        {
            BlockPos blockpos = this.getPosition().down();
            this.world.playEvent( 2001, blockpos, Block.getStateId( this.world.getBlockState(blockpos) ) );
        }
    }

    private <E extends IAnimatable> PlayState animationPredicate( AnimationEvent<E> event )
    {
        if ( this.dataManager.get( LAY_EGG ) )
        {
            event.getController().setAnimation( new AnimationBuilder().addAnimation( "lizard.dig", true ) );
            return PlayState.CONTINUE;
        }
        else if ( !( limbSwingAmount > -0.15F && limbSwingAmount < 0.15F ) )
        {
            event.getController().setAnimation( new AnimationBuilder().addAnimation( "lizard.walk", true ) );
            return PlayState.CONTINUE;
        }
        else if ( this.isPartying() )
        {
            event.getController().setAnimation( new AnimationBuilder().addAnimation( "lizard.dance", true ) );
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    protected void damageEntity( DamageSource damageSrc, float damageAmount )
    {
        super.damageEntity( damageSrc, damageAmount );
        this.setPartying( false, null );
    }

    @Override
    public void registerControllers( AnimationData animationData )
    {
        animationData.addAnimationController( new AnimationController<LizardEntity>( this, "controller", 0,
                                                                                     this::animationPredicate ) );
    }

    @Override
    public AnimationFactory getFactory()
    {
        return this.factory;
    }

    @Override
    public ActionResultType func_230254_b_( PlayerEntity player, Hand hand ) // on right click
    {
        ActionResultType result = super.func_230254_b_( player, hand );
        ItemStack item = player.getHeldItem( hand );
        if ( item.getItem() instanceof AppleSliceItem && this.isSad() )
        {
            this.setSad( false );
            item.shrink( player.abilities.isCreativeMode ? 0 : 1 );
            spawnParticles( ParticleTypes.HEART );
            return ActionResultType.SUCCESS;
        }
        return result;
    }

    @Override
    protected void registerGoals()
    {
        this.goalSelector.addGoal( 0, new SwimGoal( this ) );
        this.goalSelector.addGoal( 1, new PanicGoal( this, 1.25D ) );
        this.goalSelector.addGoal( 3, new AnimatedBreedGoal<LizardEntity>( this, 1.0D ) );
        this.goalSelector.addGoal( 6, new WaterAvoidingRandomWalkingGoal( this, 1.0D )
        {
            @Override
            public boolean shouldExecute()
            {
                return !( (LizardEntity) this.creature ).isPartying() && super.shouldExecute();
            }
        } );
        this.goalSelector.addGoal( 7, new LookAtGoal( this, PlayerEntity.class, 6.0F ) );
        this.goalSelector.addGoal( 8, new LookRandomlyGoal( this ) );
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes()
    {
        return MobEntity.func_233666_p_().createMutableAttribute( Attributes.MAX_HEALTH,
                                                                  12.0D ).createMutableAttribute( Attributes.MOVEMENT_SPEED,
                                                                                                  0.4D ); // Movement
                                                                                                          // Speed
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a( ServerWorld world, AgeableEntity entity )
    {
        LizardEntity baby = ModEntityTypes.LIZARD.get().create( world );
        baby.setVariant( ( (LizardEntity) entity ).getVariant() );
        return baby;
    }

    @Override
    public void writeAdditional( CompoundNBT compound )
    {
        super.writeAdditional( compound );
        compound.putInt( "variant", getVariant() );
        compound.putBoolean( "sad", isSad() );
    }

    @Override
    public void readAdditional( CompoundNBT compound )
    {
        super.readAdditional( compound );
        if ( compound.contains( "variant" ) )
        {
            setVariant( compound.getInt( "variant" ) );
        }
        if ( compound.contains( "sad" ) )
        {
            setSad( compound.getBoolean( "sad" ) );
        }
    }

    public LizardType getLizardType()
    {
        return LizardType.values()[this.getVariant()];
    }

    public int getLizardTypeFromBiome( Biome.Category biomeCategory )
    {
        switch ( biomeCategory )
        {
            case DESERT:
            case MESA:
                return this.getRNG().nextInt( 2 );
            case JUNGLE:
                return this.getRNG().nextInt( 2 ) + 2;
            default:
                return this.getRNG().nextInt( 4 );
        }
    }

    public int getVariant()
    {
        return MathHelper.clamp( this.dataManager.get( LIZARD_VARIANT ), 0, LizardType.values().length );
    }

    public void setVariant( int variant )
    {
        this.dataManager.set( LIZARD_VARIANT, variant );
    }

    public void setPartying( boolean isPartying, BlockPos jukeboxPos )
    {
        if ( !this.isSad() )
        {
            this.dataManager.set( PARTYING, isPartying );
            this.jukeboxPosition = jukeboxPos;
        }
    }

    public boolean isPartying()
    {
        return this.dataManager.get( PARTYING );
    }

    public boolean isSad()
    {
        return this.dataManager.get( SAD );
    }

    public void setSad( boolean sad )
    {
        this.dataManager.set( SAD, sad );
    }

    public boolean shouldLookAround()
    {
        return !this.isPartying() && !this.dataManager.get( LAY_EGG );
    }

    @Override
    public boolean isBreedingItem( ItemStack stack )
    {
        return stack.getItem() instanceof AppleSliceItem;
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
    public ItemStack getItem()
    {
        if ( !this.isSad() && !this.isChild() )
        {
            LizardType type = this.getLizardType();
            ItemStack stack = new ItemStack( type.getItem() );
            CompoundNBT nbt = stack.getOrCreateTag();
            nbt.putInt( "variant", Arrays.asList( LizardType.values() ).indexOf( type ) );
            nbt.putFloat( "health", this.getHealth() );
            if ( this.hasCustomName() )
            {
                nbt.putString( "name", this.getCustomName().getString() );
            }
            return stack;
        }
        return null;
    }

    @Override
    public void spawnParticleFeedback()
    {
        spawnParticles( ParticleTypes.HAPPY_VILLAGER );
    }

    @Override
    public void checkDespawn()
    {
        if ( !CNBConfig.ServerConfig.LIZARD_CONFIG.shouldExist )
        {
            this.remove();
            return;
        }
        super.checkDespawn();
    }

    @Override
    public void executeBreakpoint( Optional<ExecutionData> data )
    {
        if ( data.isPresent() && data.get().isBreedData )
        {
            ServerWorld world = data.get().world;
            LizardEntity lizardMate = (LizardEntity) data.get().entity;
            BlockState state = ModBlockRegistry.LIZARD_EGGS.get().getDefaultState()
                                    .with( LizardEggBlock.EGGS, Integer.valueOf( this.rand.nextInt( 4 ) + 3 ) )
                                    .with( LizardEggBlock.VARIANT_0, this.getVariant() )
                                    .with( LizardEggBlock.VARIANT_1, lizardMate.getVariant() );

            world.setBlockState( this.getPosition(), state, 3 );
        }
    }

    @Override
    public AnimationHandler<LizardEntity> getAnimationHandler (String name)
    {
        return this.animationHandler;
    }

    public static boolean canLizardSpawn( EntityType<LizardEntity> animal, IWorld worldIn,
                                             SpawnReason reason, BlockPos pos, Random randomIn )
    {
        return worldIn.getLightSubtracted(pos, 0) > 8;
    }

    public enum LizardType
    {
        DESERT_1( createLocation( "textures/model/entity/lizard/lizard_desert.png" ),
            createLocation( "textures/model/entity/lizard/sad_lizard_desert.png" ), ModItems.LIZARD_ITEM_0.get() ),
        DESERT_2( createLocation( "textures/model/entity/lizard/lizard_desert_2.png" ),
            createLocation( "textures/model/entity/lizard/sad_lizard_desert_2.png" ), ModItems.LIZARD_ITEM_1.get() ),
        JUNGLE_1( createLocation( "textures/model/entity/lizard/lizard_jungle.png" ),
            createLocation( "textures/model/entity/lizard/sad_lizard_jungle.png" ), ModItems.LIZARD_ITEM_2.get() ),
        JUNGLE_2( createLocation( "textures/model/entity/lizard/lizard_jungle_2.png" ),
            createLocation( "textures/model/entity/lizard/sad_lizard_jungle_2.png" ), ModItems.LIZARD_ITEM_3.get() );

        public final ResourceLocation textureLocation;

        public final ResourceLocation textureLocationSad;

        public final Item item;

        LizardType( ResourceLocation tl, ResourceLocation tls, Item it )
        {
            this.textureLocation = tl;
            this.textureLocationSad = tls;
            this.item = it;
        }

        private static ResourceLocation createLocation( String pathPart )
        {
            return new ResourceLocation( CreaturesAndBeasts.MOD_ID, pathPart );
        }

        public ResourceLocation getTextureLocation( boolean sad )
        {
            if ( sad )
            {
                return textureLocationSad;
            }
            return textureLocation;
        }

        public Item getItem()
        {
            return item;
        }
    }
}
