package com.cgessinger.creaturesandbeasts.items;

import com.cgessinger.creaturesandbeasts.entities.ThrownCactemSpearEntity;
import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.FireAspectEnchantment;
import net.minecraft.world.item.enchantment.KnockbackEnchantment;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import net.minecraft.world.item.enchantment.MultiShotEnchantment;
import net.minecraft.world.item.enchantment.TridentLoyaltyEnchantment;
import net.minecraft.world.item.enchantment.VanishingCurseEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class SpearItem extends Item implements Vanishable {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public SpearItem(Properties properties) {
        super(properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 5.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.9D, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment instanceof TridentLoyaltyEnchantment || enchantment instanceof MendingEnchantment ||
                enchantment instanceof DigDurabilityEnchantment || enchantment instanceof VanishingCurseEnchantment ||
                enchantment instanceof FireAspectEnchantment || enchantment instanceof DamageEnchantment ||
                enchantment instanceof KnockbackEnchantment || enchantment instanceof MultiShotEnchantment ||
                enchantment.getRegistryName().equals(Enchantments.MOB_LOOTING.getRegistryName())) {
            return true;
        }

        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int useTicks) {
        if (entity instanceof Player) {
            Player player = (Player)entity;
            int i = this.getUseDuration(stack) - useTicks;
            if (i >= 10 && !level.isClientSide) {
                stack.hurtAndBreak(1, player, (broadcastPlayer) -> broadcastPlayer.broadcastBreakEvent(entity.getUsedItemHand()));

                spawnSpears(stack, player, level);

                if (!player.getAbilities().instabuild) {
                    player.getInventory().removeItem(stack);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }


    private void spawnSpears(ItemStack stack, Player player, Level level) {
        int multishotLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, stack);
        int numberOfSpears = multishotLevel == 0 ? 1 : 3;
        float[] afloat = getShotPitches(player.getRandom());

        ItemStack noLoyaltyStack = stack.copy();

        ResourceLocation loyaltyResource = EnchantmentHelper.getEnchantmentId(Enchantments.LOYALTY);
        ListTag listtag = noLoyaltyStack.getEnchantmentTags();
        for(int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            ResourceLocation tagEnchantment = EnchantmentHelper.getEnchantmentId(compoundtag);
            if (tagEnchantment != null && tagEnchantment.equals(loyaltyResource)) {
                listtag.remove(compoundtag);
            }
        }


        for (int i = 0; i < numberOfSpears; i++) {
            if (i == 0) {
                shootProjectile(level, player, stack, afloat[i], 0.0F, true);
            } else if (i == 1) {
                shootProjectile(level, player, noLoyaltyStack, afloat[i], -10.0F, false);
            } else {
                shootProjectile(level, player, noLoyaltyStack, afloat[i], 10.0F, false);
            }
        }
    }

    private void shootProjectile(Level level, Player player, ItemStack stack, float soundVariation, float randomization, boolean canPickup) {
        ThrownCactemSpearEntity thrownSpear = new ThrownCactemSpearEntity(level, player, stack);
        Vec3 vec31 = player.getUpVector(1.0F);
        Quaternion quaternion = new Quaternion(new Vector3f(vec31), randomization, true);
        Vec3 vec3 = player.getViewVector(1.0F);
        Vector3f vector3f = new Vector3f(vec3);
        vector3f.transform(quaternion);
        thrownSpear.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 1.6F, 1.0F);

        if (player.getAbilities().instabuild) {
            thrownSpear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        } else {
            thrownSpear.pickup = canPickup ? AbstractArrow.Pickup.ALLOWED : AbstractArrow.Pickup.DISALLOWED;
        }

        level.addFreshEntity(thrownSpear);
        level.playSound(null, thrownSpear, CNBSoundEvents.SPEAR_THROW.get(), SoundSource.PLAYERS, 1.0F, soundVariation);
    }

    private static float[] getShotPitches(Random rand) {
        boolean flag = rand.nextBoolean();
        return new float[]{1.0F, getRandomShotPitch(flag, rand), getRandomShotPitch(!flag, rand)};
    }

    private static float getRandomShotPitch(boolean chance, Random rand) {
        float f = chance ? 0.63F : 0.43F;
        return 1.0F / (rand.nextFloat() * 0.5F + 1.8F) + f;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity hurtEntity, LivingEntity owner) {
        stack.hurtAndBreak(1, owner, (player) -> {
            player.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if ((double)state.getDestroySpeed(level, pos) != 0.0D) {
            stack.hurtAndBreak(2, entity, (player) -> {
                player.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }

        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

}
