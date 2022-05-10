package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.client.entity.model.EndWhaleModel;
import com.cgessinger.creaturesandbeasts.entities.EndWhaleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class EndWhaleRenderer extends GeoEntityRenderer<EndWhaleEntity> {

    public EndWhaleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EndWhaleModel());
        this.shadowRadius = 1.5F;
    }

}
