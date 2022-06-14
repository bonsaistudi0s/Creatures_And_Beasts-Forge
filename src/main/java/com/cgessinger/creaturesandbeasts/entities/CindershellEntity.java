package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.containers.CinderFurnaceContainer;
import com.cgessinger.creaturesandbeasts.init.CNBBlocks;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBItems;
import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
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
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CindershellEntity extends Animal implements IAnimatable, Bucketable, ContainerListener, Container, RecipeHolder, StackedContentsCompatible, MenuProvider {
    private static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(CindershellEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(CindershellEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FURNACE = SynchedEntityData.defineId(CindershellEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> PLAYER = SynchedEntityData.defineId(CindershellEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final Ingredient TEMPTATION_ITEMS = Ingredient.of(Items.CRIMSON_FUNGUS, Items.WARPED_FUNGUS);
    private final UUID healthReductionUUID = UUID.fromString("189faad9-35de-4e15-a598-82d147b996d7");
    private final AnimationFactory factory = new AnimationFactory(this);
    protected CinderFurnaceContainer inventory;
    private Player playerInMenu;
    private int eatTimer;

    int cookingProgress;
    int cookingTotalTime;
    protected NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return CindershellEntity.this.cookingProgress;
                case 1:
                    return CindershellEntity.this.cookingTotalTime;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    CindershellEntity.this.cookingProgress = value;
                    break;
                case 1:
                    CindershellEntity.this.cookingTotalTime = value;
            }

        }

        public int getCount() {
            return 2;
        }
    };
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();

    public CindershellEntity(EntityType<CindershellEntity> type, Level worldIn) {
        super(type, worldIn);
        this.eatTimer = 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EATING, false);
        this.entityData.define(FROM_BUCKET, false);
        this.entityData.define(FURNACE, false);
        this.entityData.define(PLAYER, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("FromBucket", this.fromBucket());
        tag.putBoolean("HasFurnace", this.hasFurnace());
        if (this.hasFurnace()) {
            ListTag listtag = new ListTag();

            List<ItemStack> items = this.items;
            for(int i = 0; i < items.size(); i++) {
                ItemStack itemstack = items.get(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag compoundtag = new CompoundTag();
                    compoundtag.putByte("Slot", (byte)i);
                    itemstack.save(compoundtag);
                    listtag.add(compoundtag);
                }
            }

            tag.put("Items", listtag);
            if (this.entityData.get(PLAYER).isPresent()) {
                tag.putUUID("Player", this.entityData.get(PLAYER).get());
            }
            tag.putInt("CookTime", this.cookingProgress);
            tag.putInt("CookTimeTotal", this.cookingTotalTime);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.setFromBucket(tag.getBoolean("FromBucket"));
        UUID playerUUID = null;
        if (tag.contains("Player")) {
            playerUUID = tag.getUUID("Player");
        }
        this.setFurnace(tag.getBoolean("HasFurnace"), playerUUID);
        if (this.hasFurnace()) {
            if (tag.contains("Player") && this.level.getPlayerByUUID(tag.getUUID("Player")) != null) {
                this.inventory = this.createMenu(this.getId(), this.level.getPlayerByUUID(tag.getUUID("Player")).getInventory(), this.level.getPlayerByUUID(tag.getUUID("Player")));
            } else {
                Player player = Minecraft.getInstance().player;
                this.inventory = this.createMenu(this.getId(), player.getInventory(), player);
            }
            ListTag listtag = tag.getList("Items", 10);

            for(int i = 0; i < listtag.size(); ++i) {
                CompoundTag compoundtag = listtag.getCompound(i);
                int j = compoundtag.getByte("Slot") & 255;
                if (j < this.items.size()) {
                    this.setItem(j, ItemStack.of(compoundtag));
                }
            }

            this.cookingProgress = tag.getInt("CookTime");
            this.cookingTotalTime = tag.getInt("CookTimeTotal");
        }

        super.readAdditionalSaveData(tag);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new CindershellFloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new CindershellBreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, TEMPTATION_ITEMS, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.getEating()) {
            this.navigation.stop();
            this.eatTimer--;
        }

        if (this.eatTimer == 10) {
            this.setHolding(ItemStack.EMPTY);
        } else if (this.eatTimer == 0) {
            this.setEating(false);
        }
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return 10.0F;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.hasFurnace() && this.random.nextDouble() <= 0.25) {
            this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX() + (this.random.nextDouble() * 0.5D - 0.25), this.getY() + 2.5 + (this.random.nextDouble() * 0.1D - 0.05), this.getZ() + (this.random.nextDouble() * 0.5D - 0.25), this.getDeltaMovement().x, 0, this.getDeltaMovement().z);
        }

        if (!this.level.isClientSide && this.hasFurnace()) {
            if (this.inventory.getSlot(0).hasItem()) {
                Recipe<?> recipe = this.level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>)this.inventory.getRecipeType(), this, this.level).orElse(null);

                if (this.canBurn(recipe, this.inventory.getItems(), 64)) {
                    if (this.random.nextDouble() < 0.1D) {
                        this.playSound(SoundEvents.FURNACE_FIRE_CRACKLE, 1.0F, 1.0F);
                    }
                    ++this.cookingProgress;
                    if (this.cookingProgress >= this.cookingTotalTime) {
                        this.cookingProgress = 0;
                        this.cookingTotalTime = getTotalCookTime(this.level, this.inventory.getRecipeType(), this);
                        if (this.smelt(recipe, this.items, 64)) {
                            this.setRecipeUsed(recipe);
                        }
                    }
                } else {
                    this.cookingProgress = 0;
                }
            }
        }
    }

    @Nullable
    @Override
    public Entity changeDimension(ServerLevel level) {
        if (this.playerInMenu != null) {
            this.playerInMenu.closeContainer();
        }
        if (this.hasFurnace()) {
            this.cookingTotalTime = getTotalCookTime(level, this.inventory.getRecipeType(), this);
        }
        return super.changeDimension(level);
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    public static boolean checkCindershellSpawnRules(EntityType<CindershellEntity> entity, LevelAccessor level, MobSpawnType mobSpawnType, BlockPos pos, RandomSource random) {
        return true;
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag) {
        if (dataTag != null) {
            if (dataTag.contains("Age")) {
                this.setAge(dataTag.getInt("Age"));
            }
            if (dataTag.contains("Health")) {
                this.setHealth(dataTag.getFloat("Health"));
            }
            if (dataTag.contains("Name")) {
                this.setCustomName(Component.nullToEmpty(dataTag.getString("Name")));
            }
        }

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return TEMPTATION_ITEMS.test(stack);
    }

    public InteractionResult tryStartEat(Player player, ItemStack stack) {
        if (stack.getItem() == Items.CRIMSON_FUNGUS || stack.getItem() == Items.WARPED_FUNGUS) {
            int i = this.getAge();
            if (!this.level.isClientSide && i == 0 && this.canFallInLove()) {
                this.usePlayerItem(player, player.getUsedItemHand(), stack);
                this.setEating(true);
                this.setInLove(player);
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

        if (item.sameItem(Items.LAVA_BUCKET.getDefaultInstance()) && this.isAlive() && this.isBaby()) {
            this.playSound(this.getPickupSound(), 1.0F, 1.0F);
            ItemStack bucketItem = this.getBucketItemStack();
            this.saveToBucketTag(bucketItem);
            ItemStack bucketWithData = ItemUtils.createFilledResult(item, player, bucketItem, false);
            player.setItemInHand(hand, bucketWithData);
            Level level = this.level;

            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, bucketItem);
            }

            this.discard();
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (!this.isBaby() && !this.hasFurnace() && item.sameItem(CNBItems.CINDERSHELL_FURNACE.get().getDefaultInstance())) {
            this.setFurnace(true, player.getUUID());

            this.inventory = this.createMenu(this.getId(), player.getInventory(), player);

            if (!player.getAbilities().instabuild) {
                item.shrink(1);
            }

            this.playSound(SoundEvents.HORSE_SADDLE, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else if (this.isFood(item) && !this.getEating()) {
            return this.tryStartEat(player, item);
        } else if (this.hasFurnace() && player.isSecondaryUseActive()) {
            this.dropEquipment();
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else if (this.hasFurnace()) {
            if (!this.level.isClientSide) {
                NetworkHooks.openGui((ServerPlayer) player, this);
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    public CinderFurnaceContainer createMenu(int id, Inventory playerInventory, Player player) {
        this.playerInMenu = player;
        return new CinderFurnaceContainer(id, playerInventory, this, this.dataAccess);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.hasFurnace()) {
            this.playSound(SoundEvents.HORSE_SADDLE, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.8F);

            if (!this.level.isClientSide) {
                this.spawnAtLocation(CNBBlocks.CINDER_FURNACE.get());
                for (int i = 0; i < this.inventory.getSize(); i++) {
                    this.spawnAtLocation(this.inventory.getSlot(i).getItem());
                }
                ((CinderFurnaceContainer.CinderFurnaceResultSlot)this.inventory.getSlot(1)).checkTakeAchievements(this.inventory.getSlot(1).getItem());
                this.clearContent();
            }

            this.setFurnace(false, null);
        }
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.items, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        ItemStack itemstack = this.getItem(slot);
        boolean flag = !stack.isEmpty() && stack.sameItem(itemstack) && ItemStack.tagMatches(stack, itemstack);
        this.items.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }

        if (slot == 0 && !flag) {
            this.dataAccess.set(1, getTotalCookTime(this.level, this.inventory.getRecipeType(), this));
            this.dataAccess.set(0, 0);
            this.setChanged();
        }

    }

    @Override
    public void setChanged() {
    }

    @Override
    public void clearContent() {
        this.inventory.clearCraftingContent();
    }

    public boolean stillValid(Player player) {
        return true;
    }

    public static int getTotalCookTime(Level level, RecipeType<? extends AbstractCookingRecipe> recipeType, CindershellEntity container) {
        ResourceKey<Level> dimensionKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, container.level.dimension().location());
        float cookTimeMultiplier = dimensionKey.equals(Level.NETHER) ? 1.0F : 1.667F;
        return (int) (level.getRecipeManager().getRecipeFor(recipeType, container, level).map(AbstractCookingRecipe::getCookingTime).orElse(200) * cookTimeMultiplier);
    }

    private boolean smelt(@Nullable Recipe<?> recipe, NonNullList<ItemStack> stack, int amount) {
        if (recipe != null && this.canBurn(recipe, stack, amount)) {
            ItemStack itemstack = stack.get(0);
            ItemStack itemstack1 = ((Recipe<CindershellEntity>) recipe).assemble(this);
            ItemStack itemstack2 = stack.get(1);
            if (itemstack2.isEmpty()) {
                stack.set(1, itemstack1.copy());
            } else if (itemstack2.is(itemstack1.getItem())) {
                itemstack2.grow(itemstack1.getCount());
            }

            itemstack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    private boolean canBurn(@Nullable Recipe<?> recipe, NonNullList<ItemStack> items, int maxStack) {
        if (!items.get(0).isEmpty() && recipe != null) {
            ItemStack itemstack = ((Recipe<CindershellEntity>)recipe).assemble(this);
            if (itemstack.isEmpty()) {
                return false;
            } else {
                ItemStack itemstack1 = items.get(1);
                if (itemstack1.isEmpty()) {
                    return true;
                } else if (!itemstack1.sameItem(itemstack)) {
                    return false;
                } else if (itemstack1.getCount() + itemstack.getCount() <= maxStack && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) {
                    return true;
                } else {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
        }
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer player) {
        List<Recipe<?>> list = this.getRecipesToAwardAndPopExperience(player.getLevel(), player.position());
        player.awardRecipes(list);
        this.recipesUsed.clear();
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 vec3) {
        List<Recipe<?>> list = Lists.newArrayList();

        for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                createExperience(level, vec3, entry.getIntValue(), ((AbstractCookingRecipe)recipe).getExperience());
            });
        }

        return list;
    }

    private static void createExperience(ServerLevel level, Vec3 vec3, int value, float experience) {
        int i = Mth.floor((float)value * experience);
        float f = Mth.frac((float)value * experience);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        ExperienceOrb.award(level, vec3, i);
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents) {
        for(ItemStack itemstack : this.items) {
            stackedContents.accountStack(itemstack);
        }
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
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
        tag.putInt("Age", this.getAge());
    }

    @Override
    public void loadFromBucketTag(CompoundTag compound) {
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

        if (compound.contains("Age")) {
            this.setAge(compound.getInt("Age"));
        } else {
            this.setAge(-24000);
        }
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(CNBItems.CINDERSHELL_BUCKET.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_LAVA;
    }

    @Override
    public void containerChanged(Container container) {

    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
        double MAX_HEALTH = this.getAttribute(Attributes.MAX_HEALTH).getValue();
        float babyHealth = 10.0F;
        if (isBaby() && MAX_HEALTH > babyHealth) {
            Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
            multimap.put(Attributes.MAX_HEALTH, new AttributeModifier(this.healthReductionUUID, "yeti_health_reduction", babyHealth - MAX_HEALTH, AttributeModifier.Operation.ADDITION));
            this.getAttributes().addTransientAttributeModifiers(multimap);
            this.setHealth(babyHealth);
        }
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        this.getAttribute(Attributes.MAX_HEALTH).removeModifier(this.healthReductionUUID);
        this.setHealth((float) this.getAttribute(Attributes.MAX_HEALTH).getValue());
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 23 && !this.level.isClientSide()) {
            this.level.broadcastEntityEvent(this, (byte)60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return pose == Pose.SLEEPING ? SLEEPING_DIMENSIONS : super.getDimensions(pose).scale(this.getScale(), this.getHeightScale());
    }

    private float getHeightScale() {
        return this.isBaby() ? 0.35F : 1.0F;
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.55F : 1.0F;
    }

    @Override
    public float getEyeHeight(Pose pose) {
        return this.getBbHeight() * 0.2F;
    }

    public ItemStack getHolding() {
        return this.getItemBySlot(EquipmentSlot.MAINHAND);
    }

    public void setHolding(ItemStack stack) {
        this.setItemSlot(EquipmentSlot.MAINHAND, stack);
    }

    public void setEating(boolean isEating) {
        this.eatTimer = isEating ? 40 : 0;
        this.entityData.set(EATING, isEating);
    }

    public boolean getEating() {
        return this.entityData.get(EATING);
    }

    public boolean hasFurnace() {
        return this.entityData.get(FURNACE);
    }

    public void setFurnace(boolean hasFurnace, @Nullable UUID playerUUID) {
        this.entityData.set(FURNACE, hasFurnace);
        if (playerUUID != null) {
            this.entityData.set(PLAYER, Optional.of(playerUUID));
        } else {
            this.entityData.set(PLAYER, Optional.empty());
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return CNBEntityTypes.CINDERSHELL.get().create(level);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return !this.hasFurnace();
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return CNBSoundEvents.CINDERSHELL_AMBIENT.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
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

    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        if (!(animationSpeed > -0.05F && animationSpeed < 0.05F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.isBaby() ? "baby_cindershell_walk" : "cindershell_walk"));
        } else if (this.getEating()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("cindershell_idle_eat"));
        } else if (this.isDeadOrDying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("cindershell_death"));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("cindershell_idle"));
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState eatAnimationPredicate(AnimationEvent<E> event) {
        if (this.getEating()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("cindershell_eat"));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> void soundListener(SoundKeyframeEvent<E> event) {
        LocalPlayer player = Minecraft.getInstance().player;
        player.playSound(this.isBaby() ? CNBSoundEvents.CINDERSHELL_BABY_EAT.get() : CNBSoundEvents.CINDERSHELL_ADULT_EAT.get(), 0.4F, 1F);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController<CindershellEntity> controller = new AnimationController<>(this, "controller", 0, this::animationPredicate);
        AnimationController<CindershellEntity> eatController = new AnimationController<>(this, "eatController", 0, this::eatAnimationPredicate);

        eatController.registerSoundListener(this::soundListener);

        animationData.addAnimationController(controller);
        animationData.addAnimationController(eatController);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    static class CindershellFloatGoal extends FloatGoal {
        private final CindershellEntity cindershell;

        public CindershellFloatGoal(CindershellEntity cindershell) {
            super(cindershell);
            this.cindershell = cindershell;
        }

        @Override
        public boolean canUse() {
            return this.cindershell.isInLava();
        }
    }

    static class CindershellBreedGoal extends BreedGoal {

        public CindershellBreedGoal(Animal cindershell, double speedModifier) {
            super(cindershell, speedModifier);
        }

        @Override
        protected void breed() {
            int range = this.animal.getRandom().nextInt(4) + 3;
            for (int i = 0; i <= range; i++) {
                super.breed();
            }
        }
    }
}
