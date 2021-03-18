package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import com.cgessinger.creaturesandbeasts.common.interfaces.IAnimationHolder;
import com.cgessinger.creaturesandbeasts.common.util.AnimationHandler;
import com.cgessinger.creaturesandbeasts.common.util.AnimationHandler.ExecutionData;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Optional;

public class FriendlySporelingEntity
    extends AbstractSporelingEntity
    implements IAnimationHolder<FriendlySporelingEntity>
{
    private static final DataParameter<Boolean> WAVE =
        EntityDataManager.createKey( FriendlySporelingEntity.class, DataSerializers.BOOLEAN );
    
    private static final DataParameter<Boolean> INSPECT =
        EntityDataManager.createKey( FriendlySporelingEntity.class, DataSerializers.BOOLEAN );
    
    public final AnimationHandler<FriendlySporelingEntity> animationHandler;

    public FriendlySporelingEntity( EntityType<? extends CreatureEntity> type, World worldIn )
    {
        super( type, worldIn );
        this.setCanPickUpLoot( true );
        this.animationHandler = new AnimationHandler<>("trade_controller", this, 40, 1, 0, INSPECT);
    }

    @Override
    public void livingTick()
    {
        super.livingTick();

        if ( this.dataManager.get( WAVE ) )
        {
            this.navigator.clearPath();
            this.getNavigator().setSpeed( 0 );
        }

        this.animationHandler.process();
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn( IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason,
                                             @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag )
    {
        this.setSporelingType( this.getRNG().nextInt( 2 ) );
        return super.onInitialSpawn( worldIn, difficultyIn, reason, spawnDataIn, dataTag );
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal( 1, new PanicGoal( this, 1.25D ) );
        this.goalSelector.addGoal( 6, new WaveGoal( this, PlayerEntity.class, 8.0F ) );
    }

    @Override
    protected void registerData()
    {
        super.registerData();
        this.dataManager.register( WAVE, false );
        this.dataManager.register( INSPECT, false );
    }

    public void setWave( boolean wave )
    {
        this.dataManager.set( WAVE, wave );
    }

    @Override
    public <E extends IAnimatable> PlayState animationPredicate( AnimationEvent<E> event )
    {
        if ( super.animationPredicate( event ) == PlayState.STOP )
        {
            if ( this.dataManager.get( WAVE ) )
            {
                event.getController().setAnimation( new AnimationBuilder().addAnimation( "sporeling.wave", false ) );
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound( DamageSource damageSourceIn )
    {
        return ModSoundEventTypes.SPORELING_OVERWORLD_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return ModSoundEventTypes.SPORELING_OVERWORLD_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return ModSoundEventTypes.SPORELING_OVERWORLD_AMBIENT.get();
    }

    @Override
    public void checkDespawn()
    {
        if ( !CNBConfig.ServerConfig.FRIENDLY_SPORELING_CONFIG.shouldExist )
        {
            this.remove();
            return;
        }
        super.checkDespawn();
    }

    @Override
    protected void updateEquipmentIfNeeded( ItemEntity itemEntity )
    {
        ItemStack stack = itemEntity.getItem();
        if ( this.canPickUpItem( stack ) )
        {
            this.triggerItemPickupTrigger( itemEntity );
            this.setItemStackToSlot( EquipmentSlotType.MAINHAND, stack );
            this.setHolding(stack);
            itemEntity.remove();
            this.animationHandler.startAnimation(ExecutionData.create().withItemStack(stack).build());
        }
    }

    @Override
    public boolean canPickUpItem( ItemStack itemstackIn )
    {
        if ( !this.hasItemInSlot(EquipmentSlotType.MAINHAND) )
        {
            if ( itemstackIn.isEnchanted() )
            {
                for ( Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments( itemstackIn ).entrySet() )
                {
                    if ( entry.getKey().isCurse() )
                    {
                        return true;
                    }
                }
            }
            return itemstackIn.getItem() == Items.DIRT;
        }
        return false;
    }
    
    @Override
    public void executeBreakpoint( Optional<ExecutionData> data )
    {
        if (!data.isPresent())
            return;

        ItemStack stack = data.get().stack;
        if ( stack != null )
        {
            if ( stack.isEnchanted() )
            {
                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments( stack );
                for ( Entry<Enchantment, Integer> entry :  map.entrySet())
                {
                    if ( entry.getKey().isCurse() )
                    {
                        map.remove(entry.getKey(), entry.getValue());
                        if(stack.isDamageable())
                        {
                            float percent = ( this.getRNG().nextFloat() * 0.8F ) + 0.1F;
                            int damage = (int) ( percent * stack.getMaxDamage() + stack.getDamage() );
                            int setDamage = Math.min(damage, (int) ( stack.getMaxDamage() * 0.9F) );
                            stack.setDamage(Math.max(stack.getDamage(), setDamage) );
                        }
                        EnchantmentHelper.setEnchantments(map, stack);
                        break;
                    }
                }
            } else if (stack.getItem() == Items.DIRT)
            {
                stack = new ItemStack(Items.MYCELIUM, stack.getCount());
            }

            this.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
            this.setHolding(ItemStack.EMPTY);
            this.entityDropItem(stack);
        }
    }

    @Override
    public AnimationHandler<FriendlySporelingEntity> getAnimationHandler (String name)
    {
        return this.animationHandler;
    }

    static class WaveGoal
        extends LookAtGoal
    {
        private final FriendlySporelingEntity sporeling;

        private int waveTimer;

        public WaveGoal( FriendlySporelingEntity entityIn, Class<? extends LivingEntity> watchTargetClass,
                         float maxDistance )
        {
            super( entityIn, watchTargetClass, maxDistance );
            sporeling = entityIn;
            this.waveTimer = 0;
        }

        @Override
        public boolean shouldExecute()
        {
            boolean shouldExec = super.shouldExecute();
            if ( shouldExec && this.waveTimer == 0 && this.sporeling.getRNG().nextInt( 9 ) == 0 )
            {
                this.waveTimer = 8;
            }
            else if ( this.waveTimer > 0 && this.closestEntity != null )
            {
                this.sporeling.setWave( --this.waveTimer > 0 );
            }
            return shouldExec;
        }

        @Override
        public boolean shouldContinueExecuting()
        {
            return super.shouldContinueExecuting() && this.sporeling.getLookController().getIsLooking();
        }
    }

    public static boolean canSporelingSpawn( EntityType<FriendlySporelingEntity> p_234418_0_, IWorld worldIn,
                                             SpawnReason p_234418_2_, BlockPos pos, Random p_234418_4_ )
    {
        return ( worldIn.getBlockState( pos.down() ).isIn( Blocks.MYCELIUM )
                        || worldIn.getBlockState( pos.down() ).isIn( Blocks.GRASS_BLOCK ) )
                        && worldIn.getLightSubtracted( pos, 0 ) > 8;
    }
}
