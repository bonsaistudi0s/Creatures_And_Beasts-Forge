package com.cgessinger.creaturesandbeasts.entities;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.UUID;

public class CactemEntity extends AgeableMob implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private final UUID healthReductionUUID = UUID.fromString("65a301bb-531d-499e-939c-eda5b857c0b4");
    private final float babyHealth = 20.0F;

    public CactemEntity(EntityType<CactemEntity> entity, Level level) {
        super(entity, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void registerGoals() {
    }

    @Override
    protected int getExperienceReward(Player player) {
        return 3 + this.level.random.nextInt(4);
    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
        double MAX_HEALTH = this.getAttribute(Attributes.MAX_HEALTH).getValue();
        if (isBaby() && MAX_HEALTH > this.babyHealth) {
            Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
            multimap.put(Attributes.MAX_HEALTH, new AttributeModifier(this.healthReductionUUID, "cactem_health_reduction", this.babyHealth - MAX_HEALTH, AttributeModifier.Operation.ADDITION));
            this.getAttributes().addTransientAttributeModifiers(multimap);
            this.setHealth(this.babyHealth);
        }
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        float percentHealth = this.getHealth() / this.babyHealth;
        this.getAttribute(Attributes.MAX_HEALTH).removeModifier(this.healthReductionUUID);
        this.setHealth(percentHealth * (float) this.getAttribute(Attributes.MAX_HEALTH).getValue());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob entity) {
        return null;
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.75F : 1.0F;
    }
    
    private <E extends IAnimatable> PlayState animationPredicate(AnimationEvent<E> event) {
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController<CactemEntity> controller = new AnimationController<>(this, "controller", 0, this::animationPredicate);

        animationData.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

}
