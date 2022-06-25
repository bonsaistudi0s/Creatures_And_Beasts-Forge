package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.blocks.LizardEggBlock;
import com.cgessinger.creaturesandbeasts.init.CNBBlocks;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBItems;
import com.cgessinger.creaturesandbeasts.init.CNBLizardTypes;
import com.cgessinger.creaturesandbeasts.util.LizardType;
import com.cgessinger.creaturesandbeasts.util.Netable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Optional;

public class LizardEntity extends Animal implements IAnimatable, Netable {
    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(LizardEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> PARTYING = SynchedEntityData.defineId(LizardEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SAD = SynchedEntityData.defineId(LizardEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(LizardEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(LizardEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_NET = SynchedEntityData.defineId(LizardEntity.class, EntityDataSerializers.BOOLEAN);

    private final AnimationFactory factory = new AnimationFactory(this);
    private LizardEntity partner;

    public BlockPos jukeboxPosition;
    int layEggCounter;

    public LizardEntity(EntityType<LizardEntity> type, Level worldIn) {
        super(type, worldIn);

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
        this.entityData.define(HAS_EGG, false);
        this.entityData.define(LAYING_EGG, false);
        this.entityData.define(FROM_NET, false);
        this.entityData.define(PARTYING, false);
        this.entityData.define(SAD, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("LizardType", this.getLizardType().getId().toString());
        compound.putBoolean("Sad", this.getSad());
        compound.putBoolean("FromNet", this.fromNet());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        LizardType type = LizardType.getById(compound.getString("LizardType"));
        if (type == null) {
            type = CNBLizardTypes.DESERT;
        }
        this.setLizardType(type);
        if (compound.contains("Sad")) {
            setSad(compound.getBoolean("Sad"));
        }

        if (compound.contains("FromNet")) {
            setFromNet(compound.getBoolean("FromNet"));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.18D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new LizardBreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LizardLayEggGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return !((LizardEntity) this.mob).isPartying() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !((LizardEntity) this.mob).isPartying() && super.canContinueToUse();
            }
        });
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }


    @Override
    public void aiStep() {
        super.aiStep();

        if (this.jukeboxPosition != null) {
            BlockEntity te = this.level.getBlockEntity(this.jukeboxPosition);
            Vec3 pos = this.position();
            if (!this.jukeboxPosition.closerThan(new Vec3i(pos.x, pos.y, pos.z), 10.0D) || !(te instanceof JukeboxBlockEntity)) {
                this.setPartying(false, null);
            }
        }

        if (this.isPartying() || this.entityData.get(LAYING_EGG)) {
            this.navigation.stop();
        }

        if (this.isAlive() && this.isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0) {
            BlockPos blockpos = this.blockPosition().below();
            this.level.levelEvent(2001, blockpos, Block.getId(this.level.getBlockState(blockpos)));
        }
    }

    public static boolean checkLizardSpawnRules(EntityType<LizardEntity> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return worldIn.getRawBrightness(pos, 0) > 8;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        Holder<Biome> biome = worldIn.getBiome(this.blockPosition());
        Optional<ResourceKey<Biome>> biomeKey = ForgeRegistries.BIOMES.getResourceKey(biome.get());

        if (reason == MobSpawnType.SPAWN_EGG && dataTag != null && dataTag.contains("LizardType")) {
            LizardType type = LizardType.getById(dataTag.getString("LizardType"));
            if (type != null) {
                this.setLizardType(type);
            }
        } else if (biomeKey.isPresent()) {
            if (biomeKey.get().equals(Biomes.DESERT) || biome.is(BiomeTags.IS_BADLANDS)) {
                if (random.nextBoolean()) {
                    this.setLizardType(CNBLizardTypes.DESERT);
                } else {
                    this.setLizardType(CNBLizardTypes.DESERT_2);
                }
            } else if (biome.is(BiomeTags.IS_JUNGLE)) {
                if (random.nextBoolean()) {
                    this.setLizardType(CNBLizardTypes.JUNGLE);
                } else {
                    this.setLizardType(CNBLizardTypes.JUNGLE_2);
                }
            }  else if (biomeKey.get().equals(Biomes.MUSHROOM_FIELDS)) {
                this.setLizardType(CNBLizardTypes.MUSHROOM);
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

        if (dataTag != null && dataTag.contains("Health")) {
            this.setHealth(dataTag.getFloat("Health"));
        }

        if (dataTag != null && dataTag.contains("Name")) {
            this.setCustomName(Component.nullToEmpty(dataTag.getString("Name")));
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void setRecordPlayingNearby(BlockPos pos, boolean isPartying) {
        this.setPartying(isPartying, pos);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        this.setPartying(false, null);
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);

        if (item.sameItem(CNBItems.APPLE_SLICE.get().getDefaultInstance()) && this.getSad()) {
            this.setSad(false);
            this.usePlayerItem(player, hand, item);
            spawnParticles(ParticleTypes.HEART);
            return InteractionResult.SUCCESS;
        }

        return Netable.netMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    public boolean fromNet() {
        return this.entityData.get(FROM_NET);
    }

    @Override
    public void setFromNet(boolean fromNet) {
        this.entityData.set(FROM_NET, fromNet);
    }

    @Override
    public void saveToNetTag(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();

        if (this.hasCustomName()) {
            stack.setHoverName(this.getCustomName());
        }
        if (this.isNoAi()) {
            tag.putBoolean("NoAI", this.isNoAi());
        }

        if (this.isSilent()) {
            tag.putBoolean("Silent", this.isSilent());
        }

        if (this.isNoGravity()) {
            tag.putBoolean("NoGravity", this.isNoGravity());
        }

        if (this.hasGlowingTag()) {
            tag.putBoolean("Glowing", this.hasGlowingTag());
        }

        if (this.isInvulnerable()) {
            tag.putBoolean("Invulnerable", this.isInvulnerable());
        }

        tag.putFloat("Health", this.getHealth());
        tag.putBoolean("Sad", this.getSad());
        tag.putBoolean("FromNet", true);
        tag.putString("LizardType", this.getLizardType().getId().toString());
    }

    @Override
    public void loadFromNetTag(CompoundTag compound) {
        if (compound.contains("NoAI")) {
            this.setNoAi(compound.getBoolean("NoAI"));
        }

        if (compound.contains("Silent")) {
            this.setSilent(compound.getBoolean("Silent"));
        }

        if (compound.contains("NoGravity")) {
            this.setNoGravity(compound.getBoolean("NoGravity"));
        }

        if (compound.contains("Glowing")) {
            this.setGlowingTag(compound.getBoolean("Glowing"));
        }

        if (compound.contains("Invulnerable")) {
            this.setInvulnerable(compound.getBoolean("Invulnerable"));
        }

        if (compound.contains("Health", 99)) {
            this.setHealth(compound.getFloat("Health"));
        }

        if (compound.contains("Sad")) {
            this.setSad(compound.getBoolean("Sad"));
        }

        if (compound.contains("LizardType")) {
            LizardType type = LizardType.getById(compound.getString("LizardType"));
            if (type != null) {
                this.setLizardType(type);
            }
        }

        if (compound.contains("FromNet")) {
            this.setFromNet(compound.getBoolean("FromNet"));
        }
    }

    @Override
    public ItemStack getItemStack() {
        if (!this.isBaby()) {
            return new ItemStack(this.getLizardType().getSpawnItem());
        }
        return null;
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.ITEM_PICKUP;
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
        if (!this.getSad()) {
            this.entityData.set(PARTYING, isPartying);
            this.jukeboxPosition = jukeboxPos;
        }
    }

    public boolean isPartying() {
        return this.entityData.get(PARTYING);
    }

    public void setSad(boolean sad) {
        this.entityData.set(SAD, sad);
    }

    public boolean getSad() {
        return this.entityData.get(SAD);
    }

    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    public boolean isLayingEgg() {
        return this.entityData.get(LAYING_EGG);
    }

    void setLayingEgg(boolean layingEgg) {
        this.layEggCounter = layingEgg ? 1 : 0;
        this.entityData.set(LAYING_EGG, layingEgg);
    }

    public boolean shouldLookAround() {
        return !this.isPartying() && !this.entityData.get(LAYING_EGG);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.sameItem(CNBItems.APPLE_SLICE.get().getDefaultInstance());
    }

    public void spawnParticles(ParticleOptions data) {
        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(data, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    public void setLizardType(LizardType lizardType) {
        this.entityData.set(TYPE, lizardType.getId().toString());
    }

    public LizardType getLizardType() {
        return LizardType.getById(this.entityData.get(TYPE));
    }

    @Override
    public int getMaxHeadYRot() {
        return 50;
    }

    @Override
    public int getMaxHeadXRot() {
        return 35;
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(this.getLizardType().getSpawnItem());
    }

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        if (this.entityData.get(LAYING_EGG)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("lizard_dig", true));
            return PlayState.CONTINUE;
        } else if (!(animationSpeed > -0.13F && animationSpeed < 0.13F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("lizard_walk", true));
            return PlayState.CONTINUE;
        } else if (this.isPartying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("lizard_dance", true));
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

    static class LizardBreedGoal extends BreedGoal {
        private final LizardEntity lizard;

        LizardBreedGoal(LizardEntity lizard, double speedModifier) {
            super(lizard, speedModifier);
            this.lizard = lizard;
        }

        public boolean canUse() {
            return super.canUse() && !this.lizard.hasEgg();
        }

        protected void breed() {
            ServerPlayer serverplayer = this.animal.getLoveCause();
            if (serverplayer == null && this.partner.getLoveCause() != null) {
                serverplayer = this.partner.getLoveCause();
            }

            if (serverplayer != null) {
                serverplayer.awardStat(Stats.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger(serverplayer, this.animal, this.partner, null);
            }

            this.lizard.setHasEgg(true);
            this.lizard.partner = (LizardEntity) this.partner;
            this.animal.resetLove();
            this.partner.resetLove();
            RandomSource random = this.animal.getRandom();
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                this.level.addFreshEntity(new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1));
            }

        }
    }

    static class LizardLayEggGoal extends MoveToBlockGoal {
        private final LizardEntity lizard;

        LizardLayEggGoal(LizardEntity lizard, double speedModifier) {
            super(lizard, speedModifier, 16);
            this.lizard = lizard;
        }

        public boolean canUse() {
            return this.lizard.hasEgg() && super.canUse();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.lizard.hasEgg();
        }

        @Override
        public void stop() {
            super.stop();
            this.lizard.setLayingEgg(false);
        }

        public void tick() {
            super.tick();
            BlockPos blockpos = this.lizard.blockPosition();
            if (!this.lizard.isInWater() && this.isReachedTarget()) {
                if (this.lizard.layEggCounter < 1) {
                    this.lizard.setLayingEgg(true);
                } else if (this.lizard.layEggCounter > this.adjustedTickDelay(200)) {
                    Level level = this.lizard.level;
                    level.playSound(null, blockpos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
                    level.setBlock(this.blockPos.above(), CNBBlocks.LIZARD_EGGS.get().defaultBlockState().setValue(LizardEggBlock.EGGS, this.lizard.random.nextInt(6) + 1), 3);

                    LizardEggBlock lizardEggBlock = (LizardEggBlock) level.getBlockState(this.blockPos.above()).getBlock();
                    lizardEggBlock.setParents(this.lizard.getLizardType(), this.lizard.partner.getLizardType());

                    this.lizard.setHasEgg(false);
                    this.lizard.setLayingEgg(false);
                    this.lizard.setInLoveTime(600);
                }

                if (this.lizard.isLayingEgg()) {
                    ++this.lizard.layEggCounter;
                }
            } else if (!this.isReachedTarget()) {
                this.lizard.setLayingEgg(false);
                this.moveMobToBlock();
            }
        }

        protected boolean isValidTarget(LevelReader levelReader, BlockPos pos) {
            return levelReader.isEmptyBlock(pos.above());
        }
    }
}
