package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.config.CNBConfig.ServerConfig;
import com.cgessinger.creaturesandbeasts.entities.ai.AnimatedAttackGoal;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import com.cgessinger.creaturesandbeasts.util.IAnimationHolder;
import com.cgessinger.creaturesandbeasts.util.AnimationHandler;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
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
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

public class YetiEntity extends Animal implements IAnimatable, IAnimationHolder<YetiEntity>, Enemy {
    public static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(YetiEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> EAT = SynchedEntityData.defineId(YetiEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<ItemStack> HOLDING = SynchedEntityData.defineId(YetiEntity.class, EntityDataSerializers.ITEM_STACK);
    private final AnimationFactory factory = new AnimationFactory(this);
    private final UUID healthReductionUUID = UUID.fromString("189faad9-35de-4e15-a598-82d147b996d7");
    private final float babyHealth = 20.0F;
    public AnimationHandler<YetiEntity> attackHandler;

    public AnimationHandler<YetiEntity> eatHandler;

    public boolean isPassive;

    public YetiEntity(EntityType<? extends Animal> type, Level worldIn) {
        super(type, worldIn);
        this.attackHandler = new AnimationHandler<>("attack_controller", this, 35, 17, 5, ATTACKING);
        this.eatHandler = new AnimationHandler<>("breed_controller", this, 40, 10, 20, EAT);
    }

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(CNBEntityTypes.YETI.get(), Attributes.MAX_HEALTH, 80.0D);
        event.add(CNBEntityTypes.YETI.get(), Attributes.MOVEMENT_SPEED, 0.3D);
        event.add(CNBEntityTypes.YETI.get(), Attributes.ATTACK_DAMAGE, 16.0D);
        event.add(CNBEntityTypes.YETI.get(), Attributes.ATTACK_SPEED, 0.1D);
        event.add(CNBEntityTypes.YETI.get(), Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }

    public static boolean canYetiSpawn(EntityType<? extends Animal> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, Random random) {
        return random.nextDouble() >= ServerConfig.YETI_PROP.value;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag) {

        if (spawnDataIn == null) {
            spawnDataIn = new AgeableMobGroupData(1.0F);
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(EAT, false);
        this.entityData.define(HOLDING, ItemStack.EMPTY);
    }

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

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        this.getAttribute(Attributes.MAX_HEALTH).removeModifier(this.healthReductionUUID);
        this.setHealth((float) this.getAttribute(Attributes.MAX_HEALTH).getValue());
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

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        if (this.entityData.get(EAT)) {
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

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return CNBEntityTypes.YETI.get().create(p_241840_1_);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new AnimatedAttackGoal<YetiEntity>(this, 1.2D, true) {
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
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new YetiEntity.AttackPlayerGoal());
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController<YetiEntity> controller = new AnimationController<>(this, "controller", 0, this::animationPredicate);

        controller.registerSoundListener(this::soundListener);
        controller.registerParticleListener(this::particleListener);

        animationData.addAnimationController(controller);
    }

    public boolean isEating() {
        return this.entityData.get(EAT);
    }

    @Override
    public AnimationHandler<YetiEntity> getAnimationHandler(String name) {
        if (name.equals("attack_controller")) {
            return this.attackHandler;
        }

        return this.eatHandler;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.entityData.get(EAT)) {
            this.navigation.stop();
        }

        this.attackHandler.process();
        this.eatHandler.process();
    }

    @Override
    public Optional<AnimationHandler.ExecutionData> onAnimationInit(Optional<AnimationHandler.ExecutionData> data) {
        this.getNavigation().stop();
        return IAnimationHolder.super.onAnimationInit(data);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public void executeAttack() {
        for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3.0D, 2.0D, 3.0D))) {
            if (!(entity instanceof YetiEntity)) {
                this.doHurtTarget(entity);
            }
        }
    }

    @Override
    public void executeBreakpoint(Optional<AnimationHandler.ExecutionData> data) {
        if (data.isPresent()) {
            AnimationHandler.ExecutionData execData = data.get();
            if (execData.name.equals(this.attackHandler.name)) {
                this.executeAttack();
            } else if (execData.name.equals(this.eatHandler.name)) {
                if (this.isBaby()) {
                    this.ageUp((int) (-this.getAge() / 20F * 0.1F), true);
                } else if (this.getHolding().getItem() == Items.SWEET_BERRIES) {
                    this.setInLove(execData.player);
                } else {
                    this.setTarget(null);
                    this.isPassive = true;
                }
                this.setHolding(ItemStack.EMPTY);
            }
        }
    }

    public ItemStack getHolding() {
        return this.entityData.get(HOLDING);
    }

    public void setHolding(ItemStack stack) {
        this.entityData.set(HOLDING, stack);
    }

    public InteractionResult tryStartEat(Player player, ItemStack stack) {
        if (this.level.isClientSide) return InteractionResult.CONSUME;

        if (this.eatHandler.canStart()) {
            if (stack.getItem() == Items.MELON_SLICE && !this.isPassive) {
                return this.startEat(player, stack);
            } else if (stack.getItem() == Items.SWEET_BERRIES) {
                if ((this.getAge() == 0 && this.canFallInLove()) || this.isBaby()) {
                    return this.startEat(player, stack);
                }
            }
        }
        return InteractionResult.PASS;
    }

    private InteractionResult startEat(Player player, ItemStack stack) {
        this.setHolding(stack);
        this.usePlayerItem(player, player.getUsedItemHand(), stack);
        this.eatHandler.startAnimation(AnimationHandler.ExecutionData.create().withPlayer(player).build());
        SoundEvent sound = this.isBaby() ? CNBSoundEvents.YETI_BABY_EAT.get() : CNBSoundEvents.YETI_ADULT_EAT.get();
        this.playSound(sound, 1.1F, 1F);
        return InteractionResult.SUCCESS;

    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) // on right click
    {
        super.mobInteract(player, hand);

        ItemStack item = player.getItemInHand(hand);
        return this.tryStartEat(player, item);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
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

    @Override
    public void checkDespawn() {
        if (!CNBConfig.ServerConfig.YETI_CONFIG.shouldExist) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        super.checkDespawn();
    }

    class AttackPlayerGoal extends NearestAttackableTargetGoal<Player> {
        public AttackPlayerGoal() {
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
