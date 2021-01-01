package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.client.model.LilytadModel;
import com.cgessinger.creaturesandbeasts.common.entites.LilytadEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class LilytadRender extends GeoEntityRenderer<LilytadEntity>
{
	public LilytadRender (EntityRendererManager renderManager)
	{
		super(renderManager, new LilytadModel());
	}
}
