package com.cgessinger.creaturesandbeasts.items;

import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class HealSpellBookItem extends Item {

    public HealSpellBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));

            List<? extends LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(15, 4, 15));
            for (LivingEntity nearbyEntity : list) {
                if (nearbyEntity instanceof TamableAnimal tamableAnimal && tamableAnimal.getOwner() != null && tamableAnimal.getOwner().equals(player)) {
                    nearbyEntity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
                }
            }

            player.awardStat(Stats.ITEM_USED.get(this));

            player.playSound(CNBSoundEvents.PLAYER_HEAL.get(), 1.0F, 1.0F);
            player.playSound(SoundEvents.BOOK_PAGE_TURN, 1.0F, 1.0F);
            player.getCooldowns().addCooldown(stack.getItem(), 600);

            return InteractionResultHolder.success(stack);
        } else {
            return InteractionResultHolder.fail(stack);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 7200;
    }
}
