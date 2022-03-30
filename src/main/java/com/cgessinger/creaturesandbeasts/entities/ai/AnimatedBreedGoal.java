package com.cgessinger.creaturesandbeasts.entities.ai;

import com.cgessinger.creaturesandbeasts.util.IAnimationHolder;
import com.cgessinger.creaturesandbeasts.util.AnimationHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.GameRules;
import software.bernie.geckolib3.core.IAnimatable;

import java.util.Random;

public class AnimatedBreedGoal<E extends Animal & IAnimatable & IAnimationHolder<E>> extends BreedGoal {
    protected final E entity;

    public AnimatedBreedGoal(E animal, double speedIn) {
        super(animal, speedIn);
        this.entity = animal;
    }

    @Override
    public boolean canUse() {
        return this.entity.getAnimationHandler("breed_controller").canStart() && super.canUse();
    }

    @Override
    protected void breed() {
        ServerPlayer serverplayerentity = this.entity.getLoveCause();
        if (serverplayerentity == null && this.partner.getLoveCause() != null) {
            serverplayerentity = this.partner.getLoveCause();
        }

        if (serverplayerentity != null) {
            serverplayerentity.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(serverplayerentity, this.entity, this.partner, null);
        }

        this.entity.getAnimationHandler("breed_controller").startAnimation(AnimationHandler.ExecutionData.create().isBreed().withWorld((ServerLevel) this.level).withEntity(this.partner).build());
        this.entity.setAge(6000);
        this.partner.setAge(6000);
        this.entity.resetLove();
        this.partner.resetLove();
        Random random = this.entity.getRandom();
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.level.addFreshEntity(new ExperienceOrb(level, this.entity.getX(), this.entity.getY(), this.entity.getZ(), random.nextInt(7) + 1));
        }
    }
}
