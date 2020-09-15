package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.model.LizardModel;
import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LizardRender extends MobRenderer<LizardEntity, LizardModel<LizardEntity>>
{
	protected static ResourceLocation[] TEXTURES;

	public LizardRender (EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn, new LizardModel<LizardEntity>(), 0.4F);
		TEXTURES = new ResourceLocation[]{
				new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/lizard/lizard_desert.png"),
				new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/lizard/lizard_desert_2.png"),
				new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/lizard/lizard_jungle.png"),
				new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/lizard/lizard_jungle_2.png")
		};
	}

	@Override
	public ResourceLocation getEntityTexture (LizardEntity entity)
	{
		return TEXTURES[entity.getTexture()];
	}

	@Override
	public void render (LizardEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	protected void preRenderCallback (LizardEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime)
	{
		matrixStackIn.scale(0.8F, 0.8F, 0.8F);
		super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
	}
}
