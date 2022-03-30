package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.blocks.LizardEggBlock;
import com.cgessinger.creaturesandbeasts.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.entities.ai.AnimatedBreedGoal;
import com.cgessinger.creaturesandbeasts.init.CNBBlocks;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBItems;
import com.cgessinger.creaturesandbeasts.util.IAnimationHolder;
import com.cgessinger.creaturesandbeasts.util.IModNetable;
import com.cgessinger.creaturesandbeasts.items.AppleSliceItem;
import com.cgessinger.creaturesandbeasts.util.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class LizardEntity extends Animal implements IAnimatable, IModNetable, IAnimationHolder<LizardEntity> {
    private static final EntityDataAccessor<Boolean> PARTYING = SynchedEntityData.defineId(LizardEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SAD = SynchedEntityData.defineId(LizardEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> LIZARD_VARIANT = SynchedEntityData.defineId(LizardEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> LAY_EGG = SynchedEntityData.defineId(LizardEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimationFactory factory = new AnimationFactory(this);
    private final AnimationHandler<LizardEntity> animationHandler;
    public BlockPos jukeboxPosition;

    public LizardEntity(EntityType<? extends Animal> type, Level worldIn) {
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
        this.animationHandler = new AnimationHandler<>("breed_controller", this, 110, 1, 0, LAY_EGG);
    }

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(CNBEntityTypes.LIZARD.get(), Attributes.MAX_HEALTH, 12.0D);
        event.add(CNBEntityTypes.LIZARD.get(), Attributes.MOVEMENT_SPEED, 0.4D);
    }

    public static boolean canLizardSpawn(EntityType<LizardEntity> animal, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, Random randomIn) {
        return worldIn.getRawBrightness(pos, 0) > 8;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LIZARD_VARIANT, 0);
        this.entityData.define(PARTYING, false);
        this.entityData.define(SAD, false);
        this.entityData.define(LAY_EGG, false);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        int variant;
        boolean forceNotSad = false;
        if (dataTag != null && dataTag.contains("variant")) {
            variant = dataTag.getInt("variant");
            forceNotSad = true;
        } else {
            Biome.BiomeCategory biomeCategory = Biome.getBiomeCategory(worldIn.getBiome(this.blockPosition()));
            variant = getLizardTypeFromBiome(biomeCategory);
        }

        setVariant(variant);
        // 1/10 chance to change variant to sad lizard variant
        this.setSad(!forceNotSad && this.getRandom().nextInt(10) == 1);

        if (dataTag != null && dataTag.contains("health")) {
            this.setHealth(dataTag.getFloat("health"));
        }

        if (dataTag != null && dataTag.contains("name")) {
            this.setCustomName(Component.nullToEmpty(dataTag.getString("name")));
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
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

        super.aiStep();

        this.animationHandler.process();

        if (this.isAlive() && this.entityData.get(LAY_EGG) && this.tickCount % 10 == 0) {
            BlockPos blockpos = this.blockPosition().below();
            this.level.levelEvent(2001, blockpos, Block.getId(this.level.getBlockState(blockpos)));
        }
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
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        this.setPartying(false, null);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<LizardEntity>(this, "controller", 0, this::animationPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) // on right click
    {
        InteractionResult result = super.mobInteract(player, hand);
        ItemStack item = player.getItemInHand(hand);
        if (item.getItem() instanceof AppleSliceItem && this.isSad()) {
            this.setSad(false);
            item.shrink(player.getAbilities().instabuild ? 0 : 1);
            spawnParticles(ParticleTypes.HEART);
            return InteractionResult.SUCCESS;
        }
        return result;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new AnimatedBreedGoal<LizardEntity>(this, 1.0D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return !((LizardEntity) this.mob).isPartying() && super.canUse();
            }
        });
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel world, AgeableMob entity) {
        LizardEntity baby = CNBEntityTypes.LIZARD.get().create(world);
        baby.setVariant(((LizardEntity) entity).getVariant());
        return baby;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("variant", getVariant());
        compound.putBoolean("sad", isSad());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("variant")) {
            setVariant(compound.getInt("variant"));
        }
        if (compound.contains("sad")) {
            setSad(compound.getBoolean("sad"));
        }
    }

    public LizardType getLizardType() {
        return LizardType.values()[this.getVariant()];
    }

    public int getLizardTypeFromBiome(Biome.BiomeCategory biomeCategory) {
        switch (biomeCategory) {
            case DESERT:
            case MESA:
                return this.getRandom().nextInt(2);
            case JUNGLE:
                return this.getRandom().nextInt(2) + 2;
            default:
                return this.getRandom().nextInt(4);
        }
    }

    public int getVariant() {
        return Mth.clamp(this.entityData.get(LIZARD_VARIANT), 0, LizardType.values().length);
    }

    public void setVariant(int variant) {
        this.entityData.set(LIZARD_VARIANT, variant);
    }

    public void setPartying(boolean isPartying, BlockPos jukeboxPos) {
        if (!this.isSad()) {
            this.entityData.set(PARTYING, isPartying);
            this.jukeboxPosition = jukeboxPos;
        }
    }

    public boolean isPartying() {
        return this.entityData.get(PARTYING);
    }

    public boolean isSad() {
        return this.entityData.get(SAD);
    }

    public void setSad(boolean sad) {
        this.entityData.set(SAD, sad);
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

    @Override
    public ItemStack getItem() {
        if (!this.isSad() && !this.isBaby()) {
            LizardType type = this.getLizardType();
            ItemStack stack = new ItemStack(type.getItem());
            CompoundTag nbt = stack.getOrCreateTag();
            nbt.putInt("variant", Arrays.asList(LizardType.values()).indexOf(type));
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

    @Override
    public void checkDespawn() {
        if (!CNBConfig.ServerConfig.LIZARD_CONFIG.shouldExist) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        super.checkDespawn();
    }

    @Override
    public void executeBreakpoint(Optional<AnimationHandler.ExecutionData> data) {
        if (data.isPresent() && data.get().isBreedData) {
            ServerLevel world = data.get().world;
            LizardEntity lizardMate = (LizardEntity) data.get().entity;
            BlockState state = CNBBlocks.LIZARD_EGGS.get().defaultBlockState().setValue(LizardEggBlock.EGGS, Integer.valueOf(this.random.nextInt(4) + 3)).setValue(LizardEggBlock.VARIANT_0, this.getVariant()).setValue(LizardEggBlock.VARIANT_1, lizardMate.getVariant());

            world.setBlock(this.blockPosition(), state, 3);
        }
    }

    @Override
    public AnimationHandler<LizardEntity> getAnimationHandler(String name) {
        return this.animationHandler;
    }

    public enum LizardType {
        DESERT_1(createLocation("textures/model/entity/lizard/lizard_desert.png"), createLocation("textures/model/entity/lizard/sad_lizard_desert.png"), CNBItems.LIZARD_ITEM_0.get()), DESERT_2(createLocation("textures/model/entity/lizard/lizard_desert_2.png"), createLocation("textures/model/entity/lizard/sad_lizard_desert_2.png"), CNBItems.LIZARD_ITEM_1.get()), JUNGLE_1(createLocation("textures/model/entity/lizard/lizard_jungle.png"), createLocation("textures/model/entity/lizard/sad_lizard_jungle.png"), CNBItems.LIZARD_ITEM_2.get()), JUNGLE_2(createLocation("textures/model/entity/lizard/lizard_jungle_2.png"), createLocation("textures/model/entity/lizard/sad_lizard_jungle_2.png"), CNBItems.LIZARD_ITEM_3.get());

        public final ResourceLocation textureLocation;

        public final ResourceLocation textureLocationSad;

        public final Item item;

        LizardType(ResourceLocation tl, ResourceLocation tls, Item it) {
            this.textureLocation = tl;
            this.textureLocationSad = tls;
            this.item = it;
        }

        private static ResourceLocation createLocation(String pathPart) {
            return new ResourceLocation(CreaturesAndBeasts.MOD_ID, pathPart);
        }

        public ResourceLocation getTextureLocation(boolean sad) {
            if (sad) {
                return textureLocationSad;
            }
            return textureLocation;
        }

        public Item getItem() {
            return item;
        }
    }
}
