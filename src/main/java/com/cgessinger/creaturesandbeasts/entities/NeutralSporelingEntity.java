package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.entities.ai.TimedAttackGoal;
import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

public class NeutralSporelingEntity extends AbstractSporelingEntity {
    public NeutralSporelingEntity(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    public static boolean canSporelingSpawn(EntityType<NeutralSporelingEntity> p_234418_0_, LevelAccessor worldIn, MobSpawnType p_234418_2_, BlockPos p_234418_3_, Random p_234418_4_) {
        return worldIn.getDifficulty() != Difficulty.PEACEFUL;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new TimedAttackGoal<>(this, 1.3D, false, 3));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        Optional<ResourceKey<Biome>> optional = level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getResourceKey(level.getBiome(this.blockPosition()).value());

        if (optional.isPresent() && optional.get() == Biomes.WARPED_FOREST) {
            this.setSporelingType(4);
        } else if (optional.isPresent() && optional.get() == Biomes.CRIMSON_FOREST) {
            this.setSporelingType(5);
        } else {
            this.setSporelingType(this.getRandom().nextInt(2) + 4);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        if (this.isAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sporeling.bite"));
            return PlayState.CONTINUE;
        }
        return super.animationPredicate(event);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CNBSoundEvents.SPORELING_WARPED_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return CNBSoundEvents.SPORELING_WARPED_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return CNBSoundEvents.SPORELING_WARPED_HURT.get();
    }

    @Override
    public void checkDespawn() {
        if (!CNBConfig.ServerConfig.NEUTRAL_SPORELING_CONFIG.shouldExist) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        super.checkDespawn();
    }
}
