package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.CactemEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CactemModel extends AnimatedGeoModel<CactemEntity> {
    private static final ResourceLocation ELDER_CACTEM_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/cactem/elder_cactem.geo.json");
    private static final ResourceLocation WARRIOR_CACTEM_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/cactem/warrior_cactem.geo.json");
    private static final ResourceLocation BABY_CACTEM_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/cactem/baby_cactem.geo.json");

    private static final ResourceLocation ELDER_CACTEM_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/cactem/elder_cactem.png");
    private static final ResourceLocation WARRIOR_CACTEM_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/cactem/warrior_cactem.png");
    private static final ResourceLocation BABY_CACTEM_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/cactem/baby_cactem.png");

    @Override
    public ResourceLocation getModelLocation(CactemEntity entity) {
        if (entity.isBaby()) {
            return BABY_CACTEM_MODEL;
        } else if (entity.isElder()) {
            return ELDER_CACTEM_MODEL;
        } else {
            return WARRIOR_CACTEM_MODEL;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(CactemEntity entity) {
        if (entity.isBaby()) {
            return BABY_CACTEM_TEXTURE;
        } else if (entity.isElder()) {
            return ELDER_CACTEM_TEXTURE;
        } else {
            return WARRIOR_CACTEM_TEXTURE;
        }
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CactemEntity entity) {
        return null;
    }
}
