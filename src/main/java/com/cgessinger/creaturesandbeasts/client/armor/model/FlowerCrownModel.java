package com.cgessinger.creaturesandbeasts.client.armor.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.items.FlowerCrownItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class FlowerCrownModel extends AnimatedGeoModel<FlowerCrownItem> {
    private final ResourceLocation FLOWER_CROWN_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/armor/flower_crown.geo.json");
    private final ResourceLocation FLOWER_CROWN_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/armor/flower_crown.png");

    @Override
    public ResourceLocation getModelResource(FlowerCrownItem object) {
        return FLOWER_CROWN_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(FlowerCrownItem object) {
        return FLOWER_CROWN_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(FlowerCrownItem animatable) {
        return null;
    }
}
