package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.Random;

public class EndWhaleEntity extends TamableAnimal implements FlyingAnimal, Saddleable, IAnimatable {
    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(EndWhaleEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimationFactory factory = new AnimationFactory(this);

    public EndWhaleEntity(EntityType<EndWhaleEntity> entityType, Level level) {
        super(entityType, level);
        this.setTame(false);
        this.moveControl = new FlyingMoveControl(this, 2, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 45);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 160.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 100.0D)
                .add(Attributes.FLYING_SPEED, 1.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new EndWhaleTemptGoal(this, 1.25D, Ingredient.of(Items.CHORUS_FRUIT)));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SADDLED, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Saddled", this.isSaddled());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        boolean isSaddled = tag.getBoolean("Saddled");
        if (isSaddled) {
            this.equipSaddle(SoundSource.PLAYERS);
        }
    }

    @Override
    public boolean isSaddleable() {
        return this.isTame();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource soundSource) {
        this.entityData.set(SADDLED, true);
        this.playSound(SoundEvents.HORSE_SADDLE, 1.0F, 1.0F);
    }

    public void removeSaddle() {
        this.entityData.set(SADDLED, false);
        this.spawnAtLocation(Items.SADDLE);
        this.playSound(SoundEvents.HORSE_SADDLE, 0.8F, 1.0F);
    }

    @Override
    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

    private void mountWhale(Player player) {
        if (!this.level.isClientSide) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }
    }

    @Override
    public void positionRider(Entity rider) {
        if (this.hasPassenger(rider)) {
            double verticalOffset = this.getPassengersRidingOffset() + rider.getMyRidingOffset();
            float whaleRoll = this.getWhaleRoll(rider) * Mth.PI/180;
            float whalePitch = this.getWhalePitch(rider) * Mth.PI/180;
            rider.setPos(this.getX() + Mth.cos(this.getYRot() * Mth.PI/180) * verticalOffset * Mth.sin(whaleRoll) + Mth.sin(this.getYRot() * Mth.PI/180) * verticalOffset * Mth.sin(whalePitch),
                    this.getY() + verticalOffset * Mth.cos(whaleRoll) * Mth.cos(whalePitch),
                    this.getZ() + Mth.sin(this.getYRot() * Mth.PI/180) * verticalOffset * Mth.sin(whaleRoll) - Mth.cos(this.getYRot() * Mth.PI/180) * verticalOffset * Mth.sin(whalePitch));

            this.clampRotation(rider);
        }
    }

    protected void clampRotation(Entity rider) {
        rider.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(rider.getYRot() - this.getYRot());
        float f1 = Mth.clamp(f, -90.0F, 90.0F);
        rider.yRotO += f1 - f;
        rider.setYRot(rider.getYRot() + f1 - f);
        rider.setYHeadRot(rider.getYRot());
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double)this.getDimensions(this.getPose()).height * 0.70D;
    }

    private float getWhaleRoll(Entity rider) {
        float whaleRotY = this.getYRot();
        float riderRotY = rider.getYRot();

        return Mth.wrapDegrees(whaleRotY - riderRotY) / 2;
    }

    private float getWhalePitch(Entity rider) {
        float whaleRotY = this.getXRot();
        float riderRotY = rider.getXRot();

        return Mth.wrapDegrees(whaleRotY - riderRotY);
    }

    @Override
    public boolean canBeControlledByRider() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getFirstPassenger();
    }

    @Override
    public boolean rideableUnderWater() {
        return true;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isAlive()) {
            if (this.isVehicle() && this.canBeControlledByRider() && this.isSaddled()) {
                LivingEntity livingentity = (LivingEntity)this.getControllingPassenger();
                this.setYRot(Mth.rotLerp(0.05F, this.getYRot(), livingentity.getYRot()));
                this.yRotO = this.getYRot();
                this.setXRot(livingentity.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                float forwardMovement = livingentity.zza;
                if (forwardMovement <= 0.0F) {
                    forwardMovement *= 0.25F;
                }

                float verticalMovement = 0;

                if (Mth.abs(livingentity.getXRot()) > 7.0F) {
                    verticalMovement = Mth.rotLerp(0.01F, this.getXRot(), livingentity.getXRot()) * -forwardMovement/50;
                }

                this.flyingSpeed = this.getSpeed() * 0.1F;
                if (this.isControlledByLocalInstance()) {
                    this.setSpeed((float)this.getAttributeValue(Attributes.FLYING_SPEED));

                    Vec3 proposedMovement = new Vec3(0, verticalMovement, forwardMovement);

                    if (this.isInLava()) {
                        this.moveRelative(0.02F, proposedMovement);
                        this.move(MoverType.SELF, this.getDeltaMovement());
                        this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
                    } else {
                        BlockPos ground = new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ());
                        float f = 0.91F;
                        if (this.onGround) {
                            f = this.level.getBlockState(ground).getFriction(this.level, ground, this) * 0.91F;
                        }

                        float f1 = 0.16277137F / (f * f * f);

                        this.moveRelative(this.onGround ? 0.1F * f1 : 0.1F, proposedMovement);
                        this.move(MoverType.SELF, this.getDeltaMovement());
                        this.setDeltaMovement(this.getDeltaMovement().scale(f));
                    }
                } else if (livingentity instanceof Player) {
                    this.setDeltaMovement(Vec3.ZERO);
                }

                this.calculateEntityAnimation(this, false);
                this.tryCheckInsideBlocks();
            } else {
                this.flyingSpeed = 0.02F;

                if (this.isInLava()) {
                    this.moveRelative(0.02F, travelVector);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
                } else {
                    BlockPos ground = new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ());
                    float f = 0.91F;
                    if (this.onGround) {
                        f = this.level.getBlockState(ground).getFriction(this.level, ground, this) * 0.91F;
                    }

                    float f1 = 0.16277137F / (f * f * f);

                    this.moveRelative(this.onGround ? 0.1F * f1 : 0.02F, travelVector);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    this.setDeltaMovement(this.getDeltaMovement().scale(f));
                }

                this.calculateEntityAnimation(this, false);
            }
        }
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.level.isClientSide) {
            boolean flag = this.isOwnedBy(player) || this.isTame() || itemstack.is(Items.CHORUS_FRUIT) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else if (this.isSaddled() && player.isSecondaryUseActive()) {
            this.removeSaddle();
            return InteractionResult.CONSUME;
        } else if (this.isSaddled()) {
            this.mountWhale(player);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else if (!this.isTame()) {
            if (itemstack.is(Items.CHORUS_FRUIT)) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (this.random.nextInt(10) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setOrderedToSit(true);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    }

    @Override
    protected int getExperienceReward(Player player) {
        return 12 + this.level.random.nextInt(5);
    }

    public static boolean checkEndWhaleSpawnRules(EntityType<EndWhaleEntity> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, Random randomIn) {
        return true;
    }

    @Override
    public void checkDespawn() {
        if (!CNBConfig.ServerConfig.END_WHALE_CONFIG.shouldExist) {
            this.discard();
            return;
        }
        super.checkDespawn();
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float p_148750_, float p_148751_, DamageSource p_148752_) {
        return false;
    }

    @Override
    protected void checkFallDamage(double p_27754_, boolean p_27755_, BlockState p_27756_, BlockPos p_27757_) {
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(false);
        flyingpathnavigation.setCanPassDoors(false);
        return flyingpathnavigation;
    }

    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return CNBSoundEvents.END_WHALE_AMBIENT.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 800;
    }

    @Override
    protected float getSoundVolume() {
        return 5.0F;
    }

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("whale_fly"));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::animationPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    static class EndWhaleWanderGoal extends Goal {
        private final EndWhaleEntity endWhale;

        EndWhaleWanderGoal(EndWhaleEntity endWhale) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
            this.endWhale = endWhale;
        }

        public boolean canUse() {
            return this.endWhale.navigation.isDone() && this.endWhale.random.nextInt(3) == 0;
        }

        public boolean canContinueToUse() {
            return this.endWhale.navigation.isInProgress();
        }

        public void start() {
            Vec3 vec3 = this.findPos();
            if (vec3 != null) {
                this.endWhale.navigation.moveTo(this.endWhale.navigation.createPath(new BlockPos(vec3), 3), 1.0D);
            }

        }

        @Nullable
        private Vec3 findPos() {
            Vec3 vec3 = this.endWhale.getViewVector(0.5F);

            Vec3 vec32 = HoverRandomPos.getPos(this.endWhale, 20, 20, vec3.x, vec3.z, (float)Math.PI, 50, 15);
            return vec32 != null ? vec32 : AirAndWaterRandomPos.getPos(this.endWhale, 20, 20, -2, vec3.x, vec3.z, ((float)Math.PI));
        }
    }

    static class EndWhaleTemptGoal extends Goal {
        private static final TargetingConditions TEMP_TARGETING = TargetingConditions.forNonCombat().range(100.0D).ignoreLineOfSight();
        private final TargetingConditions targetingConditions;
        protected final EndWhaleEntity mob;
        private final double speedModifier;
        @Nullable
        protected Player player;
        private int calmDown;
        private final Ingredient items;

        public EndWhaleTemptGoal(EndWhaleEntity endWhale, double speedModifier, Ingredient temptIngredient) {
            this.mob = endWhale;
            this.speedModifier = speedModifier;
            this.items = temptIngredient;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            this.targetingConditions = TEMP_TARGETING.copy().selector(this::shouldFollow);
        }

        public boolean canUse() {
            if (this.calmDown > 0) {
                --this.calmDown;
                return false;
            } else {
                this.player = this.mob.level.getNearestPlayer(this.targetingConditions, this.mob);
                return this.player != null;
            }
        }

        private boolean shouldFollow(LivingEntity entity) {
            return this.items.test(entity.getMainHandItem()) || this.items.test(entity.getOffhandItem());
        }

        public boolean canContinueToUse() {
            return this.canUse();
        }

        public void stop() {
            this.player = null;
            this.mob.getNavigation().stop();
            this.calmDown = reducedTickDelay(100);
        }

        public void tick() {
            this.mob.getLookControl().setLookAt(this.player, (float)(this.mob.getMaxHeadYRot() + 20), (float)this.mob.getMaxHeadXRot());
            if (this.mob.distanceToSqr(this.player) < 6.25D) {
                this.mob.getNavigation().stop();
            } else {
                this.mob.getNavigation().moveTo(this.player, this.speedModifier);
            }
        }
    }
}
