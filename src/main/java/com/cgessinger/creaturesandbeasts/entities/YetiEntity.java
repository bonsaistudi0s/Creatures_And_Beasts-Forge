package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.config.CNBConfig.ServerConfig;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
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
import java.util.Random;
import java.util.UUID;

public class YetiEntity extends Animal implements IAnimatable, Enemy {
    public static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(YetiEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(YetiEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<ItemStack> HELD_ITEM = SynchedEntityData.defineId(YetiEntity.class, EntityDataSerializers.ITEM_STACK);
    private final AnimationFactory factory = new AnimationFactory(this);
    private final UUID healthReductionUUID = UUID.fromString("189faad9-35de-4e15-a598-82d147b996d7");
    private final float babyHealth = 20.0F;

    private int eatTimer;
    private boolean isPassive;

    public YetiEntity(EntityType<? extends Animal> type, Level worldIn) {
        super(type, worldIn);
        this.eatTimer = 0;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(EATING, false);
        this.entityData.define(HELD_ITEM, ItemStack.EMPTY);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("passive", this.isPassive);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("passive")) {
            this.isPassive = compound.getBoolean("passive");
        }
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 16.0D)
                .add(Attributes.ATTACK_SPEED, 0.1D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true) {
            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !((YetiEntity) this.mob).isPassive;
            }

            @Override
            public boolean canUse() {
                return super.canUse() && !((YetiEntity) this.mob).isPassive;
            }
        });
        this.goalSelector.addGoal(3, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.01F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new TargetPlayerGoal());
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.getEating()) {
            this.navigation.stop();
            this.eatTimer--;
        }

        if (this.eatTimer == 40) {
            if (this.isBaby()) {
                this.ageUp((int) (-this.getAge() / 20F * 0.1F), true);
            } else {
                this.setTarget(null);
                this.isPassive = true;
            }
            this.setHolding(ItemStack.EMPTY);
        } else if (this.eatTimer == 0) {
            this.setEating(false);
        }
    }

    public static boolean checkYetiSpawnRules(EntityType<YetiEntity> entity, LevelAccessor level, MobSpawnType mobSpawnType, BlockPos pos, Random random) {
        return random.nextDouble() >= ServerConfig.YETI_PROP.value;
    }

    @Override
    public void checkDespawn() {
        if (!CNBConfig.ServerConfig.YETI_CONFIG.shouldExist) {
            this.discard();
            return;
        }
        super.checkDespawn();
    }

    /*
     * Guarantees a baby to spawn alongside a Yeti
     */
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag) {
        if (spawnDataIn == null) {
            spawnDataIn = new AgeableMobGroupData(1.0F);
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);

        if (this.level.isClientSide) {
            return InteractionResult.CONSUME;
        }

        if (!(this.getEating() || this.entityData.get(ATTACKING))) {
            if (item.getItem() == Items.MELON_SLICE && !this.isPassive) {
                return this.startEat(player, item);
            } else if (item.getItem() == Items.SWEET_BERRIES) {
                if (this.canFallInLove()) {
                    this.setInLove(player);
                    return this.startEat(player, item);
                } else if (this.isBaby()) {
                    return this.startEat(player, item);
                }
            }
        }
        return InteractionResult.PASS;
    }

    /*
     * If a Yeti is a baby, apply the max health reduction to the yeti and set its health to the new max
     */
    @Override
    public void setAge(int age) {
        super.setAge(age);
        double MAX_HEALTH = this.getAttribute(Attributes.MAX_HEALTH).getValue();
        if (isBaby() && MAX_HEALTH > this.babyHealth) {
            Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
            multimap.put(Attributes.MAX_HEALTH, new AttributeModifier(this.healthReductionUUID, "yeti_health_reduction", this.babyHealth - MAX_HEALTH, AttributeModifier.Operation.ADDITION));
            this.getAttributes().addTransientAttributeModifiers(multimap);
            this.setHealth(this.babyHealth);
        }
    }

    /*
     * When a Yeti baby grows up, remove the max health debuff, maintain the same percentage of max health
     */
    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        float percentHealth = this.getHealth() / this.babyHealth;
        this.getAttribute(Attributes.MAX_HEALTH).removeModifier(this.healthReductionUUID);
        this.setHealth(percentHealth * (float) this.getAttribute(Attributes.MAX_HEALTH).getValue());
        this.setEating(false);
        this.setHolding(ItemStack.EMPTY);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return CNBEntityTypes.YETI.get().create(level);
    }

    public void setEating(boolean isEating) {
        this.eatTimer = isEating ? 60 : 0;
        this.entityData.set(EATING, isEating);
    }

    public boolean getEating() {
        return this.entityData.get(EATING);
    }

    public ItemStack getHolding() {
        return this.entityData.get(HELD_ITEM);
    }

    public void setHolding(ItemStack stack) {
        this.entityData.set(HELD_ITEM, stack);
    }

    private InteractionResult startEat(Player player, ItemStack stack) {
        this.setHolding(stack);
        this.usePlayerItem(player, player.getUsedItemHand(), stack);
        this.setEating(true);
        this.gameEvent(GameEvent.MOB_INTERACT, this.eyeBlockPosition());
        SoundEvent sound = this.isBaby() ? CNBSoundEvents.YETI_BABY_EAT.get() : CNBSoundEvents.YETI_ADULT_EAT.get();
        this.playSound(sound, 1.1F, 1F);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isBaby()) {
            List<YetiEntity> list = this.level.getEntitiesOfClass(YetiEntity.class, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));

            for (YetiEntity yeti : list) {
                if (!yeti.isBaby()) {
                    yeti.isPassive = false;
                    break;
                }
            }
        }
        this.isPassive = false;
        return super.hurt(source, amount);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        if (!blockIn.getMaterial().isLiquid()) {
            this.playSound(CNBSoundEvents.YETI_STEP.get(), this.getSoundVolume() * 0.3F, this.getVoicePitch());
        }
    }

    @Override
    public float getVoicePitch() {
        float pitch = super.getVoicePitch();
        return this.isBaby() ? pitch * 1.5F : pitch;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? null : CNBSoundEvents.YETI_AMBIENT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? null : CNBSoundEvents.YETI_HURT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return this.isBaby() ? null : CNBSoundEvents.YETI_HURT.get();
    }

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        if (this.getEating()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.isBaby() ? "yeti_baby.eat" : "yeti_adult.eat", false));
            return PlayState.CONTINUE;
        } else if (this.entityData.get(ATTACKING)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("yeti.attack", false));
            return PlayState.CONTINUE;
        } else if (!(animationSpeed > -0.15F && animationSpeed < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("yeti.walk", true));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("yeti.idle", false));
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> void soundListener(SoundKeyframeEvent<E> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        player.playSound(CNBSoundEvents.YETI_HIT.get(), 0.4F, 1F);
    }

    private <E extends IAnimatable> void particleListener(ParticleKeyFrameEvent<E> event) {
        ParticleEngine manager = Minecraft.getInstance().particleEngine;
        BlockPos pos = this.blockPosition();

        if ("hit.ground.particle".equals(event.effect)) {
            for (int x = pos.getX() - 1; x <= pos.getX() + 1; x++) {
                for (int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++) {
                    BlockPos newPos = new BlockPos(x, pos.getY() - 1, z);
                    manager.destroy(newPos, this.level.getBlockState(newPos));
                }
            }
        } else if ("eat.particle".equals(event.effect)) {
            spawnParticles(ParticleTypes.HAPPY_VILLAGER);
        }
    }

    public void spawnParticles(ParticleOptions data) {
        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(data, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController<YetiEntity> controller = new AnimationController<>(this, "controller", 0, this::animationPredicate);

        controller.registerSoundListener(this::soundListener);
        controller.registerParticleListener(this::particleListener);

        animationData.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


    class TargetPlayerGoal extends NearestAttackableTargetGoal<Player> {
        public TargetPlayerGoal() {
            super(YetiEntity.this, Player.class, 20, true, true, null);
        }

        @Override
        public boolean canUse() {
            if (!YetiEntity.this.isBaby() && super.canUse()) {
                for (YetiEntity yeti : YetiEntity.this.level.getEntitiesOfClass(YetiEntity.class, YetiEntity.this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D))) {
                    if (yeti.isBaby() && !YetiEntity.this.isPassive) {
                        return true;
                    }
                }

            }
            return false;
        }

        @Override
        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5D;
        }
    }
}
