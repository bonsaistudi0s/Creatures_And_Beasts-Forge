package com.cgessinger.creaturesandbeasts.client.armor.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.items.SporelingBackpackItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class SporelingBackpackModel extends AnimatedGeoModel<SporelingBackpackItem> {
    private final ResourceLocation SPORELING_BACKPACK_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/armor/sporeling_backpack.geo.json");
    private final ResourceLocation SPORELING_BACKPACK_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/armor/sporeling_backpack.png");
    private final ResourceLocation SPORELING_BACKPACK_ANIMATION = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/sporeling_backpack.json");

    @Override
    public ResourceLocation getModelResource(SporelingBackpackItem object) {
        return SPORELING_BACKPACK_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(SporelingBackpackItem object) {
        return SPORELING_BACKPACK_TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(SporelingBackpackItem animatable) {
        return null;
    }

}
