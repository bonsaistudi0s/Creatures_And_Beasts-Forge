package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.EndWhaleEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class EndWhaleModel extends AnimatedGeoModel<EndWhaleEntity> {
    private static final ResourceLocation END_WHALE_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/end_whale/end_whale.geo.json");

    private static final ResourceLocation END_WHALE_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/end_whale/end_whale.png");
    private static final ResourceLocation END_WHALE_SADDLE_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/end_whale/end_whale_saddle.png");

    private static final ResourceLocation END_WHALE_ANIMATIONS = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/end_whale.json");


    @Override
    public ResourceLocation getModelResource(EndWhaleEntity entity) {
        return END_WHALE_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(EndWhaleEntity entity) {
        return entity.isSaddled() ? END_WHALE_SADDLE_TEXTURE : END_WHALE_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(EndWhaleEntity entity) {
        return END_WHALE_ANIMATIONS;
    }
}
