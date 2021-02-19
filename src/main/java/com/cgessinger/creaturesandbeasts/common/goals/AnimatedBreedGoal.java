package com.cgessinger.creaturesandbeasts.common.goals;

import java.util.Random;

import com.cgessinger.creaturesandbeasts.common.interfaces.IAnimationHolder;
import com.cgessinger.creaturesandbeasts.common.util.AnimationHandler.ExecutionData;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;

public class AnimatedBreedGoal<E extends AnimalEntity & IAnimatable & IAnimationHolder<E>>
    extends BreedGoal
{

    protected final E entity;

    public AnimatedBreedGoal( E animal, double speedIn )
    {
        super( animal, speedIn );
        this.entity = animal;
    }

    @Override
    public boolean shouldExecute()
    {
        return this.entity.getAnimationHandler().canStart() && super.shouldExecute();
    }

    @Override
    protected void spawnBaby()
    {
        ServerPlayerEntity serverplayerentity = this.entity.getLoveCause();
        if ( serverplayerentity == null && this.targetMate.getLoveCause() != null )
        {
            serverplayerentity = this.targetMate.getLoveCause();
        }

        if ( serverplayerentity != null )
        {
            serverplayerentity.addStat( Stats.ANIMALS_BRED );
            CriteriaTriggers.BRED_ANIMALS.trigger( serverplayerentity, this.entity, this.targetMate,
                                                   (AgeableEntity) null );
        }

        
        this.entity.getAnimationHandler().startAnimation( ExecutionData.create().isBreed().withWorld( (ServerWorld) this.world ).withEntity( this.targetMate ).build() );
        this.entity.setGrowingAge(6000);
        this.targetMate.setGrowingAge(6000);
        this.entity.resetInLove();
        this.targetMate.resetInLove();
        Random random = this.entity.getRNG();
        if ( this.world.getGameRules().getBoolean( GameRules.DO_MOB_LOOT ) )
        {
            this.world.addEntity( new ExperienceOrbEntity( world, this.entity.getPosX(), this.entity.getPosY(),
                                                           this.entity.getPosZ(), random.nextInt( 7 ) + 1 ) );
        }
    }
}
