package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.EndWhaleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EndWhaleModel extends AnimatedGeoModel<EndWhaleEntity> {
    private static final ResourceLocation END_WHALE_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/end_whale/end_whale.geo.json");

    private static final ResourceLocation END_WHALE_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/end_whale/end_whale.png");
    private static final ResourceLocation END_WHALE_SADDLE_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/end_whale/end_whale_saddle.png");

    private static final ResourceLocation END_WHALE_ANIMATIONS = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/end_whale.json");


    @Override
    public ResourceLocation getModelLocation(EndWhaleEntity object) {
        return END_WHALE_MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(EndWhaleEntity object) {
        return END_WHALE_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EndWhaleEntity animatable) {
        return END_WHALE_ANIMATIONS;
    }
}
