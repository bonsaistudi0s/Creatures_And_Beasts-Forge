package com.cgessinger.creaturesandbeasts.client.armor.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.items.FlowerCrownItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FlowerCrownModel extends AnimatedGeoModel<FlowerCrownItem> {
    private final ResourceLocation FLOWER_CROWN_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/armor/flower_crown.geo.json");
    private final ResourceLocation FLOWER_CROWN_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/armor/flower_crown.png");

    @Override
    public ResourceLocation getModelLocation(FlowerCrownItem object) {
        return FLOWER_CROWN_MODEL;
    }

    @Override
    public ResourceLocation getTextureLocation(FlowerCrownItem object) {
        return FLOWER_CROWN_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(FlowerCrownItem animatable) {
        return null;
    }
}
