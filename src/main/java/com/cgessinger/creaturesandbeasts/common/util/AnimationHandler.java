package com.cgessinger.creaturesandbeasts.common.util;

import java.util.Optional;

import com.cgessinger.creaturesandbeasts.common.interfaces.IAnimationHolder;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.world.server.ServerWorld;

public class AnimationHandler <T extends Entity & IAnimationHolder<T>>
{
    public final T entity;
    private final int animationLength;
    private final int breakpoint;
    private final int delay;
    public Optional<ExecutionData> data;
    public DataParameter<Boolean> animating;
    
    private int animateTimer;

    public AnimationHandler (T entity, int animationLength, int breakpoint, int delay, DataParameter<Boolean> parameter)
    {
        this.entity = entity;
        this.animateTimer = 0;
        this.animationLength = animationLength;
        this.breakpoint = breakpoint;
        this.animating = parameter;
        this.delay = delay;
        this.data = Optional.empty();
    }

    public void process()
    {
        if(!this.entity.world.isRemote())
        {
            this.animateTimer = Math.max(this.animateTimer - 1, 0);

            this.setAnimating((this.animateTimer - this.delay) > 0);

            if(this.animateTimer == this.breakpoint + this.delay)
            {
                this.entity.executeBreakpoint(this.data);
                this.data = Optional.empty();
            }
        }
    }

    public void startAnimation(Optional<ExecutionData> data)
    {
        if(this.canStart())
        {
            this.animateTimer = this.animationLength + this.delay;
            this.data = this.entity.onAnimationInit(data);
        }
    }

    public void startAnimation ()
    {
        this.startAnimation(Optional.empty());
    }

    public boolean canStart ()
    {
        return this.animateTimer <= 0 && !this.isAnimating();
    }
    
    public boolean isAnimating ()
    {
        return this.entity.getDataManager().get(this.animating);
    }

    private void setAnimating (boolean anim)
    {
        this.entity.getDataManager().set(this.animating, anim);
    }

    public static class ExecutionData
    {
        public boolean isBreedData;
        public ServerWorld world;
        public AnimalEntity entity;
        public ItemStack stack;

        public ExecutionData (DataBuilder builder)
        {
            this.isBreedData = builder.isBreedData;
            this.world = builder.world;
            this.entity = builder.entity;
            this.stack = builder.stack;
        }

        public static DataBuilder create () 
        {
            return new DataBuilder();
        }

        public static Optional<ExecutionData> EMPTY ()
        {
            return Optional.empty();
        }
    }

    public static class DataBuilder
    {
        private boolean isBreedData = false;
        private ServerWorld world;
        private AnimalEntity entity;
        private ItemStack stack;

        public DataBuilder isBreed ()
        {
            this.isBreedData = true;
            return this;
        }

        public DataBuilder withWorld (ServerWorld world)
        {
            this.world = world;
            return this;
        }

        public DataBuilder withEntity (AnimalEntity entity)
        {
            this.entity = entity;
            return this;
        }

        public DataBuilder withItemStack (ItemStack stack)
        {
            this.stack = stack;
            return this;
        }

        public Optional<ExecutionData> build ()
        {
            return Optional.of(new ExecutionData(this));
        }
    }
}
