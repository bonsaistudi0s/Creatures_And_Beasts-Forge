package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.LizardEntity;
import com.cgessinger.creaturesandbeasts.init.CNBLizardTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class LizardModel extends AnimatedGeoModel<LizardEntity> {
    private static final ResourceLocation LIZARD_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/lizard/lizard.geo.json");
    private static final ResourceLocation MUSHROOM_LIZARD_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/lizard/mushroom_lizard.geo.json");
    private static final ResourceLocation SAD_LIZARD_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/lizard/sad_lizard.geo.json");
    private static final ResourceLocation SAD_MUSHROOM_LIZARD_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/lizard/sad_mushroom_lizard.geo.json");

    private static final ResourceLocation LIZARD_ANIMATIONS = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/lizard.json");

    @Override
    public ResourceLocation getModelLocation(LizardEntity entity) {
        if (entity.getLizardType().equals(CNBLizardTypes.MUSHROOM)) {
            return entity.getSad() ? SAD_MUSHROOM_LIZARD_MODEL : MUSHROOM_LIZARD_MODEL;
        }
        return entity.getSad() ? SAD_LIZARD_MODEL : LIZARD_MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(LizardEntity entity) {
        return entity.getSad() ? entity.getLizardType().getSadTextureLocation() : entity.getLizardType().getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(LizardEntity entity) {
        return LIZARD_ANIMATIONS;
    }
    
}