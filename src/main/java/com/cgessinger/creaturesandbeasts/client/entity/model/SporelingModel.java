package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class SporelingModel extends AnimatedGeoModel<SporelingEntity> {
    private static final ResourceLocation SPORELING_ANIMATIONS = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/sporeling.json");

    @Override
    public ResourceLocation getModelResource(SporelingEntity entity) {
        return entity.getSporelingType().getModelLocation();
    }

    @Override
    public ResourceLocation getTextureResource(SporelingEntity entity) {
        return entity.getSporelingType().getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationResource(SporelingEntity entity) {
        return SPORELING_ANIMATIONS;
    }
}
