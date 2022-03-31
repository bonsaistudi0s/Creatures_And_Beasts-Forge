package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.GrebeEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GrebeModel extends AnimatedGeoModel<GrebeEntity> {
    private static final ResourceLocation GREBE_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/grebe/grebe.geo.json");
    private static final ResourceLocation BABY_GREBE_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/grebe/baby_grebe.geo.json");

    private static final ResourceLocation GREBE_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/grebe/grebe.png");
    private static final ResourceLocation BABY_GREBE_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/grebe/baby_grebe.png");


    @Override
    public ResourceLocation getModelLocation(GrebeEntity entity) {
        return entity.isBaby() ? BABY_GREBE_MODEL : GREBE_MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(GrebeEntity entity) {
        return entity.isBaby() ? BABY_GREBE_TEXTURE : GREBE_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GrebeEntity entity) {
        return null;
    }

    @Override
    public void setLivingAnimations(GrebeEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        IBone head = this.getAnimationProcessor().getBone("head");
        IBone beak = this.getAnimationProcessor().getBone("beak");
        IBone rleg = this.getAnimationProcessor().getBone("rleg");
        IBone lleg = this.getAnimationProcessor().getBone("lleg");
        IBone rwing = this.getAnimationProcessor().getBone("rwing");
        IBone lwing = this.getAnimationProcessor().getBone("lwing");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        head.setRotationX(extraData.headPitch * ((float)Math.PI / 180F));
        head.setRotationY(extraData.netHeadYaw * ((float)Math.PI / 180F));
        rleg.setRotationX(Mth.cos(customPredicate.getLimbSwing() * 0.6662F) * 1.4F * customPredicate.getLimbSwingAmount());
        lleg.setRotationX(Mth.cos(customPredicate.getLimbSwing() * 0.6662F + (float)Math.PI) * 1.4F * customPredicate.getLimbSwingAmount());
        rwing.setRotationZ(customPredicate.getPartialTick());
        lwing.setRotationZ(-customPredicate.getPartialTick());

        if (beak != null) {
            beak.setRotationX(head.getRotationX());
            beak.setRotationY(head.getRotationY());
        }
    }
}