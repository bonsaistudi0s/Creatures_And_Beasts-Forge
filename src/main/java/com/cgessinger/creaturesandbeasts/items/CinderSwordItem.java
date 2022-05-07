package com.cgessinger.creaturesandbeasts.items;

import com.cgessinger.creaturesandbeasts.capabilities.CinderSwordCapability;
import com.cgessinger.creaturesandbeasts.capabilities.CinderSwordWrapper;
import com.cgessinger.creaturesandbeasts.capabilities.ICinderSwordUpdate;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
import org.jetbrains.annotations.Nullable;

public class CinderSwordItem extends SwordItem {
    private final float attackDamage;
    private final float attackSpeed;

    public CinderSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
        this.attackDamage = (float)attackDamageModifier + tier.getAttackDamageBonus();
        this.attackSpeed = attackSpeedModifier;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity targetEntity, LivingEntity attackingEntity) {
        LazyOptional<ICinderSwordUpdate> capability = stack.getCapability(CinderSwordCapability.CINDER_SWORD_CAPABILITY);
        if (capability.map(ICinderSwordUpdate::getImbued).get()) {
            targetEntity.setSecondsOnFire(6);
        }

        return super.hurtEnemy(stack, targetEntity, attackingEntity);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotNumber, boolean isSelected) {
        LazyOptional<ICinderSwordUpdate> capability = stack.getCapability(CinderSwordCapability.CINDER_SWORD_CAPABILITY);
        int imbuedTicks = capability.map(ICinderSwordUpdate::getImbuedTicks).get();

        if (imbuedTicks > 0) {
            capability.map(handler -> handler.setImbuedTicks(imbuedTicks-1));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        BlockPos pos = blockhitresult.getBlockPos();

        if (level.getFluidState(pos).is(Fluids.LAVA)) {
            itemstack.getCapability(CinderSwordCapability.CINDER_SWORD_CAPABILITY).map(handler -> handler.setImbued(true));
            return InteractionResultHolder.success(itemstack);
        }

        return super.use(level, player, hand);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        LazyOptional<ICinderSwordUpdate> capability = stack.getCapability(CinderSwordCapability.CINDER_SWORD_CAPABILITY);
        if (slot == EquipmentSlot.MAINHAND && capability.map(ICinderSwordUpdate::getImbued).get()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage + 2.5, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", this.attackSpeed, AttributeModifier.Operation.ADDITION));
            return builder.build();
        } else {
            return this.getDefaultAttributeModifiers(slot);
        }
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
