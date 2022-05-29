package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class CindershellModel extends AnimatedGeoModel<CindershellEntity> {
    private static final ResourceLocation CINDERSHELL_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/cindershell/cindershell.geo.json");
    private static final ResourceLocation BABY_CINDERSHELL_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/cindershell/baby_cindershell.geo.json");
    private static final ResourceLocation CINDERSHELL_FURNACE_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/cindershell/cindershell_furnace.geo.json");

    private static final ResourceLocation CINDERSHELL_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/cindershell/cindershell.png");
    private static final ResourceLocation BABY_CINDERSHELL_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/cindershell/baby_cindershell.png");

    private static final ResourceLocation CINDERSHELL_ANIMATIONS = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/cindershell.json");

    @Override
    public ResourceLocation getModelLocation(CindershellEntity entity) {
        if (entity.isBaby()) {
            return BABY_CINDERSHELL_MODEL;
        } else if (entity.hasFurnace()) {
            return CINDERSHELL_FURNACE_MODEL;
        } else {
            return CINDERSHELL_MODEL;
        }
    }

    @Override
    public ResourceLocation getTextureLocation(CindershellEntity entity) {
        return entity.isBaby() ? BABY_CINDERSHELL_TEXTURE : CINDERSHELL_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(CindershellEntity entity) {
        return CINDERSHELL_ANIMATIONS;
    }


}