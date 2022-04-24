/*
 * Copyright 2022 Infernal Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cgessinger.creaturesandbeasts.entities.ai;

import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.pathfinder.Path;

import java.util.List;
import java.util.Map;

public class ConvertItemGoal extends Goal {
    protected Path path;
    protected ItemEntity itemInstance;
    protected double convertTime;
    protected double convertDelay;
    protected boolean converting;

    protected final double speed;
    protected final SporelingEntity entityIn;
    protected final double range;
    protected final PathNavigation navigation;

    public ConvertItemGoal(SporelingEntity entityIn, double range, double speedIn) {
        this.entityIn = entityIn;
        this.range = range;
        this.speed = speedIn;
        this.navigation = entityIn.getNavigation();
    }

    @Override
    public boolean canUse() {
        if (this.itemInstance == null) {
            List<ItemEntity> list = this.entityIn.level.getEntitiesOfClass(ItemEntity.class, this.entityIn.getBoundingBox().inflate(this.range, 3.0D, this.range));

            for (ItemEntity item : list) {
                if (item.getItem().sameItem(Items.DIRT.getDefaultInstance()) || (item.getItem().isEnchanted() && hasCurse(item.getItem()))) {
                    this.path = this.navigation.createPath(item.getOnPos(), 0);
                    this.itemInstance = item;
                    return path != null;
                }
            }
        }

        return false;
    }

    private boolean hasCurse(ItemStack stack) {
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            if (entry.getKey().isCurse()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void stop() {
        this.itemInstance = null;
        this.path = null;
        entityIn.setHolding(ItemStack.EMPTY);
        converting = false;
        entityIn.setInspecting(false);
    }

    @Override
    public void start() {
        this.navigation.moveTo(this.path, this.speed);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.navigation.isDone() || this.convertTime > 0;
    }

    public void convertItem() {
        converting = false;
        entityIn.setInspecting(false);

        if (entityIn.getHolding().sameItem(Items.DIRT.getDefaultInstance())) {
            entityIn.spawnAtLocation(new ItemStack(Items.MYCELIUM, 1));
        } else {
            ItemStack returnItem = entityIn.getHolding().copy();

            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(returnItem);
            for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
                if (entry.getKey().isCurse()) {
                    map.remove(entry.getKey(), entry.getValue());
                    if (returnItem.isDamageableItem()) {
                        float percent = entityIn.getRandom().nextFloat() * 0.5F;
                        int damage = (int) (percent * returnItem.getMaxDamage() + returnItem.getDamageValue());
                        int setDamage = Math.min(damage, (int) (returnItem.getMaxDamage() * 0.9F));
                        returnItem.setDamageValue(Math.max(returnItem.getDamageValue(), setDamage));
                    }
                    EnchantmentHelper.setEnchantments(map, returnItem);
                    break;
                }
            }

            entityIn.spawnAtLocation(returnItem);
            if (entityIn.level instanceof ServerLevel serverLevel) {
                ExperienceOrb.award(serverLevel, entityIn.position(), entityIn.getRandom().nextInt(16) + 1);
            }
        }

        entityIn.setHolding(ItemStack.EMPTY);
    }

    @Override
    public void tick() {
        if (convertDelay <= 0) {
            if (this.entityIn.distanceToSqr(itemInstance) < 2.0D || this.entityIn.isInspecting()) {
                this.navigation.setSpeedModifier(0.0D);

                if (!converting) {
                    converting = true;
                    this.entityIn.setHolding(itemInstance.getItem().copy());
                    itemInstance.getItem().shrink(1);
                    entityIn.setInspecting(true);
                    entityIn.lookAt(EntityAnchorArgument.Anchor.EYES, itemInstance.position());
                    this.convertTime = 54;

                } else {
                    if (--this.convertTime <= 0) {
                        convertItem();
                        convertDelay = 20;

                    } else if (convertTime % 3 == 0) {
                        entityIn.lookAt(EntityAnchorArgument.Anchor.EYES, itemInstance.position());
                        entityIn.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, itemInstance.getItem()), entityIn.getRandomX(0.5F) + entityIn.getLookAngle().x / 2.0D, entityIn.getRandomY(), entityIn.getRandomZ(0.5F) + entityIn.getLookAngle().z / 2.0D, 4D, 0D, 0D);
                    }
                }

            } else {
                this.navigation.setSpeedModifier(speed);
            }

        } else {
            convertDelay--;
        }
    }
}
