package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.client.entity.model.GrebeModel;
import com.cgessinger.creaturesandbeasts.entities.GrebeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class GrebeRenderer extends GeoEntityRenderer<GrebeEntity> {

    public GrebeRenderer(EntityRendererProvider.Context context) {
        super(context, new GrebeModel());
        this.shadowRadius = 0.4F;
    }

}
