package com.cgessinger.creaturesandbeasts.util;

import net.minecraft.world.entity.Entity;

import java.util.Optional;

public interface IAnimationHolder<T extends Entity & IAnimationHolder<T>> {
    void executeBreakpoint(Optional<AnimationHandler.ExecutionData> data);

    default Optional<AnimationHandler.ExecutionData> onAnimationInit(Optional<AnimationHandler.ExecutionData> data) {
        return data;
    }

    AnimationHandler<T> getAnimationHandler(String name);
}
