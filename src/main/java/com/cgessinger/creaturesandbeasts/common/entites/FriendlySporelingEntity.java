package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import com.cgessinger.creaturesandbeasts.common.interfaces.IAnimationHolder;
import com.cgessinger.creaturesandbeasts.common.util.AnimationHandler;
import com.cgessinger.creaturesandbeasts.common.util.AnimationHandler.ExecutionData;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.BlockPos;
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

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;

public class FriendlySporelingEntity
    extends AbstractSporelingEntity
    implements IAnimationHolder<FriendlySporelingEntity>
{
    private static final EntityDataAccessor<Boolean> WAVE =
        SynchedEntityData.defineId( FriendlySporelingEntity.class, EntityDataSerializers.BOOLEAN );
    
    private static final EntityDataAccessor<Boolean> INSPECT =
        SynchedEntityData.defineId( FriendlySporelingEntity.class, EntityDataSerializers.BOOLEAN );
    
    public final AnimationHandler<FriendlySporelingEntity> animationHandler;

    public FriendlySporelingEntity( EntityType<? extends PathfinderMob> type, Level worldIn )
    {
        super( type, worldIn );
        this.setCanPickUpLoot( true );
        this.animationHandler = new AnimationHandler<>("trade_controller", this, 40, 1, 0, INSPECT);
    }

    @Override
    public void aiStep()
    {
        super.aiStep();

        if ( this.entityData.get( WAVE ) )
        {
            this.navigation.stop();
            this.getNavigation().setSpeedModifier( 0 );
        }

        this.animationHandler.process();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn( ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason,
                                             @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag )
    {
        this.setSporelingType( this.getRandom().nextInt( 2 ) );
        return super.finalizeSpawn( worldIn, difficultyIn, reason, spawnDataIn, dataTag );
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal( 1, new PanicGoal( this, 1.25D ) );
        this.goalSelector.addGoal( 6, new WaveGoal( this, Player.class, 8.0F ) );
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define( WAVE, false );
        this.entityData.define( INSPECT, false );
    }

    public void setWave( boolean wave )
    {
        this.entityData.set( WAVE, wave );
    }

    @Override
    public <E extends IAnimatable> PlayState animationPredicate( AnimationEvent<E> event )
    {
        if ( super.animationPredicate( event ) == PlayState.STOP )
        {
            if ( this.entityData.get( WAVE ) && this.getHolding() == ItemStack.EMPTY )
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
    protected void pickUpItem( ItemEntity itemEntity )
    {
        ItemStack stack = itemEntity.getItem();
        if ( this.canTakeItem( stack ) && this.animationHandler.canStart() )
        {
            this.onItemPickup( itemEntity );
            this.setItemSlot( EquipmentSlot.MAINHAND, stack );
            this.setHolding(stack);
            itemEntity.remove();
            this.animationHandler.startAnimation(ExecutionData.create().withItemStack(stack).build());
        }
    }

    @Override
    public boolean canTakeItem( ItemStack itemstackIn )
    {
        if ( !this.hasItemInSlot(EquipmentSlot.MAINHAND) )
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
                        if(stack.isDamageableItem())
                        {
                            float percent = ( this.getRandom().nextFloat() * 0.8F ) + 0.1F;
                            int damage = (int) ( percent * stack.getMaxDamage() + stack.getDamageValue() );
                            int setDamage = Math.min(damage, (int) ( stack.getMaxDamage() * 0.9F) );
                            stack.setDamageValue(Math.max(stack.getDamageValue(), setDamage) );
                        }
                        EnchantmentHelper.setEnchantments(map, stack);
                        break;
                    }
                }
            } else if (stack.getItem() == Items.DIRT)
            {
                stack = new ItemStack(Items.MYCELIUM, stack.getCount());
            }

            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            this.setHolding(ItemStack.EMPTY);
            this.spawnAtLocation(stack);
        }
    }

    @Override
    public AnimationHandler<FriendlySporelingEntity> getAnimationHandler (String name)
    {
        return this.animationHandler;
    }

    static class WaveGoal
        extends LookAtPlayerGoal
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
        public boolean canUse()
        {
            boolean shouldExec = super.canUse();
            if ( shouldExec && this.waveTimer == 0 && this.sporeling.getRandom().nextInt( 9 ) == 0 )
            {
                this.waveTimer = 8;
            }
            else if ( this.waveTimer > 0 && this.lookAt != null )
            {
                this.sporeling.setWave( --this.waveTimer > 0 );
            }
            return shouldExec;
        }

        @Override
        public boolean canContinueToUse()
        {
            return super.canContinueToUse() && this.sporeling.getLookControl().isHasWanted();
        }
    }

    public static boolean canSporelingSpawn( EntityType<FriendlySporelingEntity> animal, LevelAccessor worldIn,
                                             MobSpawnType reason, BlockPos pos, Random randomIn )
    {
        return worldIn.getRawBrightness(pos, 0) > 8;
    }
}
