package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.LittleGrebeEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LittleGrebeModel extends AnimatedGeoModel<LittleGrebeEntity> {
    private static final ResourceLocation LITTLE_GREBE_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/little_grebe/little_grebe.geo.json");
    private static final ResourceLocation LITTLE_GREBE_CHICK_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/little_grebe/little_grebe_chick.geo.json");

    private static final ResourceLocation LITTLE_GREBE_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/little_grebe/little_grebe.png");
    private static final ResourceLocation LITTLE_GREBE_CHICK_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/little_grebe/little_grebe_chick.png");

    private static final ResourceLocation LITTLE_GREBE_ANIMATIONS = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/little_grebe.json");

    @Override
    public ResourceLocation getModelLocation(LittleGrebeEntity entity) {
        return entity.isBaby() ? LITTLE_GREBE_CHICK_MODEL : LITTLE_GREBE_MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(LittleGrebeEntity entity) {
        return entity.isBaby() ? LITTLE_GREBE_CHICK_TEXTURE : LITTLE_GREBE_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(LittleGrebeEntity entity) {
        return LITTLE_GREBE_ANIMATIONS;
    }

}