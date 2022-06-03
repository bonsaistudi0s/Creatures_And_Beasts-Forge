package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBItems;
import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class CactemEntity extends AgeableMob implements RangedAttackMob, IAnimatable {
    private static final EntityDataAccessor<Boolean> ELDER = SynchedEntityData.defineId(CactemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(CactemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SPEAR_SHOWN = SynchedEntityData.defineId(CactemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HEALING = SynchedEntityData.defineId(CactemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> TRADING = SynchedEntityData.defineId(CactemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> IDLE_ANIM = SynchedEntityData.defineId(CactemEntity.class, EntityDataSerializers.INT);

    private final RandomStrollGoal elderStrollGoal = new RandomStrollGoal(this, 0.5D);
    private final RandomStrollGoal randomStrollGoal = new RandomStrollGoal(this, 1.0D);
    private final FollowElderGoal followElderGoal = new FollowElderGoal(this, 1.0D);
    private final TradeGoal tradeGoal = new TradeGoal(this, 16.0D, 0.5D);
    private final RangedSpearAttackGoal spearAttackGoal = new RangedSpearAttackGoal(this, 60, 16.0F);
    private final HealGoal healGoal = new HealGoal(this, 0.5D, 100, 160, 16.0F, 7.0F);

    private final AnimationFactory factory = new AnimationFactory(this);
    private final UUID healthReductionUUID = UUID.fromString("65a301bb-531d-499e-939c-eda5b857c0b4");
    private final float babyHealth = 20.0F;

    public CactemEntity(EntityType<CactemEntity> entity, Level level) {
        super(entity, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ELDER, false);
        this.entityData.define(ATTACKING, false);
        this.entityData.define(SPEAR_SHOWN, true);
        this.entityData.define(HEALING, false);
        this.entityData.define(TRADING, false);
        this.entityData.define(IDLE_ANIM, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("IsElder")) {
            this.setElder(tag.getBoolean("IsElder"));
        } else if (tag.contains("Age") && tag.getInt("Age") >= 0) {
            this.setItemInHand(this.getUsedItemHand(), new ItemStack(CNBItems.CACTEM_SPEAR.get()));
        }

        this.reassessGoals();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("IsElder", this.entityData.get(ELDER));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, CactemEntity.class).setAlertOthers());
    }

    private void reassessGoals() {
        this.goalSelector.removeGoal(elderStrollGoal);
        this.goalSelector.removeGoal(randomStrollGoal);
        this.goalSelector.removeGoal(spearAttackGoal);
        this.goalSelector.removeGoal(followElderGoal);
        this.goalSelector.removeGoal(tradeGoal);
        this.goalSelector.removeGoal(healGoal);

        if (this.isElder()) {
            this.goalSelector.addGoal(1, tradeGoal);
            this.goalSelector.addGoal(1, healGoal);
            this.goalSelector.addGoal(2, elderStrollGoal);
        } else if (!this.isBaby()){
            this.goalSelector.addGoal(1, spearAttackGoal);
            this.goalSelector.addGoal(1, followElderGoal);
            this.goalSelector.addGoal(2, randomStrollGoal);
        } else {
            this.goalSelector.addGoal(1, followElderGoal);
            this.goalSelector.addGoal(2, randomStrollGoal);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.isElder() && !this.getItemInHand(this.getUsedItemHand()).is(CNBItems.HEAL_SPELL_BOOK_1.get())) {
            this.setItemInHand(this.getUsedItemHand(), new ItemStack(CNBItems.HEAL_SPELL_BOOK_1.get()));
        }
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    protected int getExperienceReward(Player player) {
        return 3 + this.level.random.nextInt(4);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroup, @Nullable CompoundTag tag) {
        double elderChance = level.getRandom().nextDouble();

        if (!this.isBaby()) {
            if (elderChance < 0.25) {
                this.setElder(true);
                this.setItemInHand(this.getUsedItemHand(), new ItemStack(CNBItems.HEAL_SPELL_BOOK_1.get()));
            } else {
                this.setItemInHand(this.getUsedItemHand(), new ItemStack(CNBItems.CACTEM_SPEAR.get()));
                this.setIdleAnim(this.random.nextInt(2));
            }
        }

        this.reassessGoals();

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroup, tag);
    }

    @Override
    public void performRangedAttack(LivingEntity entity, float damage) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(this.getUsedItemHand()));
        ThrownCactemSpearEntity spearEntity = new ThrownCactemSpearEntity(this.level, this, itemstack);
        double d0 = entity.getX() - this.getX();
        double d1 = entity.getY(0.3333333333333333D) - spearEntity.getY();
        double d2 = entity.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        spearEntity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.level.addFreshEntity(spearEntity);
    }

    private void performHeal(float range) {
        List<? extends CactemEntity> list = this.level.getEntitiesOfClass(CactemEntity.class, this.getBoundingBox().inflate(range, 4, range));
        for(CactemEntity nearbyCactem : list) {
            nearbyCactem.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
        }
    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
        double MAX_HEALTH = this.getAttribute(Attributes.MAX_HEALTH).getValue();
        if (isBaby() && MAX_HEALTH > this.babyHealth) {
            Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
            multimap.put(Attributes.MAX_HEALTH, new AttributeModifier(this.healthReductionUUID, "cactem_health_reduction", this.babyHealth - MAX_HEALTH, AttributeModifier.Operation.ADDITION));
            this.getAttributes().addTransientAttributeModifiers(multimap);
            this.setHealth(this.babyHealth);
        }
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();

        double elderChance = this.random.nextDouble();

        if (!this.isBaby()) {
            if (elderChance < 0.25) {
                this.setElder(true);
                this.setItemInHand(this.getUsedItemHand(), new ItemStack(CNBItems.HEAL_SPELL_BOOK_1.get()));
            } else {
                this.setItemInHand(this.getUsedItemHand(), new ItemStack(CNBItems.CACTEM_SPEAR.get()));
            }
        }

        float percentHealth = this.getHealth() / this.babyHealth;
        this.getAttribute(Attributes.MAX_HEALTH).removeModifier(this.healthReductionUUID);
        this.setHealth(percentHealth * (float) this.getAttribute(Attributes.MAX_HEALTH).getValue());

        this.reassessGoals();
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5F;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob entity) {
        return CNBEntityTypes.CACTEM.get().create(level);
    }

    @Override
    public SoundEvent getAmbientSound() {
        return CNBSoundEvents.CACTEM_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return CNBSoundEvents.CACTEM_HURT.get();
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.75F : 1.0F;
    }

    public boolean isElder() {
        return this.entityData.get(ELDER);
    }

    public void setElder(boolean isElder) {
        this.entityData.set(ELDER, isElder);
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(boolean isAttacking) {
        this.entityData.set(ATTACKING, isAttacking);
    }

    public boolean isSpearShown() {
        return this.entityData.get(SPEAR_SHOWN);
    }

    public void setSpearShown(boolean isShown) {
        this.entityData.set(SPEAR_SHOWN, isShown);
    }

    public boolean isHealing() {
        return this.entityData.get(HEALING);
    }

    public void setHealing(boolean isHealing) {
        this.entityData.set(HEALING, isHealing);
    }

    public boolean isTrading() {
        return this.entityData.get(TRADING);
    }

    public void setTrading(boolean isTrading) {
        this.entityData.set(TRADING, isTrading);
    }

    public int getIdleAnim() {
        return this.entityData.get(IDLE_ANIM);
    }

    public void setIdleAnim(int anim) {
        switch (anim) {
            default:
            case 0:
                this.entityData.set(IDLE_ANIM, 0);
                break;
            case 1:
                this.entityData.set(IDLE_ANIM, 1);
                break;
        }
    }

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        if (this.isHealing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("cactem_elder_heal"));
        } else if (this.isTrading()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("cactem_admire"));
        } else if (!(animationSpeed > -0.075F && animationSpeed < 0.075F)) {
            if (this.isElder()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("cactem_elder_walk"));
            } else if (this.isBaby()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("cactem_baby_run"));
            } else if (this.isAttacking() || !this.isSpearShown()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("cactem_run_throw"));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("cactem_run"));
                this.setIdleAnim(this.random.nextInt(2));
            }
        } else {
            if (this.isElder()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("cactem_idle_2"));
            } else if (this.isBaby()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("cactem_idle"));
            } else {
                if (this.getIdleAnim() == 0) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("cactem_idle"));
                } else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("cactem_idle_2"));
                }
            }
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState attackAnimationPredicate(AnimationEvent<E> event) {
        Animation currentAnim = event.getController().getCurrentAnimation();

        if (this.isAttacking() || (currentAnim != null && currentAnim.animationName.equals("cactem_throw") && event.getController().getAnimationState().equals(AnimationState.Running))) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("cactem_throw"));
            return PlayState.CONTINUE;
        }

        event.getController().markNeedsReload();
        this.setSpearShown(true);
        return PlayState.STOP;
    }

    private <E extends IAnimatable> void soundListener(SoundKeyframeEvent<E> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (event.sound.equals("cactem_heal")) {
            player.playSound(CNBSoundEvents.CACTEM_HEAL.get(), 1.0F, 1.0F);
        } else if (event.sound.equals("spear_throw")) {
            player.playSound(CNBSoundEvents.SPEAR_THROW.get(), 1.0F, 1.0F);
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController<CactemEntity> controller = new AnimationController<>(this, "controller", 0, this::animationPredicate);
        AnimationController<CactemEntity> attackController = new AnimationController<>(this, "attackController", 0, this::attackAnimationPredicate);

        controller.registerSoundListener(this::soundListener);
        attackController.registerSoundListener(this::soundListener);

        animationData.addAnimationController(controller);
        animationData.addAnimationController(attackController);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    static class FollowElderGoal extends Goal {
        public static final int HORIZONTAL_SCAN_RANGE = 32;
        public static final int VERTICAL_SCAN_RANGE = 4;
        public static final int DONT_FOLLOW_IF_CLOSER_THAN = 20;
        private final CactemEntity cactem;
        @Nullable
        private CactemEntity elder;
        private final double speedModifier;
        private int timeToRecalcPath;

        public FollowElderGoal(CactemEntity cactem, double speedModifier) {
            this.cactem = cactem;
            this.speedModifier = speedModifier;
        }

        public boolean canUse() {
            List<? extends CactemEntity> list = this.cactem.level.getEntitiesOfClass(CactemEntity.class, this.cactem.getBoundingBox().inflate(HORIZONTAL_SCAN_RANGE, VERTICAL_SCAN_RANGE, HORIZONTAL_SCAN_RANGE));
            CactemEntity followTarget = null;
            double closestElderDistance = Double.MAX_VALUE;

            for(CactemEntity nearbyCactem : list) {
                if (nearbyCactem.isElder()) {
                    double distanceToCactem = this.cactem.distanceToSqr(nearbyCactem);
                    if (!(distanceToCactem > closestElderDistance)) {
                        closestElderDistance = distanceToCactem;
                        followTarget = nearbyCactem;
                    }
                }
            }

            if (followTarget == null) {
                return false;
            } else if (closestElderDistance < (DONT_FOLLOW_IF_CLOSER_THAN * DONT_FOLLOW_IF_CLOSER_THAN)) {
                return false;
            } else {
                this.elder = followTarget;
                return true;
            }
        }

        public boolean canContinueToUse() {
            if (!this.elder.isAlive()) {
                return false;
            } else {
                double d0 = this.cactem.distanceToSqr(this.elder);
                return !(d0 < 8.0D) && !(d0 > 256.0D);
            }
        }

        public void start() {
            this.timeToRecalcPath = 0;
        }

        public void stop() {
            this.elder = null;
        }

        public void tick() {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                Path path = this.cactem.getNavigation().createPath(this.elder, 8);
                this.cactem.getNavigation().moveTo(path, this.speedModifier);
            }
        }
    }

    static class TradeGoal extends Goal {
        protected Path path;
        protected ItemEntity itemInstance;
        protected double tradeTime;
        protected double tradeDelay;
        protected boolean trading;

        protected final double speed;
        protected final CactemEntity entityIn;
        protected final double range;
        protected final PathNavigation navigation;

        public TradeGoal(CactemEntity entityIn, double range, double speedIn) {
            this.entityIn = entityIn;
            this.range = range;
            this.speed = speedIn;
            this.navigation = entityIn.getNavigation();
        }

        @Override
        public boolean canUse() {
            if (this.itemInstance == null && this.entityIn.getTarget() == null) {
                List<ItemEntity> list = this.entityIn.level.getEntitiesOfClass(ItemEntity.class, this.entityIn.getBoundingBox().inflate(this.range, 3.0D, this.range));

                for (ItemEntity item : list) {
                    if (item.getItem().sameItem(Items.TOTEM_OF_UNDYING.getDefaultInstance())) {
                        this.path = this.navigation.createPath(item.getOnPos(), 0);
                        this.itemInstance = item;
                        return path != null;
                    }
                }
            }

            return false;
        }

        @Override
        public void stop() {
            this.itemInstance = null;
            this.path = null;
            this.navigation.stop();
            trading = false;
            this.entityIn.setTrading(false);
        }

        @Override
        public void start() {
            this.navigation.moveTo(this.path, this.speed);
        }

        @Override
        public boolean canContinueToUse() {
            return (!this.navigation.isDone() || this.tradeTime > 0) && (!this.itemInstance.isRemoved() || this.trading);
        }

        public void trade() {
            trading = false;
            this.entityIn.setTrading(false);
            ItemStack returnItem;
            double lootChance = this.entityIn.random.nextDouble();

            if (lootChance < 0.2) {
                returnItem = new ItemStack(Items.EMERALD, 15 + this.entityIn.random.nextInt(10));
            } else if (lootChance < 0.7) {
                returnItem = new ItemStack(CNBItems.HEAL_SPELL_BOOK_1.get());
            } else {
                returnItem = new ItemStack(Items.DEAD_BUSH);
            }

            entityIn.spawnAtLocation(returnItem);
            this.entityIn.playSound(SoundEvents.ITEM_PICKUP, 0.8F, 1.0F);
        }

        @Override
        public void tick() {
            if (tradeDelay <= 0) {
                if (this.entityIn.distanceToSqr(itemInstance) < 2.0D) {
                    this.navigation.setSpeedModifier(0.0D);

                    if (!trading) {
                        trading = true;
                        this.entityIn.setTrading(true);
                        itemInstance.getItem().shrink(1);
                        entityIn.lookAt(EntityAnchorArgument.Anchor.EYES, itemInstance.position());
                        this.tradeTime = 54;

                    } else {
                        if (--this.tradeTime <= 0) {
                            trade();
                            tradeDelay = 20;

                        } else if (tradeTime % 3 == 0) {
                            entityIn.lookAt(EntityAnchorArgument.Anchor.EYES, itemInstance.position());
                            entityIn.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemInstance.getItem()), entityIn.getRandomX(0.5F) + entityIn.getLookAngle().x / 2.0D, entityIn.getRandomY(), entityIn.getRandomZ(0.5F) + entityIn.getLookAngle().z / 2.0D, 4D, 0D, 0D);
                        }
                    }

                } else {
                    this.navigation.setSpeedModifier(speed);
                }

            } else {
                tradeDelay--;
            }
        }
    }

    static class HealGoal extends Goal {
        private final CactemEntity cactem;
        private final double speedModifier;
        private final int healIntervalMin;
        private final int healIntervalDiff;
        private final float healRadius;
        private final float avoidDist;
        private int healTime = -1;

        public HealGoal(CactemEntity cactem, double speedModifier, int healIntervalMin, int healIntervalMax, float healRadius, float avoidDist) {
            this.cactem = cactem;
            this.speedModifier = speedModifier;
            this.healIntervalMin = healIntervalMin;
            this.healIntervalDiff = healIntervalMax - healIntervalMin;
            this.healRadius = healRadius;
            this.avoidDist = avoidDist;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.cactem.getTarget();
            if (livingentity == null) {
                return false;
            } else {
                return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player) livingentity).isCreative();
            }
        }

        public boolean canContinueToUse() {
            return this.canUse();
        }

        public void start() {
            super.start();
            this.cactem.setAggressive(true);
        }

        public void stop() {
            super.stop();
            this.cactem.setAggressive(false);
            this.healTime = -1;
            this.cactem.setHealing(false);
            this.cactem.stopUsingItem();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity targetEntity = this.cactem.getTarget();

            if (targetEntity != null) {
                if (this.cactem.isUsingItem()) {
                    this.cactem.getNavigation().stop();
                    int i = this.cactem.getTicksUsingItem();
                    if (i >= 3 && i < 38) {
                        this.cactem.performHeal(this.healRadius);
                    } else if (i >= 38) {
                        this.cactem.setHealing(false);
                        this.cactem.stopUsingItem();
                        this.healTime = this.healIntervalMin + this.cactem.random.nextInt(this.healIntervalDiff + 1);
                    }
                } else if (--this.healTime <= 0 && this.cactemNeedsHeal(this.cactem, this.cactem.level)) {
                    this.cactem.getNavigation().stop();
                    this.cactem.setHealing(true);
                    this.cactem.startUsingItem(this.cactem.getUsedItemHand());
                } else if (!this.cactem.getNavigation().isInProgress() && this.cactem.distanceToSqr(this.cactem.getTarget()) <= (this.avoidDist * this.avoidDist)) {
                    Vec3 vec3 = DefaultRandomPos.getPosAway(this.cactem, (int) this.avoidDist, 7, targetEntity.position());
                    if (vec3 != null) {
                        Path path = this.cactem.getNavigation().createPath(vec3.x, vec3.y, vec3.z, 0);
                        this.cactem.getNavigation().moveTo(path, this.speedModifier);
                    }
                } else if (!this.cactem.getNavigation().isInProgress()) {
                    Vec3 vec3 = DefaultRandomPos.getPosTowards(this.cactem, (int) this.avoidDist, 7, targetEntity.position(), ((float)Math.PI / 2.0F));
                    if (vec3 != null) {
                        Path path = this.cactem.getNavigation().createPath(vec3.x, vec3.y, vec3.z, 0);
                        this.cactem.getNavigation().moveTo(path, this.speedModifier);
                    }
                }
            }
        }

        private boolean cactemNeedsHeal(CactemEntity elder, Level level) {
            List<? extends CactemEntity> list = level.getEntitiesOfClass(CactemEntity.class, elder.getBoundingBox().inflate(this.healRadius, 4, this.healRadius));
            for(CactemEntity nearbyCactem : list) {
                if (!nearbyCactem.isElder() && nearbyCactem.getHealth() / nearbyCactem.getMaxHealth() <= 0.5) {
                    return true;
                }
            }

            return false;
        }
    }

    static class RangedSpearAttackGoal extends Goal {
        private final CactemEntity cactem;
        private final int attackIntervalMin;
        private final float attackRadiusSqr;
        private int attackTime = -1;
        private int seeTime;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;

        public RangedSpearAttackGoal(CactemEntity cactem, int attackIntervalMin, float attackRadius) {
            this.cactem = cactem;
            this.attackIntervalMin = attackIntervalMin;
            this.attackRadiusSqr = attackRadius * attackRadius;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.cactem.getTarget();
            if (livingentity == null) {
                return false;
            } else {
                return !(livingentity instanceof Player) || !livingentity.isSpectator() && !((Player) livingentity).isCreative();
            }
        }

        public boolean canContinueToUse() {
                return this.canUse();
        }

        public void start() {
            super.start();
            this.cactem.setAggressive(true);
        }

        public void stop() {
            super.stop();
            this.cactem.setAggressive(false);
            this.seeTime = 0;
            this.attackTime = -1;
            this.cactem.setAttacking(false);
            this.cactem.stopUsingItem();
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity targetEntity = this.cactem.getTarget();
            if (targetEntity != null) {
                double d0 = this.cactem.distanceToSqr(targetEntity.getX(), targetEntity.getY(), targetEntity.getZ());
                boolean flag = this.cactem.getSensing().hasLineOfSight(targetEntity);
                boolean flag1 = this.seeTime > 0;
                if (flag != flag1) {
                    this.seeTime = 0;
                }

                if (flag) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                    this.cactem.getNavigation().stop();
                    ++this.strafingTime;
                }

                if (this.strafingTime >= 20) {
                    if ((double)this.cactem.getRandom().nextFloat() < 0.3D) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double)this.cactem.getRandom().nextFloat() < 0.3D) {
                        this.strafingBackwards = !this.strafingBackwards;
                    }

                    this.strafingTime = 0;
                }

                if (this.strafingTime > -1) {
                    if (d0 > (double)(this.attackRadiusSqr * 0.75F)) {
                        this.strafingBackwards = false;
                    } else if (d0 < (double)(this.attackRadiusSqr * 0.5F)) {
                        this.strafingBackwards = true;
                    }
                    if (!this.cactem.isUsingItem()) {
                        this.cactem.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    }
                    this.cactem.lookAt(targetEntity, 30.0F, 30.0F);
                } else {
                    this.cactem.getLookControl().setLookAt(targetEntity, 30.0F, 30.0F);
                }

                if (this.cactem.isUsingItem()) {
                    if (!flag && this.seeTime < -60) {
                        this.cactem.setAttacking(false);
                        this.cactem.stopUsingItem();
                    } else if (flag) {
                        int i = this.cactem.getTicksUsingItem();
                        if (i >= 6) {
                            this.cactem.setSpearShown(false);
                            this.cactem.setAttacking(false);
                            this.cactem.stopUsingItem();
                            this.cactem.performRangedAttack(targetEntity, 1.0F);
                            this.attackTime = this.attackIntervalMin;
                        }
                    }
                } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                    this.cactem.setAttacking(true);
                    this.cactem.startUsingItem(this.cactem.getUsedItemHand());
                }
            }
        }
    }
}
