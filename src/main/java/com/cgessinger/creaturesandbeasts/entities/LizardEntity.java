package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.blocks.LizardEggBlock;
import com.cgessinger.creaturesandbeasts.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.init.CNBBlocks;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBItems;
import com.cgessinger.creaturesandbeasts.init.CNBLizardTypes;
import com.cgessinger.creaturesandbeasts.items.AppleSliceItem;
import com.cgessinger.creaturesandbeasts.util.IModNetable;
import com.cgessinger.creaturesandbeasts.util.LizardType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Random;

public class LizardEntity extends Animal implements IAnimatable, IModNetable {
    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(LizardEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> LAY_EGG = SynchedEntityData.defineId(LizardEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimationFactory factory = new AnimationFactory(this);
    public BlockPos jukeboxPosition;
    private boolean isPartying;
    private boolean isSad;

    private int breedTimer;

    public LizardEntity(EntityType<? extends Animal> type, Level worldIn) {
        super(type, worldIn);
        this.breedTimer = 0;

        this.lookControl = new LookControl(this) {
            @Override
            public void tick() {
                LizardEntity lizard = (LizardEntity) this.mob;
                if (lizard.shouldLookAround()) {
                    super.tick();
                }
            }
        };

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE, CNBLizardTypes.DESERT.getId().toString());
        this.entityData.define(LAY_EGG, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("LizardType", this.getLizardType().getId().toString());
        compound.putBoolean("sad", this.isSad);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        LizardType type = LizardType.getById(compound.getString("LizardType"));
        if (type == null) {
            type = CNBLizardTypes.DESERT;
        }
        this.setLizardType(type);
        if (compound.contains("sad")) {
            setSad(compound.getBoolean("sad"));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return !((LizardEntity) this.mob).isPartying() && super.canUse();
            }
        });
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }


    @Override
    public void aiStep() {
        if (this.jukeboxPosition != null) {
            BlockEntity te = this.level.getBlockEntity(this.jukeboxPosition);
            Vec3 pos = this.position();
            if (!this.jukeboxPosition.closerThan(new Vec3i(pos.x, pos.y, pos.z), 10.0D) || !(te instanceof JukeboxBlockEntity) || ((JukeboxBlockEntity) te).getRecord() == ItemStack.EMPTY) {
                this.setPartying(false, null);
            }
        }

        if (this.isPartying() || this.entityData.get(LAY_EGG)) {
            this.navigation.stop();
            this.getNavigation().setSpeedModifier(0);
        }

        if (this.breedTimer > 0) {
            Level world = this.level;
            LizardEntity lizardMate = (LizardEntity) this;
            BlockState state = CNBBlocks.LIZARD_EGGS.get().defaultBlockState().setValue(LizardEggBlock.EGGS, this.random.nextInt(4) + 3);
            if (state.getBlock() instanceof LizardEggBlock eggBlock) {
                eggBlock.setParents(this.getLizardType(), lizardMate.getLizardType());
            }
            world.setBlock(this.blockPosition(), state, 3);
        }

        super.aiStep();

        if (this.isAlive() && this.entityData.get(LAY_EGG) && this.tickCount % 10 == 0) {
            BlockPos blockpos = this.blockPosition().below();
            this.level.levelEvent(2001, blockpos, Block.getId(this.level.getBlockState(blockpos)));
        }
    }

    public static boolean checkLizardSpawnRules(EntityType<LizardEntity> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, Random randomIn) {
        return worldIn.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        Holder<Biome> biome = worldIn.getBiome(this.blockPosition());
        ResourceKey<Biome> biomeKey = ResourceKey.create(Registry.BIOME_REGISTRY, biome.value().getRegistryName());
        Biome.BiomeCategory biomeCategory = Biome.getBiomeCategory(biome);

        if (reason == MobSpawnType.SPAWN_EGG && dataTag != null && dataTag.contains("LizardType")) {
            LizardType type = LizardType.getById(dataTag.getString("LizardType"));
            if (type != null) {
                this.setLizardType(type);
            }
        } else {
            if (biomeCategory.equals(Biome.BiomeCategory.DESERT) || biomeCategory.equals(Biome.BiomeCategory.MESA)) {
                if (random.nextBoolean()) {
                    this.setLizardType(CNBLizardTypes.DESERT);
                } else {
                    this.setLizardType(CNBLizardTypes.DESERT_2);
                }
            } else if (biomeCategory.equals(Biome.BiomeCategory.JUNGLE)) {
                if (random.nextBoolean()) {
                    this.setLizardType(CNBLizardTypes.JUNGLE);
                } else {
                    this.setLizardType(CNBLizardTypes.JUNGLE_2);
                }
            } else {
                switch (random.nextInt(4)) {
                    case 0:
                        this.setLizardType(CNBLizardTypes.DESERT);
                        break;
                    case 1:
                        this.setLizardType(CNBLizardTypes.DESERT_2);
                        break;
                    case 2:
                        this.setLizardType(CNBLizardTypes.JUNGLE);
                        break;
                    case 3:
                    default:
                        this.setLizardType(CNBLizardTypes.JUNGLE_2);
                        break;
                }
            }
        }

        // 1/10 chance to change variant to sad lizard variant
        this.setSad(this.getRandom().nextInt(10) == 0);

        if (dataTag != null && dataTag.contains("health")) {
            this.setHealth(dataTag.getFloat("health"));
        }

        if (dataTag != null && dataTag.contains("name")) {
            this.setCustomName(Component.nullToEmpty(dataTag.getString("name")));
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        this.setPartying(false, null);
    }

    @Override
    public void checkDespawn() {
        if (!CNBConfig.ServerConfig.LIZARD_CONFIG.shouldExist) {
            this.discard();
            return;
        }
        super.checkDespawn();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);

        if (item.getItem().equals(CNBItems.APPLE_SLICE.get()) && this.isSad()) {
            this.setSad(false);
            this.usePlayerItem(player, hand, item);
            spawnParticles(ParticleTypes.HEART);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        LizardEntity baby = CNBEntityTypes.LIZARD.get().create(world);
        if (baby != null) {
            baby.setLizardType(((LizardEntity) entity).getLizardType());
        }
        return baby;
    }

    public void setPartying(boolean isPartying, @Nullable BlockPos jukeboxPos) {
        if (!this.isSad()) {
            this.isPartying = isPartying;
            this.jukeboxPosition = jukeboxPos;
        }
    }

    public boolean isPartying() {
        return this.isPartying;
    }

    public void setSad(boolean sad) {
        this.isSad = sad;
    }

    public boolean isSad() {
        return this.isSad;
    }

    public boolean shouldLookAround() {
        return !this.isPartying() && !this.entityData.get(LAY_EGG);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() instanceof AppleSliceItem;
    }

    public void spawnParticles(ParticleOptions data) {
        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(data, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    public ItemStack getItem() {
        if (!this.isSad() && !this.isBaby()) {
            LizardType type = this.getLizardType();
            ItemStack stack = new ItemStack(type.getSpawnItem());
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putFloat("health", this.getHealth());
            if (this.hasCustomName()) {
                nbt.putString("name", this.getCustomName().getString());
            }
            return stack;
        }
        return null;
    }

    @Override
    public void spawnParticleFeedback() {
        spawnParticles(ParticleTypes.HAPPY_VILLAGER);
    }

    public void setLizardType(LizardType lizardType) {
        this.entityData.set(TYPE, lizardType.getId().toString());
    }

    public LizardType getLizardType() {
        return LizardType.getById(this.entityData.get(TYPE));
    }

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        if (this.entityData.get(LAY_EGG)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("lizard.dig", true));
            return PlayState.CONTINUE;
        } else if (!(animationSpeed > -0.15F && animationSpeed < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("lizard.walk", true));
            return PlayState.CONTINUE;
        } else if (this.isPartying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("lizard.dance", true));
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

}
