package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.client.entity.model.CactemModel;
import com.cgessinger.creaturesandbeasts.entities.CactemEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CactemRenderer extends GeoEntityRenderer<CactemEntity> {

    public CactemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CactemModel());
        this.shadowRadius = 0.4F;
    }

}
