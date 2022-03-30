package com.cgessinger.creaturesandbeasts.util;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/*
 * I created this class to sync activities on server side and animation via geckolib on client side. But this handler is also useful for non geckolib mobs and can be used with them.
 * To use it implement IAnimationHolder return a private instance of AnimationHandler in IAnimationHolder#getAnimationHandler and do whatever in IAnimationHolder#executeBreakpoint.
 * In addition do AnimationHandler#process in the LivingTick()
 *
 */
public class AnimationHandler<T extends Entity & IAnimationHolder<T>> {
    public final T entity;
    public final String name;
    protected final int animationLength;
    protected final int breakpoint;
    protected final int delay;
    public Optional<ExecutionData> data;
    public EntityDataAccessor<Boolean> animating;

    protected int animateTimer;

    public AnimationHandler(String name, T entity, int animationLength, int breakpoint, int delay, EntityDataAccessor<Boolean> parameter) {
        this.entity = entity;
        this.animateTimer = 0;
        this.animationLength = animationLength;
        this.breakpoint = breakpoint;
        this.animating = parameter;
        this.delay = delay;
        this.name = name;
        this.data = ExecutionData.EMPTY();
    }

    public void process() {
        if (!this.entity.level.isClientSide()) {
            this.animateTimer = Math.max(this.animateTimer - 1, 0);

            this.setAnimating((this.animateTimer - this.delay) > 0);

            if (this.animateTimer == this.breakpoint + this.delay) {
                this.data.get().name = this.name;
                this.entity.executeBreakpoint(this.data);
                this.data = ExecutionData.EMPTY();
            }
        }
    }

    public void startAnimation(Optional<ExecutionData> data) {
        if (this.canStart()) {
            this.animateTimer = this.animationLength + this.delay;
            this.data = this.entity.onAnimationInit(data);
        }
    }

    public void startAnimation() {
        this.startAnimation(ExecutionData.EMPTY());
    }

    public boolean canStart() {
        return this.animateTimer <= 0 && !this.isAnimating();
    }

    public boolean isAnimating() {
        return this.entity.getEntityData().get(this.animating);
    }

    private void setAnimating(boolean anim) {
        this.entity.getEntityData().set(this.animating, anim);
    }

    public static class ExecutionData {
        public boolean isBreedData;
        public ServerLevel world;
        public Animal entity;
        public ItemStack stack;
        public Player player;
        public String name;

        public ExecutionData(DataBuilder builder) {
            this.isBreedData = builder.isBreedData;
            this.world = builder.world;
            this.entity = builder.entity;
            this.stack = builder.stack;
            this.player = builder.player;
            this.name = "";
        }

        public static DataBuilder create() {
            return new DataBuilder();
        }

        public static Optional<ExecutionData> EMPTY() {
            return ExecutionData.create().build();
        }
    }

    public static class DataBuilder {
        public Player player;
        private boolean isBreedData = false;
        private ServerLevel world;
        private Animal entity;
        private ItemStack stack;

        public DataBuilder isBreed() {
            this.isBreedData = true;
            return this;
        }

        public DataBuilder withWorld(ServerLevel world) {
            this.world = world;
            return this;
        }

        public DataBuilder withEntity(Animal entity) {
            this.entity = entity;
            return this;
        }

        public DataBuilder withItemStack(ItemStack stack) {
            this.stack = stack;
            return this;
        }

        public DataBuilder withPlayer(Player player) {
            this.player = player;
            return this;
        }


        public Optional<ExecutionData> build() {
            return Optional.of(new ExecutionData(this));
        }
    }
}
