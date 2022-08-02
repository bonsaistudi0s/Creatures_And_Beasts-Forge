package com.cgessinger.creaturesandbeasts.items;

import com.cgessinger.creaturesandbeasts.capabilities.CinderSwordCapability;
import com.cgessinger.creaturesandbeasts.capabilities.CinderSwordWrapper;
import com.cgessinger.creaturesandbeasts.capabilities.ICinderSwordUpdate;
import com.cgessinger.creaturesandbeasts.init.CNBItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.item.Item.Properties;

public class CinderSwordItem extends SwordItem {
    private static final List<RegistryObject<CinderSwordItem>> IMBUE_TIERS = List.of(CNBItems.CINDER_SWORD, CNBItems.CINDER_SWORD_1, CNBItems.CINDER_SWORD_2, CNBItems.CINDER_SWORD_3, CNBItems.CINDER_SWORD_4);
    private final int imbueLevel;

    public CinderSwordItem(Tier tier, int imbueLevel, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
        this.imbueLevel = imbueLevel;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity targetEntity, LivingEntity attackingEntity) {
        if (this.imbueLevel > 0) {
            targetEntity.setSecondsOnFire(2 * this.imbueLevel);
        }

        return super.hurtEnemy(stack, targetEntity, attackingEntity);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotNumber, boolean isSelected) {
        LazyOptional<ICinderSwordUpdate> capability = stack.getCapability(CinderSwordCapability.CINDER_SWORD_CAPABILITY);
        int imbuedTicks = capability.map(ICinderSwordUpdate::getImbuedTicks).get();

        if (imbuedTicks > 0) {
            capability.map(handler -> handler.setImbuedTicks(imbuedTicks - 1));
        } else if (this.imbueLevel > 0 && entity instanceof Player player) {
            ItemStack sword = new ItemStack(IMBUE_TIERS.get(this.imbueLevel - 1).get());

            if (this.imbueLevel == 1) {
                player.playSound(SoundEvents.FIRE_EXTINGUISH, 1.0F, 1.0F);
            }

            sword.setTag(stack.getOrCreateTag());
            sword.getCapability(CinderSwordCapability.CINDER_SWORD_CAPABILITY).map(handler -> handler.setImbuedTicks(400));
            player.getInventory().setItem(slotNumber, sword);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos pos = blockhitresult.getBlockPos();

        if (level.getFluidState(pos).is(Fluids.LAVA)) {
            ItemStack imbuedSword = new ItemStack(IMBUE_TIERS.get(IMBUE_TIERS.size()-1).get());
            imbuedSword.setTag(itemstack.getOrCreateTag());
            imbuedSword.getCapability(CinderSwordCapability.CINDER_SWORD_CAPABILITY).map(handler -> handler.setImbuedTicks(400));
            player.setItemInHand(hand, imbuedSword);
            player.playSound(SoundEvents.BUCKET_FILL_LAVA, 1.0F, 1.0F);
            return InteractionResultHolder.success(itemstack);
        }

        return super.use(level, player, hand);
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        return super.onDroppedByPlayer(item, player);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (this.getClass() == CinderSwordItem.class) {
            return new CinderSwordWrapper();
        }
        return super.initCapabilities(stack, nbt);
    }
}
