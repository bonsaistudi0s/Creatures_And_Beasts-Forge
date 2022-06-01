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
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;
import java.util.UUID;

public class CactemEntity extends AgeableMob implements IAnimatable {
    private static final EntityDataAccessor<Boolean> ELDER = SynchedEntityData.defineId(CactemEntity.class, EntityDataSerializers.BOOLEAN);

    private final FollowElderGoal followElderGoal = new FollowElderGoal(this, 0.5D);
    private final TradeGoal tradeGoal = new TradeGoal(this, 16.0D, 0.5D);

    private final AnimationFactory factory = new AnimationFactory(this);
    private final UUID healthReductionUUID = UUID.fromString("65a301bb-531d-499e-939c-eda5b857c0b4");
    private final float babyHealth = 20.0F;

    public CactemEntity(EntityType<CactemEntity> entity, Level level) {
        super(entity, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D);
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
        }

        this.reassessGoals();

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroup, tag);
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
            List<? extends CactemEntity> list = this.cactem.level.getEntitiesOfClass(this.cactem.getClass(), this.cactem.getBoundingBox().inflate(HORIZONTAL_SCAN_RANGE, VERTICAL_SCAN_RANGE, HORIZONTAL_SCAN_RANGE));
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
}
