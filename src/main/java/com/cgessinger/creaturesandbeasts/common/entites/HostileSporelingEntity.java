package com.cgessinger.creaturesandbeasts.common.entites;

import com.cgessinger.creaturesandbeasts.common.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.common.goals.TimedAttackGoal;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class HostileSporelingEntity
    extends AbstractSporelingEntity
    implements Enemy
{
    public HostileSporelingEntity( EntityType<? extends PathfinderMob> type, Level worldIn )
    {
        super( type, worldIn );
    }

    @Override
    protected void registerGoals()
    {
        super.registerGoals();
        this.goalSelector.addGoal( 2, new TimedAttackGoal<>( this, 1.3D, false, 30 ) );
        this.targetSelector.addGoal( 2, new NearestAttackableTargetGoal<>( this, Player.class, true ) );
    }

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event)
    {
        event.add(ModEntityTypes.HOSTILE_SPORELING.get(), Attributes.MAX_HEALTH, 16.0D);
        event.add(ModEntityTypes.HOSTILE_SPORELING.get(), Attributes.MOVEMENT_SPEED, 0.2D);
        event.add(ModEntityTypes.HOSTILE_SPORELING.get(), Attributes.FOLLOW_RANGE, 35.0D);
        event.add(ModEntityTypes.HOSTILE_SPORELING.get(), Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn( ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason,
                                             @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag )
    {
        this.setSporelingType( this.getRandom().nextInt( 2 ) + 2 );
        return super.finalizeSpawn( worldIn, difficultyIn, reason, spawnDataIn, dataTag );
    }

    @Override
    public <E extends IAnimatable> PlayState animationPredicate( AnimationEvent<E> event )
    {
        if ( this.isAttacking() )
        {
            event.getController().setAnimation( new AnimationBuilder().addAnimation( "sporeling.bite" ) );
            return PlayState.CONTINUE;
        }
        return super.animationPredicate( event );
    }

    public static boolean canSporelingSpawn( EntityType<HostileSporelingEntity> p_234418_0_, LevelAccessor worldIn,
                                             MobSpawnType p_234418_2_, BlockPos p_234418_3_, Random p_234418_4_ )
    {
        Optional<ResourceKey<Biome>> optional =
            worldIn.registryAccess().registryOrThrow( Registry.BIOME_REGISTRY ).getResourceKey( worldIn.getBiome( p_234418_3_ ).value() );

        return worldIn.getDifficulty() != Difficulty.PEACEFUL && ( optional.isPresent()
                        && optional.get() != Biomes.WARPED_FOREST && optional.get() != Biomes.CRIMSON_FOREST );
    }

    @Override
    protected boolean shouldDespawnInPeaceful()
    {
        return true;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound( DamageSource damageSourceIn )
    {
        return ModSoundEventTypes.SPORELING_NETHER_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound()
    {
        return ModSoundEventTypes.SPORELING_NETHER_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound()
    {
        return ModSoundEventTypes.SPORELING_NETHER_HURT.get();
    }

    @Override
    public void checkDespawn()
    {
        if ( !CNBConfig.ServerConfig.HOSTILE_SPORELING_CONFIG.shouldExist )
        {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        super.checkDespawn();
    }
}
