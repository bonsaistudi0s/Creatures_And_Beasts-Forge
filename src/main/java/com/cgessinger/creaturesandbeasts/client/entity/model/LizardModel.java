package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.LizardEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class LizardModel extends AnimatedGeoModel<LizardEntity> {
    private static final ResourceLocation LIZARD_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/lizard/lizard.geo.json");
    private static final ResourceLocation SAD_LIZARD_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/lizard/sad_lizard.geo.json");

    private static final ResourceLocation LIZARD_ANIMATIONS = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/lizard.json");

    @Override
    public ResourceLocation getModelLocation(LizardEntity entity) {
        return entity.isSad() ? SAD_LIZARD_MODEL : LIZARD_MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(LizardEntity entity) {
        return entity.getLizardType().getTextureLocation(entity.isSad());
    }

    @Override
    public ResourceLocation getAnimationFileLocation(LizardEntity entity) {
        return LIZARD_ANIMATIONS;
    }

    @Override
    public void setLivingAnimations(LizardEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        if (entity.isSad()) {
            head.setRotationX(0.2182F);
            return;
        }

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}