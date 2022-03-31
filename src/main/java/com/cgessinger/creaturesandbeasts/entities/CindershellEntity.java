package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBItems;
import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import com.cgessinger.creaturesandbeasts.util.AnimationHandler;
import com.cgessinger.creaturesandbeasts.util.IAnimationHolder;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class CindershellEntity extends Animal implements IAnimationHolder<CindershellEntity>, IAnimatable {
    public static final EntityDataAccessor<ItemStack> HOLDING = SynchedEntityData.defineId(CindershellEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Boolean> EAT = SynchedEntityData.defineId(CindershellEntity.class, EntityDataSerializers.BOOLEAN);
    private final UUID healthReductionUUID = UUID.fromString("189faad9-35de-4e15-a598-82d147b996d7");
    private final float babyHealth = 10.0F;
    private final AnimationHandler<CindershellEntity> animationHandler;
    private final AnimationFactory factory = new AnimationFactory(this);

    public CindershellEntity(EntityType<? extends Animal> type, Level worldIn) {
        super(type, worldIn);
        this.animationHandler = new AnimationHandler<>("eat_controller", this, 40, 1, 0, EAT);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100D);
    }

    public static boolean canCindershellSpawn(EntityType<CindershellEntity> p_234418_0_, LevelAccessor p_234418_1_, MobSpawnType p_234418_2_, BlockPos p_234418_3_, Random p_234418_4_) {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EAT, false);
        this.entityData.define(HOLDING, ItemStack.EMPTY);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D) {
            @Override
            protected void breed() {
                int range = this.animal.getRandom().nextInt(4) + 3;
                for (int i = 0; i <= range; i++) {
                    super.breed();
                }
            }
        });
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag) {
        if (dataTag != null) {
            if (dataTag.contains("age")) {
                this.setAge(dataTag.getInt("age"));
            }
            if (dataTag.contains("health")) {
                this.setHealth(dataTag.getFloat("health"));
            }
            if (dataTag.contains("name")) {
                this.setCustomName(Component.nullToEmpty(dataTag.getString("name")));
            }
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.animationHandler.process();
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
    public float getEyeHeight(Pose pose) {
        return this.getBbHeight() * 0.2F;
    }

    public ItemStack getHolding() {
        return this.entityData.get(HOLDING);
    }

    public void setHolding(ItemStack stack) {
        this.entityData.set(HOLDING, stack);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return CNBEntityTypes.CINDERSHELL.get().create(level);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return CNBSoundEvents.CINDERSHELL_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return CNBSoundEvents.CINDERSHELL_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return CNBSoundEvents.CINDERSHELL_HURT.get();
    }

    @Override
    protected float getSoundVolume() {
        return super.getSoundVolume() * 2;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public void checkDespawn() {
        if (!CNBConfig.ServerConfig.CINDERSHELL_CONFIG.shouldExist) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        super.checkDespawn();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    public InteractionResult tryStartEat(Player player, ItemStack stack) {
        if (stack.getItem() == Items.CRIMSON_FUNGUS || stack.getItem() == Items.WARPED_FUNGUS) {
            int i = this.getAge();
            if (!this.level.isClientSide && i == 0 && this.canFallInLove()) {
                this.usePlayerItem(player, player.getUsedItemHand(), stack);
                this.animationHandler.startAnimation(AnimationHandler.ExecutionData.create().withPlayer(player).build());
                this.playSound(CNBSoundEvents.CINDERSHELL_ADULT_EAT.get(), 1.2F, 1F);
                this.setHolding(stack);
                return InteractionResult.SUCCESS;
            }

            if (this.isBaby()) {
                this.playSound(CNBSoundEvents.CINDERSHELL_BABY_EAT.get(), 1.3F, 1F);
                this.usePlayerItem(player, player.getUsedItemHand(), stack);
                this.ageUp((int) (-i / 20F * 0.1F), true);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }

            if (this.level.isClientSide) {
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if (item.getItem() == Items.LAVA_BUCKET && this.isBaby()) {
            //spawnParticles( ParticleTypes.HEART );
            ItemStack stack = new ItemStack(CNBItems.CINDERSHELL_BUCKET.get(), item.getCount());
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putInt("age", this.getAge());
            nbt.putFloat("health", this.getHealth());
            if (this.hasCustomName()) {
                nbt.putString("name", this.getCustomName().getString());
            }

            player.setItemInHand(hand, stack);
            this.remove(RemovalReason.DISCARDED);
            return InteractionResult.SUCCESS;
        }

        return this.tryStartEat(player, item);
    }

    @Override
    public void executeBreakpoint(Optional<AnimationHandler.ExecutionData> data) {
        if (data.isPresent() && data.get().player != null) {
            this.setInLove(data.get().player);
            this.setHolding(ItemStack.EMPTY);
        }
    }

    @Override
    public AnimationHandler<CindershellEntity> getAnimationHandler(String name) {
        return this.animationHandler;
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
