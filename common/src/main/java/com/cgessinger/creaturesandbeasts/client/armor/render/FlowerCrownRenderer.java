package com.cgessinger.creaturesandbeasts.client.armor.render;

import com.cgessinger.creaturesandbeasts.client.armor.model.FlowerCrownModel;
import com.cgessinger.creaturesandbeasts.items.FlowerCrownItem;
import com.cgessinger.creaturesandbeasts.items.GlowingFlowerCrownItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class FlowerCrownRenderer extends GeoArmorRenderer<FlowerCrownItem> {

    public FlowerCrownRenderer() {
        super(new FlowerCrownModel());
    }

    @Override
    public RenderType getRenderType(FlowerCrownItem animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        if (animatable instanceof GlowingFlowerCrownItem) {
            return RenderType.eyes(texture);
        } else {
            return super.getRenderType(animatable, texture, bufferSource, partialTick);
        }
    }
}
