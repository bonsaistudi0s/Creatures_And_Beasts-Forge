package com.cgessinger.creaturesandbeasts.entities;

import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ThrownCactemSpearEntity extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> IS_FOIL = SynchedEntityData.defineId(ThrownCactemSpearEntity.class, EntityDataSerializers.BOOLEAN);
    private ItemStack spearItem = new ItemStack(CNBItems.CACTEM_SPEAR.get());
    private boolean dealtDamage;

    public ThrownCactemSpearEntity(EntityType<? extends ThrownCactemSpearEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownCactemSpearEntity(Level level, LivingEntity entity, ItemStack itemStack) {
        super(CNBEntityTypes.THROWN_CACTEM_SPEAR.get(), entity, level);
        this.entityData.set(IS_FOIL, itemStack.hasFoil());
        this.spearItem = itemStack.copy();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_FOIL, false);
    }


    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("CactemSpear", 10)) {
            this.spearItem = ItemStack.of(tag.getCompound("CactemSpear"));
        }

        this.dealtDamage = tag.getBoolean("DealtDamage");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("CactemSpear", this.spearItem.save(new CompoundTag()));
        tag.putBoolean("DealtDamage", this.dealtDamage);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        super.tick();
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.spearItem.copy();
    }

    public boolean isFoil() {
        return this.entityData.get(IS_FOIL);
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 vec1, Vec3 vec2) {
        return this.dealtDamage ? null : super.findHitEntity(vec1, vec2);
    }

    protected void onHitEntity(EntityHitResult hitResult) {
        Entity hitEntity = hitResult.getEntity();
        float f = 8.0F;
        if (hitEntity instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.spearItem, livingentity.getMobType());
        }

        Entity projectileThrower = this.getOwner();
        DamageSource damagesource = DamageSource.thrown(this, (projectileThrower == null ? this : projectileThrower));
        this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (hitEntity.hurt(damagesource, f)) {
            if (hitEntity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (hitEntity instanceof LivingEntity hitLivingEntity) {
                if (projectileThrower instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(hitLivingEntity, projectileThrower);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)projectileThrower, hitLivingEntity);
                }

                this.doPostHurtEffects(hitLivingEntity);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float f1 = 1.0F;

        this.playSound(soundevent, f1, 1.0F);
    }
    
    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }
    }

    @Override
    public void tickDespawn() {
        if (this.pickup != AbstractArrow.Pickup.ALLOWED) {
            super.tickDespawn();
        }
    }

    @Override
    public boolean shouldRender(double p_37588_, double p_37589_, double p_37590_) {
        return true;
    }
}
