package com.cgessinger.creaturesandbeasts.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.AgeableEntity;

public class AgeableModelProvider<T extends AgeableEntity> extends EntityModel<T>
{
	public final EntityModel<T> childModel;
	public final EntityModel<T> adultModel;

	public AgeableModelProvider (EntityModel<T> childModel, EntityModel<T> adultModel)
	{
		this.childModel = childModel;
		this.adultModel = adultModel;
	}

	@Override
	public void setRotationAngles (T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		if(this.isChild)
		{
			this.childModel.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		} else
		{
			this.adultModel.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		}
	}

	@Override
	public void render (MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		if(this.isChild)
		{
			this.childModel.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		} else
		{
			this.adultModel.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
		}
	}
}
