package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.model.LittleGrebeChickModel;
import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeChickEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LittleGrebeChickRender extends MobRenderer<LittleGrebeChickEntity, LittleGrebeChickModel<LittleGrebeChickEntity>>
{
	protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/little_grebe_chick.png");

	public LittleGrebeChickRender (EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn, new LittleGrebeChickModel<LittleGrebeChickEntity>(), 0.2F);
	}

	@Override
	public ResourceLocation getEntityTexture (LittleGrebeChickEntity entity)
	{
		return TEXTURE;
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	@Override
	protected float handleRotationFloat(LittleGrebeChickEntity livingBase, float partialTicks) {
		float f = MathHelper.lerp(partialTicks, livingBase.oFlap, livingBase.wingRotation);
		float f1 = MathHelper.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.destPos);
		return (MathHelper.sin(f) + 1.0F) * f1;
	}

	@Override
	protected void preRenderCallback (LittleGrebeChickEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime)
	{
		if (entitylivingbaseIn.isPassenger())
		{
			matrixStackIn.scale(0.75F, 0.75F, 0.75F);
		}
		super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
	}
}
