package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.entities.ai.GoToWaterGoal;
import com.cgessinger.creaturesandbeasts.entities.ai.MountAdultGoal;
import com.cgessinger.creaturesandbeasts.entities.ai.SmoothSwimGoal;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

import static com.cgessinger.creaturesandbeasts.init.CNBTags.Items.LITTLE_GREBE_FOOD;

public class LittleGrebeEntity extends Animal implements IAnimatable {
    private static final EntityDataAccessor<BlockPos> TRAVEL_POS = SynchedEntityData.defineId(LittleGrebeEntity.class, EntityDataSerializers.BLOCK_POS);
    private final UUID healthReductionUUID = UUID.fromString("189faad9-35de-4e15-a598-82d147b996d7");
    public float flapSpeed;
    private float nextFlap = 1.0F;
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public LittleGrebeEntity(EntityType<LittleGrebeEntity> type, Level worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MountAdultGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new SmoothSwimGoal(this));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(LITTLE_GREBE_FOOD), false));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(6, new LittleGrebeEntity.LittleGrebeRandomStrollGoal(this, 1.0D, 60, 1));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(9, new GoToWaterGoal(this, 0.8D));
    }

    public static boolean checkGrebeSpawnRules(EntityType<LittleGrebeEntity> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return worldIn.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        if (spawnDataIn == null) {
            spawnDataIn = new AgeableMobGroupData(0.6F);
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
        double MAX_HEALTH = this.getAttribute(Attributes.MAX_HEALTH).getValue();
        float babyHealth = 5.0F;
        if (isBaby() && MAX_HEALTH > babyHealth) {
            Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
            multimap.put(Attributes.MAX_HEALTH, new AttributeModifier(this.healthReductionUUID, "yeti_health_reduction", babyHealth - MAX_HEALTH, AttributeModifier.Operation.ADDITION));
            this.getAttributes().addTransientAttributeModifiers(multimap);
            this.setHealth(babyHealth);
        }
    }

    @Override
    protected void ageBoundaryReached() {
        this.stopRiding();
        this.getAttribute(Attributes.MAX_HEALTH).removeModifier(this.healthReductionUUID);
        this.setHealth((float) this.getAttribute(Attributes.MAX_HEALTH).getValue());
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.flapSpeed += (this.onGround ? -1.0F : 4.0F) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);

        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround && !this.isBaby() && vec3.y < 0.0D) {
            this.setDeltaMovement(vec3.multiply(1.0D, 0.6D, 1.0D));
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_241840_1_, AgeableMob p_241840_2_) {
        return CNBEntityTypes.LITTLE_GREBE.get().create(p_241840_1_);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return this.isBaby() && super.causeFallDamage(distance, damageMultiplier, source);
    }

    @Override
    protected float getSoundVolume() {
        return 0.6F;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isBaby()) {
            return CNBSoundEvents.LITTLE_GREBE_CHICK_AMBIENT.get();
        }
        return CNBSoundEvents.LITTLE_GREBE_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CNBSoundEvents.LITTLE_GREBE_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return CNBSoundEvents.LITTLE_GREBE_HURT.get();
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.3D;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    private BlockPos getTravelPos() {
        return this.entityData.get(TRAVEL_POS);
    }

    @Override
    public int getMaxHeadYRot() {
        return 60;
    }

    @Override
    public int getMaxHeadXRot() {
        return 35;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TRAVEL_POS, new BlockPos(0, 2, 0));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return Ingredient.of(LITTLE_GREBE_FOOD).test(stack);
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.1F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(travelVector);
        }

    }

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        if (!(this.isOnGround() || this.isInWater() || this.isBaby())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("little_grebe.fall", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else if (this.isInWater()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("little_grebe.swim", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        } else if (!(animationSpeed > -0.15F && animationSpeed < 0.15F)) {
            if (this.isBaby()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("little_grebe_chick.walk", ILoopType.EDefaultLoopTypes.LOOP));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("little_grebe.walk", ILoopType.EDefaultLoopTypes.LOOP));
            }
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    static class LittleGrebeRandomStrollGoal extends RandomStrollGoal {
        private static final Random rand = new Random();
        private final LittleGrebeEntity littleGrebe;
        private final int intervalLand;
        private final int intervalWater;
        private final boolean checkNoActionTime;

        public LittleGrebeRandomStrollGoal(LittleGrebeEntity littleGrebe, double speedModifier) {
            this(littleGrebe, speedModifier, 60, 120);
        }

        public LittleGrebeRandomStrollGoal(LittleGrebeEntity littleGrebe, double speedModifier, int intervalLand, int intervalWater) {
            this(littleGrebe, speedModifier, intervalLand, intervalWater, true);
        }

        public LittleGrebeRandomStrollGoal(LittleGrebeEntity littleGrebe, double speedModifier, int intervalLand, int intervalWater, boolean checkNoActionTime) {
            super(littleGrebe, speedModifier, intervalLand, checkNoActionTime);
            this.littleGrebe = littleGrebe;
            this.intervalLand = intervalLand;
            this.intervalWater = intervalWater;
            this.checkNoActionTime = checkNoActionTime;
        }

        @Override
        public void start() {
            this.littleGrebe.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
        }

        @Override
        public boolean canUse() {
            if (this.mob.isVehicle()) {
                return false;
            } else {
                if (!this.forceTrigger) {
                    if (this.checkNoActionTime && this.mob.getNoActionTime() >= 100) {
                        return false;
                    }

                    int i = this.littleGrebe.isInWater() ? this.intervalWater : this.intervalLand;
                    if (this.mob.getRandom().nextInt(reducedTickDelay(i)) != 0) {
                        return false;
                    }
                }

                Vec3 vec3 = this.getPosition();
                if (vec3 == null) {
                    return false;
                } else {
                    this.wantedX = vec3.x;
                    this.wantedY = vec3.y;
                    this.wantedZ = vec3.z;
                    this.forceTrigger = false;
                    return true;
                }
            }
        }

        @Override
        protected Vec3 getPosition() {
            boolean flag = GoalUtils.mobRestricted(this.littleGrebe, 10);

            return RandomPos.generateRandomPos(this.littleGrebe, () -> {
                BlockPos blockpos = this.littleGrebe.isInWater() ? RandomPos.generateRandomDirection(this.littleGrebe.getRandom(), 10, 1) : RandomPos.generateRandomDirection(this.littleGrebe.getRandom(), 10, 7);
                return generateRandomPosTowardDirection(this.littleGrebe, 10, flag, blockpos);
            });
        }

        @Nullable
        private static BlockPos generateRandomPosTowardDirection(LittleGrebeEntity littleGrebe, int horizontalRange, boolean flag, BlockPos posTowards) {
            BlockPos blockpos = RandomPos.generateRandomPosTowardDirection(littleGrebe, horizontalRange, littleGrebe.getRandom(), posTowards);
            return !GoalUtils.isOutsideLimits(blockpos, littleGrebe) && !GoalUtils.isRestricted(flag, littleGrebe, blockpos) && !GoalUtils.hasMalus(littleGrebe, blockpos) && (!GoalUtils.isNotStable(littleGrebe.getNavigation(), blockpos) || GoalUtils.isWater(littleGrebe, blockpos)) ? blockpos : null;
        }
    }
}
