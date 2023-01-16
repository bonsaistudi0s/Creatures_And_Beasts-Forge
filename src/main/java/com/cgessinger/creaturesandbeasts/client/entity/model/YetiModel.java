package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.YetiEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public class YetiModel extends AnimatedGeoModel<YetiEntity> {
    private static final ResourceLocation YETI_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/yeti/yeti.geo.json");
    private static final ResourceLocation BABY_YETI_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/yeti/baby_yeti.geo.json");

    private static final ResourceLocation YETI_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/yeti/yeti.png");
    private static final ResourceLocation BABY_YETI_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/yeti/baby_yeti.png");

    private static final ResourceLocation YETI_ANIMATIONS = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/yeti.json");

    @Override
    public ResourceLocation getModelLocation(YetiEntity entity) {
        return entity.isBaby() ? BABY_YETI_MODEL : YETI_MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(YetiEntity entity) {
        return entity.isBaby() ? BABY_YETI_TEXTURE : YETI_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(YetiEntity entity) {
        return YETI_ANIMATIONS;
    }

    @Override
    public void setCustomAnimations(YetiEntity animatable, int instanceId, AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);

        IBone head_rotation = this.getAnimationProcessor().getBone("head_rotation");

        EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);

        head_rotation.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        if (animatable.isBaby()) {
            head_rotation.setRotationZ(extraData.netHeadYaw * ((float) Math.PI / 180F));
        } else {
            head_rotation.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }
}
