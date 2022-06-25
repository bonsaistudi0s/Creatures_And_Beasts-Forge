package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

@OnlyIn(Dist.CLIENT)
public class CindershellModel extends AnimatedGeoModel<CindershellEntity> {
    private static final ResourceLocation CINDERSHELL_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/cindershell/cindershell.geo.json");
    private static final ResourceLocation BABY_CINDERSHELL_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/cindershell/baby_cindershell.geo.json");
    private static final ResourceLocation CINDERSHELL_FURNACE_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/cindershell/cindershell_furnace.geo.json");

    private static final ResourceLocation CINDERSHELL_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/cindershell/cindershell.png");
    private static final ResourceLocation BABY_CINDERSHELL_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/cindershell/baby_cindershell.png");

    private static final ResourceLocation CINDERSHELL_ANIMATIONS = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/cindershell.json");

    @Override
    public ResourceLocation getModelResource(CindershellEntity entity) {
        if (entity.isBaby()) {
            return BABY_CINDERSHELL_MODEL;
        } else if (entity.hasFurnace()) {
            return CINDERSHELL_FURNACE_MODEL;
        } else {
            return CINDERSHELL_MODEL;
        }
    }

    @Override
    public ResourceLocation getTextureResource(CindershellEntity entity) {
        return entity.isBaby() ? BABY_CINDERSHELL_TEXTURE : CINDERSHELL_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(CindershellEntity entity) {
        return CINDERSHELL_ANIMATIONS;
    }

    @Override
    public void setLivingAnimations(CindershellEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        AnimationData manager = entity.getFactory().getOrCreateAnimationData(uniqueID);
        int unpausedMultiplier = !Minecraft.getInstance().isPaused() || manager.shouldPlayWhilePaused ? 1 : 0;

        head.setRotationX(head.getRotationX() + extraData.headPitch * ((float) Math.PI / 180F) * unpausedMultiplier);
        head.setRotationY(head.getRotationY() + extraData.netHeadYaw * ((float) Math.PI / 180F) * unpausedMultiplier);
    }

}