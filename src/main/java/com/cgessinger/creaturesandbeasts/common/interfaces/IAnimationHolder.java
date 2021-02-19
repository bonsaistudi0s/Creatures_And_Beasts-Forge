package com.cgessinger.creaturesandbeasts.common.interfaces;

import java.util.Optional;

import com.cgessinger.creaturesandbeasts.common.util.AnimationHandler;
import com.cgessinger.creaturesandbeasts.common.util.AnimationHandler.ExecutionData;

import net.minecraft.entity.Entity;
import software.bernie.geckolib3.core.IAnimatable;

public interface IAnimationHolder <T extends Entity & IAnimatable & IAnimationHolder<T>>
{
    void executeBreakpoint(Optional<ExecutionData> data);

    default Optional<ExecutionData> onAnimationInit(Optional<ExecutionData> data)
    {
        return data;
    }

    AnimationHandler<T> getAnimationHandler();
}
