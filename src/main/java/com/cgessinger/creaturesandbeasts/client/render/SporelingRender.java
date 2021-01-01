package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.client.model.SporelingModel;
import com.cgessinger.creaturesandbeasts.common.entites.AbstractSporelingEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class SporelingRender<T extends AbstractSporelingEntity> extends GeoEntityRenderer<T>
{
	public SporelingRender (EntityRendererManager renderManager)
	{
		super(renderManager, new SporelingModel<>());
	}
}
