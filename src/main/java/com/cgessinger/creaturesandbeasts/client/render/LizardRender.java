package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.model.LizardModel;
import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class LizardRender extends GeoEntityRenderer<LizardEntity>
{
	protected static ResourceLocation[] TEXTURES;

	public LizardRender (EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn, new LizardModel<LizardEntity>());
		TEXTURES = new ResourceLocation[]{
				new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/lizard/lizard_desert.png"),
				new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/lizard/lizard_desert_2.png"),
				new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/lizard/lizard_jungle.png"),
				new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/lizard/lizard_jungle_2.png"),
				new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/lizard/sad_lizard_desert.png"),
				new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/lizard/sad_lizard_desert_2.png"),
				new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/lizard/sad_lizard_jungle.png"),
				new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/lizard/sad_lizard_jungle_2.png")
		};
		this.shadowSize = 0.3F;
	}

	@Override
	public ResourceLocation getEntityTexture (LizardEntity entity)
	{
		return TEXTURES[entity.getVariant()];
	}

	@Override
	public void renderEarly (LizardEntity animatable, MatrixStack stackIn, float ticks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks)
	{
		stackIn.scale(0.8F, 0.8F, 0.8F);
	}
}
