package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.LittleGrebeEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public class LittleGrebeModel extends AnimatedGeoModel<LittleGrebeEntity> {
    private static final ResourceLocation LITTLE_GREBE_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/little_grebe/little_grebe.geo.json");
    private static final ResourceLocation LITTLE_GREBE_CHICK_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/little_grebe/little_grebe_chick.geo.json");

    private static final ResourceLocation LITTLE_GREBE_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/little_grebe/little_grebe.png");
    private static final ResourceLocation LITTLE_GREBE_CHICK_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/little_grebe/little_grebe_chick.png");

    private static final ResourceLocation LITTLE_GREBE_ANIMATIONS = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/little_grebe.json");

    @Override
    public ResourceLocation getModelResource(LittleGrebeEntity entity) {
        return entity.isBaby() ? LITTLE_GREBE_CHICK_MODEL : LITTLE_GREBE_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(LittleGrebeEntity entity) {
        return entity.isBaby() ? LITTLE_GREBE_CHICK_TEXTURE : LITTLE_GREBE_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(LittleGrebeEntity entity) {
        return LITTLE_GREBE_ANIMATIONS;
    }

    @Override
    public void setLivingAnimations(LittleGrebeEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F));
        head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F));
    }

}