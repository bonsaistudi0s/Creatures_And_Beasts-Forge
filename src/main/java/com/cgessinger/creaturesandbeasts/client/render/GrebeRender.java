package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.model.AgeableModelProvider;
import com.cgessinger.creaturesandbeasts.client.model.LittleGrebeChickModel;
import com.cgessinger.creaturesandbeasts.client.model.LittleGrebeModel;
import com.cgessinger.creaturesandbeasts.common.entites.GrebeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrebeRender extends MobRenderer<GrebeEntity, AgeableModelProvider<GrebeEntity>>
{
	protected static final ResourceLocation CHILD_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/little_grebe_chick.png");
	protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/little_grebe.png");

	public GrebeRender (EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn, new AgeableModelProvider<>(new LittleGrebeChickModel<>(), new LittleGrebeModel<>()), 0.2F);
	}

	@Override
	public ResourceLocation getEntityTexture (GrebeEntity entity)
	{
		if(entity.isChild())
		{
			return CHILD_TEXTURE;
		}
		return TEXTURE;
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	@Override
	protected float handleRotationFloat(GrebeEntity livingBase, float partialTicks) {
		float f = MathHelper.lerp(partialTicks, livingBase.oFlap, livingBase.wingRotation);
		float f1 = MathHelper.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.destPos);
		return (MathHelper.sin(f) + 1.0F) * f1;
	}

	@Override
	protected void preRenderCallback (GrebeEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime)
	{
		if (entitylivingbaseIn.isPassenger() && entitylivingbaseIn.isChild())
		{
			matrixStackIn.scale(0.75F, 0.75F, 0.75F);
		}
		super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
	}
}
