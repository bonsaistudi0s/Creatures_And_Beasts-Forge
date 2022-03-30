package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.model.AgeableModelProvider;
import com.cgessinger.creaturesandbeasts.client.model.LittleGrebeChickModel;
import com.cgessinger.creaturesandbeasts.client.model.LittleGrebeModel;
import com.cgessinger.creaturesandbeasts.common.entites.GrebeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrebeRender extends MobRenderer<GrebeEntity, AgeableModelProvider<GrebeEntity>>
{
	protected static final ResourceLocation CHILD_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/little_grebe_chick.png");
	protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/little_grebe.png");

	public GrebeRender (EntityRenderDispatcher renderManagerIn)
	{
		super(renderManagerIn, new AgeableModelProvider<>(new LittleGrebeChickModel<>(), new LittleGrebeModel<>()), 0.2F);
	}

	@Override
	public ResourceLocation getTextureLocation (GrebeEntity entity)
	{
		if(entity.isBaby())
		{
			return CHILD_TEXTURE;
		}
		return TEXTURE;
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	@Override
	protected float getBob(GrebeEntity livingBase, float partialTicks) {
		float f = Mth.lerp(partialTicks, livingBase.oFlap, livingBase.wingRotation);
		float f1 = Mth.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.destPos);
		return (Mth.sin(f) + 1.0F) * f1;
	}

	@Override
	protected void scale (GrebeEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime)
	{
		if (entitylivingbaseIn.isPassenger() && entitylivingbaseIn.isBaby())
		{
			matrixStackIn.scale(0.75F, 0.75F, 0.75F);
		}
		super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
	}
}
