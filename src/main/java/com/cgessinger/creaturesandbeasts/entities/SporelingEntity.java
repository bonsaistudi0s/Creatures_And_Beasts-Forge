package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import com.cgessinger.creaturesandbeasts.init.CNBSporelingTypes;
import com.cgessinger.creaturesandbeasts.util.SporelingType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

import static com.cgessinger.creaturesandbeasts.util.SporelingType.SporelingHostility.FRIENDLY;
import static com.cgessinger.creaturesandbeasts.util.SporelingType.SporelingHostility.HOSTILE;
import static com.cgessinger.creaturesandbeasts.util.SporelingType.SporelingHostility.NEUTRAL;

public class SporelingEntity extends TamableAnimal implements Enemy, IAnimatable {
    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(SporelingEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(SporelingEntity.class, EntityDataSerializers.BOOLEAN);

    private final SporelingAttackGoal attackGoal = new SporelingAttackGoal(this, 1.3D, false);
    private final NearestAttackableTargetGoal<Player> nearestAttackableTargetGoal = new NearestAttackableTargetGoal<>(this, Player.class, true);
    private final HurtByTargetGoal hurtByTargetGoal = new HurtByTargetGoal(this);
    private final WaveGoal waveGoal = new WaveGoal(this, Player.class, 8.0F);
    private final PanicGoal panicGoal = new PanicGoal(this, 1.25D);

    private final AnimationFactory factory = new AnimationFactory(this);
    private int inspectTimer;
    private int attackTimer;

    public SporelingEntity(EntityType<SporelingEntity> entityType, Level level) {
        super(entityType, level);
        this.inspectTimer = 0;
        this.attackTimer = 0;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE, CNBSporelingTypes.RED_OVERWORLD.getId().toString());
        this.entityData.define(ATTACKING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("SporelingType", this.getSporelingType().getId().toString());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        SporelingType type = SporelingType.getById(compound.getString("SporelingType"));
        if (type == null) {
            type = CNBSporelingTypes.RED_OVERWORLD;
        }
        this.setSporelingType(type);
        this.reassessGoals();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));


    }

    private void reassessGoals() {
        if (this.getSporelingType().getHostility() == FRIENDLY) {
            this.goalSelector.addGoal(1, waveGoal);
            this.goalSelector.addGoal(6, panicGoal);
        } else if (this.getSporelingType().getHostility() == HOSTILE) {
            this.goalSelector.addGoal(2, attackGoal);
            this.targetSelector.addGoal(2, nearestAttackableTargetGoal);
        } else {
            this.goalSelector.addGoal(2, attackGoal);
            this.targetSelector.addGoal(1, hurtByTargetGoal);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isWaving()) {
            this.navigation.stop();
            this.getNavigation().setSpeedModifier(0);
        }

        if (this.isInspecting()) {
            ItemStack stack = this.getHolding();

            if (stack != null && stack != ItemStack.EMPTY) {
                if (stack.isEnchanted()) {
                    Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
                    for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                        if (entry.getKey().isCurse()) {
                            map.remove(entry.getKey(), entry.getValue());
                            if (stack.isDamageableItem()) {
                                float percent = (this.getRandom().nextFloat() * 0.8F) + 0.1F;
                                int damage = (int) (percent * stack.getMaxDamage() + stack.getDamageValue());
                                int setDamage = Math.min(damage, (int) (stack.getMaxDamage() * 0.9F));
                                stack.setDamageValue(Math.max(stack.getDamageValue(), setDamage));
                            }
                            EnchantmentHelper.setEnchantments(map, stack);
                            break;
                        }
                    }
                } else if (stack.getItem() == Items.DIRT) {
                    stack = new ItemStack(Items.MYCELIUM, stack.getCount());
                }

                this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                this.setHolding(ItemStack.EMPTY);
                this.spawnAtLocation(stack);
                this.inspectTimer--;
            }
        }

        if (this.attackTimer > 0) {
            this.navigation.stop();
            this.attackTimer--;
        } else {
            this.setAttacking(false);
        }
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        ItemStack stack = itemEntity.getItem();
        if (this.canTakeItem(stack) && !(this.isWaving() || this.isAttacking() || this.isInspecting())) {
            this.onItemPickup(itemEntity);
            this.setItemSlot(EquipmentSlot.MAINHAND, stack);
            this.setHolding(stack);
            this.setInspecting(true);
            itemEntity.discard();
        }
    }

    @Override
    public boolean canTakeItem(ItemStack itemstackIn) {
        if (!this.hasItemInSlot(EquipmentSlot.MAINHAND)) {
            if (itemstackIn.isEnchanted()) {
                for (Map.Entry<Enchantment, Integer> entry : EnchantmentHelper.getEnchantments(itemstackIn).entrySet()) {
                    if (entry.getKey().isCurse()) {
                        return true;
                    }
                }
            }
            return itemstackIn.getItem() == Items.DIRT;
        }
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.level.isClientSide) {
            boolean flag = this.isOwnedBy(player) || this.isTame() || itemstack.is(Items.BONE) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            if (this.isTame()) {
                InteractionResult interactionresult = super.mobInteract(player, hand);
                if (!interactionresult.consumesAction() && this.isOwnedBy(player)) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    this.jumping = false;
                    this.navigation.stop();
                    this.setTarget(null);
                    return InteractionResult.SUCCESS;
                }

                return interactionresult;
            } else if (this.getSporelingType().getHostility() == FRIENDLY && itemstack.is(Items.BONE_MEAL)) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (this.random.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.navigation.stop();
                    this.setTarget(null);
                    this.setOrderedToSit(true);
                    this.level.broadcastEntityEvent(this, (byte)7);
                } else {
                    this.level.broadcastEntityEvent(this, (byte)6);
                }

                return InteractionResult.SUCCESS;
            }

            return super.mobInteract(player, hand);
        }
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damage) {
        if (damageSource.isFire() && this.getSporelingType().getHostility() != FRIENDLY) {
            return;
        }
        super.actuallyHurt(damageSource, damage);
    }

    @Override
    public MobCategory getClassification(boolean forSpawnCount) {
        return this.getSporelingType().getHostility() == FRIENDLY ? MobCategory.CREATURE : MobCategory.MONSTER;
    }

    public static boolean checkSporelingSpawnRules(EntityType<SporelingEntity> entity, LevelAccessor worldIn, MobSpawnType mobSpawnType, BlockPos pos, Random rand) {
        if (Biome.getBiomeCategory(worldIn.getBiome(pos)).equals(Biome.BiomeCategory.NETHER)) {
            return worldIn.getDifficulty() != Difficulty.PEACEFUL;
        } else {
            return worldIn.getRawBrightness(pos, 0) > 8;
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        Holder<Biome> biome = worldIn.getBiome(this.blockPosition());
        ResourceKey<Biome> biomeKey = ResourceKey.create(Registry.BIOME_REGISTRY, biome.value().getRegistryName());
        Biome.BiomeCategory biomeCategory = Biome.getBiomeCategory(biome);

        if (reason == MobSpawnType.SPAWN_EGG && dataTag != null && dataTag.contains("EggType")) {
            String eggType = dataTag.getString("EggType");

            if (eggType.equals("Nether")) {
                if (biomeKey.equals(Biomes.CRIMSON_FOREST)) {
                    this.setSporelingType(CNBSporelingTypes.CRIMSON_FUNGUS);
                } else if (biomeKey.equals(Biomes.WARPED_FOREST)) {
                    this.setSporelingType(CNBSporelingTypes.WARPED_FUNGUS);
                } else {
                    if (random.nextBoolean()) {
                        this.setSporelingType(CNBSporelingTypes.RED_NETHER);
                    } else {
                        this.setSporelingType(CNBSporelingTypes.BROWN_NETHER);
                    }
                }
            } else {
                if (random.nextBoolean()) {
                    this.setSporelingType(CNBSporelingTypes.RED_OVERWORLD);
                } else {
                    this.setSporelingType(CNBSporelingTypes.BROWN_OVERWORLD);
                }
            }
        } else {
            if (biomeKey.equals(Biomes.CRIMSON_FOREST)) {
                this.setSporelingType(CNBSporelingTypes.CRIMSON_FUNGUS);
            } else if (biomeKey.equals(Biomes.WARPED_FOREST)) {
                this.setSporelingType(CNBSporelingTypes.WARPED_FUNGUS);
            } else if (biomeCategory.equals(Biome.BiomeCategory.NETHER)) {
                if (random.nextBoolean()) {
                    this.setSporelingType(CNBSporelingTypes.RED_NETHER);
                } else {
                    this.setSporelingType(CNBSporelingTypes.BROWN_NETHER);
                }
            } else {
                if (random.nextBoolean()) {
                    this.setSporelingType(CNBSporelingTypes.RED_OVERWORLD);
                } else {
                    this.setSporelingType(CNBSporelingTypes.BROWN_OVERWORLD);
                }
            }
        }

        this.reassessGoals();

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return super.createBodyControl();
    }

    public boolean isWaving() {
        return this.goalSelector.getRunningGoals().anyMatch(goal -> goal.getGoal() instanceof WaveGoal);
    }

    public boolean isRunning() {
        return this.getMoveControl().getSpeedModifier() >= 1.3D;
    }

    public void setAttacking(boolean isAttacking) {
        this.entityData.set(ATTACKING, isAttacking);
        this.attackTimer = isAttacking ? 30 : 0;
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public boolean isInspecting() {
        return this.inspectTimer > 0;
    }

    public void setInspecting(boolean isInspecting) {
        this.inspectTimer = isInspecting ? 40 : 0;
    }

    public ItemStack getHolding() {
        return this.getItemBySlot(EquipmentSlot.MAINHAND);
    }

    public void setHolding(ItemStack stack) {
        this.setItemSlot(EquipmentSlot.MAINHAND, stack);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        switch (this.getSporelingType().getHostility()) {
            case HOSTILE:
                return CNBSoundEvents.SPORELING_NETHER_HURT.get();
            case NEUTRAL:
                return CNBSoundEvents.SPORELING_WARPED_HURT.get();
            case FRIENDLY:
            default:
                return CNBSoundEvents.SPORELING_OVERWORLD_HURT.get();
        }
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        switch (this.getSporelingType().getHostility()) {
            case HOSTILE:
                return CNBSoundEvents.SPORELING_NETHER_HURT.get();
            case NEUTRAL:
                return CNBSoundEvents.SPORELING_WARPED_HURT.get();
            case FRIENDLY:
            default:
                return CNBSoundEvents.SPORELING_OVERWORLD_HURT.get();
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        switch (this.getSporelingType().getHostility()) {
            case HOSTILE:
                return CNBSoundEvents.SPORELING_NETHER_AMBIENT.get();
            case NEUTRAL:
                return CNBSoundEvents.SPORELING_WARPED_AMBIENT.get();
            case FRIENDLY:
            default:
                return CNBSoundEvents.SPORELING_OVERWORLD_AMBIENT.get();
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return this.getSporelingType().getHostility() == SporelingType.SporelingHostility.HOSTILE;
    }

    @Override
    public void checkDespawn() {
        SporelingType.SporelingHostility hostility = this.getSporelingType().getHostility();

        if (hostility == FRIENDLY && !CNBConfig.ServerConfig.FRIENDLY_SPORELING_CONFIG.shouldExist) {
            this.discard();
            return;
        } else if (hostility == NEUTRAL && !CNBConfig.ServerConfig.NEUTRAL_SPORELING_CONFIG.shouldExist) {
            this.discard();
            return;
        } else if (hostility == HOSTILE && !CNBConfig.ServerConfig.HOSTILE_SPORELING_CONFIG.shouldExist) {
            this.discard();
            return;
        }

        super.checkDespawn();
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        this.playSound(CNBSoundEvents.SPORELING_BITE.get(), this.getSoundVolume() * 2, this.getVoicePitch());
        return super.doHurtTarget(entityIn);
    }

    @Override
    public int getMaxHeadYRot() {
        return 5;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public void setSporelingType(SporelingType shroomloinType) {
        this.entityData.set(TYPE, shroomloinType.getId().toString());
    }

    public SporelingType getSporelingType() {
        return SporelingType.getById(this.entityData.get(TYPE));
    }

    public <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        if (!(animationSpeed > -0.15F && animationSpeed < 0.15F)) {
            if (this.isRunning()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("sporeling.run", true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("sporeling.walk", true));
            }
            return PlayState.CONTINUE;
        } else if (this.isAttacking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sporeling.bite", false));
            return PlayState.CONTINUE;
        } else if (this.isWaving() && this.getHolding() == ItemStack.EMPTY) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sporeling.wave", false));
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

    static class WaveGoal extends LookAtPlayerGoal {
        private final SporelingEntity sporeling;
        private int waveTimer;

        public WaveGoal(SporelingEntity entityIn, Class<? extends LivingEntity> watchTargetClass, float maxDistance) {
            super(entityIn, watchTargetClass, maxDistance);
            sporeling = entityIn;
            this.waveTimer = 0;
        }

        @Override
        public boolean canUse() {
            boolean shouldExec = super.canUse();
            if (shouldExec && this.waveTimer == 0 && this.sporeling.getRandom().nextInt(9) == 0) {
                this.waveTimer = 8;
            } else if (this.waveTimer > 0 && this.lookAt != null) {
                this.waveTimer--;
            }
            return shouldExec;
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.sporeling.getLookControl().isLookingAtTarget();
        }
    }

    static class SporelingAttackGoal extends MeleeAttackGoal {
        private final SporelingEntity goalOwner;

        public SporelingAttackGoal(SporelingEntity sporeling, double speedModifier, boolean followWithoutLineOfSight) {
            super(sporeling, speedModifier, followWithoutLineOfSight);
            this.goalOwner = sporeling;
        }

        @Override
        protected void checkAndPerformAttack(LivingEntity entity, double distance) {
            double d0 = this.getAttackReachSqr(entity);
            if (distance <= d0 && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                this.goalOwner.setAttacking(true);
                this.goalOwner.playSound(CNBSoundEvents.SPORELING_BITE.get(), 1.0F, 1.0F);
                this.goalOwner.doHurtTarget(entity);
            }
        }
    }
}
