package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.MinipadEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class MinipadModel extends AnimatedGeoModel<MinipadEntity> {
    private static final ResourceLocation MINIPAD_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/minipad/minipad.geo.json");
    private static final ResourceLocation MINIPAD_SHEARED_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/minipad/minipad_sheared.png");
    private static final ResourceLocation MINIPAD_ANIMATIONS = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/minipad.json");

    @Override
    public ResourceLocation getModelLocation(MinipadEntity entity) {
        return MINIPAD_MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(MinipadEntity entity) {
        return entity.getSheared() ? MINIPAD_SHEARED_TEXTURE : entity.getMinipadType().getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(MinipadEntity entity) {
        return MINIPAD_ANIMATIONS;
    }
}
