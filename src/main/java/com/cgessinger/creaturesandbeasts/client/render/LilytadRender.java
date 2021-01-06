package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.client.model.LilytadModel;
import com.cgessinger.creaturesandbeasts.common.entites.LilytadEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

public class LilytadRender extends GeoEntityRenderer<LilytadEntity>
{
	public LilytadRender (EntityRendererManager renderManager)
	{
		super(renderManager, new LilytadModel());
		this.shadowSize = 0.7F;
	}

	@Override
	public RenderType getRenderType (LilytadEntity animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation)
	{
		return RenderType.getEntityTranslucent(getTextureLocation(animatable));
	}
}
