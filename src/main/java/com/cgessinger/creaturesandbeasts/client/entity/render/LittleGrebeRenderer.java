package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.client.entity.model.LittleGrebeModel;
import com.cgessinger.creaturesandbeasts.entities.LittleGrebeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class LittleGrebeRenderer extends GeoEntityRenderer<LittleGrebeEntity> {

    public LittleGrebeRenderer(EntityRendererProvider.Context context) {
        super(context, new LittleGrebeModel());
        this.shadowRadius = 0.4F;
    }

}
