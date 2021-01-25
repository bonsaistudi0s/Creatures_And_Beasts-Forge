package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.client.model.YetiModel;
import com.cgessinger.creaturesandbeasts.common.entites.YetiEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class YetiRender<T extends YetiEntity> extends GeoEntityRenderer<T>
{
	public YetiRender (EntityRendererManager renderManager)
	{
		super(renderManager, new YetiModel<>());
		this.shadowSize = 0.7F;
	}

	@Override
	public RenderType getRenderType (T animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation)
	{
		return RenderType.getEntityTranslucent(getTextureLocation(animatable));
	}
}
