package com.cgessinger.creaturesandbeasts.items;

import com.cgessinger.creaturesandbeasts.init.CNBItems;
import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HealSpellBookItem extends Item {

    public HealSpellBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(CNBItems.HEAL_SPELL_BOOK_1.get())) {
            this.applyEffects(level, player, stack, MobEffects.REGENERATION, 200, 0);
            return this.applyCooldowns(player, stack, 800);
        } else if (stack.is(CNBItems.HEAL_SPELL_BOOK_2.get())) {
            this.applyEffects(level, player, stack, MobEffects.REGENERATION, 140, 1);
            this.applyEffects(level, player, stack, MobEffects.HEAL, 1, 0);
            return this.applyCooldowns(player, stack, 700);
        } else {
            this.applyEffects(level, player, stack, MobEffects.REGENERATION, 100, 2);
            this.applyEffects(level, player, stack, MobEffects.HEAL, 1, 1);
            return this.applyCooldowns(player, stack, 600);
        }
    }

    private void applyEffects(Level level, Player player, ItemStack stack, MobEffect effect, int duration, int amplifier) {
        if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
            player.addEffect(new MobEffectInstance(effect, duration, amplifier));

            List<? extends LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(15, 4, 15));
            for (LivingEntity nearbyEntity : list) {
                if (nearbyEntity instanceof TamableAnimal tamableAnimal && tamableAnimal.getOwner() != null && tamableAnimal.getOwner().equals(player)) {
                    nearbyEntity.addEffect(new MobEffectInstance(effect, duration, amplifier));
                }
            }
        }
    }

    private InteractionResultHolder<ItemStack> applyCooldowns(Player player, ItemStack stack, int cooldownTime) {
        if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
            player.awardStat(Stats.ITEM_USED.get(this));

            player.playSound(CNBSoundEvents.PLAYER_HEAL.get(), 1.0F, 1.0F);
            player.playSound(SoundEvents.BOOK_PAGE_TURN, 1.0F, 1.0F);

            player.getCooldowns().addCooldown(CNBItems.HEAL_SPELL_BOOK_1.get(), cooldownTime);
            player.getCooldowns().addCooldown(CNBItems.HEAL_SPELL_BOOK_2.get(), cooldownTime);
            player.getCooldowns().addCooldown(CNBItems.HEAL_SPELL_BOOK_3.get(), cooldownTime);

            return InteractionResultHolder.success(stack);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        if (stack.is(CNBItems.HEAL_SPELL_BOOK_1.get())) {
            tooltip.add(new TextComponent("\u00A72Level 1"));
        } else if (stack.is(CNBItems.HEAL_SPELL_BOOK_2.get())) {
            tooltip.add(new TextComponent("\u00A74Level 2"));
        } else {
            tooltip.add(new TextComponent("\u00A76Level 3"));
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 7200;
    }
}
