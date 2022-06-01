package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.init.CNBItems;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
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
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class CactemEntity extends AgeableMob implements RangedAttackMob, IAnimatable {
    private static final EntityDataAccessor<Boolean> ELDER = SynchedEntityData.defineId(CactemEntity.class, EntityDataSerializers.BOOLEAN);

    private final FollowElderGoal followElderGoal = new FollowElderGoal(this, 0.5D);
    private final TradeGoal tradeGoal = new TradeGoal(this, 16.0D, 0.5D);
    private final RangedSpearAttackGoal spearAttackGoal = new RangedSpearAttackGoal(this, 0.5D, 20, 15.0F);
    private final HealGoal healGoal = new HealGoal(this, 1.0D, 60, 16.0F);

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
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("IsElder")) {
            this.setElder(tag.getBoolean("IsElder"));
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
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.5D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this).setAlertOthers());
    }

    private void reassessGoals() {
        if (this.isElder()) {
            this.goalSelector.addGoal(1, tradeGoal);
            this.goalSelector.addGoal(1, healGoal);
        } else if (!this.isBaby()){
            this.goalSelector.addGoal(1, spearAttackGoal);
            this.goalSelector.addGoal(1, followElderGoal);
        } else {
            this.goalSelector.addGoal(1, followElderGoal);
        }
    }

    @Override
    protected int getExperienceReward(Player player) {
        return 3 + this.level.random.nextInt(4);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroup, @Nullable CompoundTag tag) {
        double elderChance = level.getRandom().nextDouble();

        if (elderChance < 0.25) {
            this.setElder(true);
            this.setItemInHand(this.getUsedItemHand(), new ItemStack(CNBItems.HEAL_SPELL_BOOK.get()));
        }

        this.reassessGoals();

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroup, tag);
    }

    @Override
    public void performRangedAttack(LivingEntity entity, float damage) {

    }

    private void performHeal(float range) {
        List<? extends CactemEntity> list = this.level.getEntitiesOfClass(CactemEntity.class, this.getBoundingBox().inflate(range, 4, range));
        for(CactemEntity nearbyCactem : list) {
            nearbyCactem.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 5, 1));
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
        float percentHealth = this.getHealth() / this.babyHealth;
        this.getAttribute(Attributes.MAX_HEALTH).removeModifier(this.healthReductionUUID);
        this.setHealth(percentHealth * (float) this.getAttribute(Attributes.MAX_HEALTH).getValue());

        this.reassessGoals();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob entity) {
        return null;
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

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController<CactemEntity> controller = new AnimationController<>(this, "controller", 0, this::animationPredicate);

        animationData.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    static class FollowElderGoal extends Goal {
        public static final int HORIZONTAL_SCAN_RANGE = 16;
        public static final int VERTICAL_SCAN_RANGE = 4;
        public static final int DONT_FOLLOW_IF_CLOSER_THAN = 5;
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
                return !(d0 < 9.0D) && !(d0 > 256.0D);
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
                this.cactem.getNavigation().moveTo(this.elder, this.speedModifier);
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
            ItemStack returnItem;
            double lootChance = this.entityIn.random.nextDouble();

            if (lootChance < 0.2) {
                returnItem = new ItemStack(Items.EMERALD, 15 + this.entityIn.random.nextInt(10));
            } else if (lootChance < 0.7) {
                returnItem = new ItemStack(CNBItems.HEAL_SPELL_BOOK.get());
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
        private final float healRadius;
        private int healTime = -1;

        public HealGoal(CactemEntity cactem, double speedModifier, int healIntervalMin, float healRadius) {
            this.cactem = cactem;
            this.speedModifier = speedModifier;
            this.healIntervalMin = healIntervalMin;
            this.healRadius = healRadius;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return this.cactem.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return this.canUse() || !this.cactem.getNavigation().isDone();
        }

        public void start() {
            super.start();
            this.cactem.setAggressive(true);
        }

        public void stop() {
            super.stop();
            this.cactem.setAggressive(false);
            this.healTime = -1;
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
                    if (i >= 60) {
                        this.cactem.stopUsingItem();
                        this.cactem.performHeal(this.healRadius);
                        this.healTime = this.healIntervalMin + this.cactem.random.nextInt(41);
                    }
                } else if (--this.healTime <= 0) {
                    this.cactem.getNavigation().stop();
                    this.cactem.startUsingItem(this.cactem.getUsedItemHand());
                } else {
                    Vec3 vec3 = DefaultRandomPos.getPosAway(this.cactem, 16, 7, targetEntity.position());
                    if (vec3 != null) {
                        Path path = this.cactem.getNavigation().createPath(vec3.x, vec3.y, vec3.z, 0);
                        this.cactem.getNavigation().moveTo(path, this.speedModifier);
                    }
                }
            }
        }
    }

    static class RangedSpearAttackGoal extends Goal {
        private final CactemEntity cactem;
        private final double speedModifier;
        private final int attackIntervalMin;
        private final float attackRadiusSqr;
        private int attackTime = -1;
        private int seeTime;
        private boolean strafingClockwise;
        private boolean strafingBackwards;
        private int strafingTime = -1;

        public RangedSpearAttackGoal(CactemEntity cactem, double speedModifier, int attackIntervalMin, float attackRadius) {
            this.cactem = cactem;
            this.speedModifier = speedModifier;
            this.attackIntervalMin = attackIntervalMin;
            this.attackRadiusSqr = attackRadius * attackRadius;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return this.cactem.getTarget() != null;
        }

        public boolean canContinueToUse() {
            return (this.canUse() || !this.cactem.getNavigation().isDone());
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
                    } else if (d0 < (double)(this.attackRadiusSqr * 0.25F)) {
                        this.strafingBackwards = true;
                    }

                    this.cactem.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    this.cactem.lookAt(targetEntity, 30.0F, 30.0F);
                } else {
                    this.cactem.getLookControl().setLookAt(targetEntity, 30.0F, 30.0F);
                }

                if (this.cactem.isUsingItem()) {
                    if (!flag && this.seeTime < -60) {
                        this.cactem.stopUsingItem();
                    } else if (flag) {
                        int i = this.cactem.getTicksUsingItem();
                        if (i >= 20) {
                            this.cactem.stopUsingItem();
                            this.cactem.performRangedAttack(targetEntity, 1.0F);
                            this.attackTime = this.attackIntervalMin;
                        }
                    }
                } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                    this.cactem.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.cactem, item -> item instanceof BowItem));
                }

            }
        }
    }
}
