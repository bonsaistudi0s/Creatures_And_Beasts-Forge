package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.LilytadEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LilytadModel extends AnimatedGeoModel<LilytadEntity> {
    private static final ResourceLocation LILYTAD_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/lilytad/lilytad.geo.json");
    private static final ResourceLocation LILYTAD_SHEARED_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/lilytad/lilytad_sheared.png");
    private static final ResourceLocation LILYTAD_ANIMATIONS = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/lilytad.json");

    @Override
    public ResourceLocation getModelLocation(LilytadEntity entity) {
        return LILYTAD_MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(LilytadEntity entity) {
        return entity.getSheared() ? LILYTAD_SHEARED_TEXTURE : entity.getLilytadType().getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(LilytadEntity entity) {
        return LILYTAD_ANIMATIONS;
    }
}
