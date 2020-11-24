package com.cgessinger.creaturesandbeasts.client.model;

import com.cgessinger.creaturesandbeasts.common.entites.CyndershellEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.MathHelper;

public class CyndershellModel<L extends AnimalEntity> extends EntityModel<CyndershellEntity>
{
	private final ModelRenderer bone;
	private final ModelRenderer bone2;
	private final ModelRenderer Head;
	private final ModelRenderer Jaw;
	private final ModelRenderer bone4;
	private final ModelRenderer Tail;
	private final ModelRenderer bone3;
	private final ModelRenderer FR;
	private final ModelRenderer BR;
	private final ModelRenderer BL;
	private final ModelRenderer FL;

	public CyndershellModel ()
	{
		textureWidth = 128;
		textureHeight = 128;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 24.0F, 0.0F);
		bone.setTextureOffset(63, 0).addBox(-7.0F, -29.0F, -7.0F, 14.0F, 3.0F, 14.0F, 0.0F, false);
		bone.setTextureOffset(0, 0).addBox(-10.0F, -26.0F, -12.0F, 20.0F, 15.0F, 23.0F, 0.0F, false);
		bone.setTextureOffset(0, 38).addBox(-11.0F, -14.0F, -13.0F, 22.0F, 4.0F, 25.0F, 0.0F, false);
		bone.setTextureOffset(79, 67).addBox(-6.0F, -15.0F, -14.0F, 12.0F, 6.0F, 5.0F, 0.0F, false);

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone.addChild(bone2);
		bone2.setTextureOffset(0, 67).addBox(-8.0F, -11.0F, -11.0F, 16.0F, 6.0F, 21.0F, 0.0F, false);

		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, -10.0F, -10.0F);
		bone2.addChild(Head);


		Jaw = new ModelRenderer(this);
		Jaw.setRotationPoint(0.0F, 1.0F, 0.0F);
		Head.addChild(Jaw);
		setRotationAngle(Jaw, 0.1745F, 0.0F, 0.0F);
		Jaw.setTextureOffset(53, 67).addBox(-3.5F, -2.0F, -12.5F, 7.0F, 4.0F, 12.0F, 0.0F, false);

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(0.0F, 0.0F, 0.0F);
		Head.addChild(bone4);
		setRotationAngle(bone4, 0.0873F, 0.0F, 0.0F);
		bone4.setTextureOffset(14, 14).addBox(-1.0F, -6.0F, -11.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		bone4.setTextureOffset(0, 38).addBox(-1.5F, -4.0F, -11.5F, 3.0F, 1.0F, 3.0F, 0.0F, false);
		bone4.setTextureOffset(69, 38).addBox(-3.0F, -3.0F, -12.0F, 6.0F, 4.0F, 12.0F, 0.0F, false);

		Tail = new ModelRenderer(this);
		Tail.setRotationPoint(0.0F, -9.0F, 9.0F);
		bone.addChild(Tail);


		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(0.0F, 0.0F, 1.0F);
		Tail.addChild(bone3);
		setRotationAngle(bone3, -0.0873F, 0.0F, 0.0F);
		bone3.setTextureOffset(74, 83).addBox(-4.0F, -3.0F, -1.0F, 8.0F, 5.0F, 8.0F, 0.0F, false);
		bone3.setTextureOffset(0, 13).addBox(-2.0F, -3.0F, 7.0F, 4.0F, 2.0F, 6.0F, 0.0F, false);

		FR = new ModelRenderer(this);
		FR.setRotationPoint(-7.0F, -8.0F, -6.5F);
		bone.addChild(FR);
		FR.setTextureOffset(0, 0).addBox(-2.0F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, 0.0F, false);

		BR = new ModelRenderer(this);
		BR.setRotationPoint(-8.0F, -8.0F, 5.5F);
		bone.addChild(BR);
		BR.setTextureOffset(0, 0).addBox(-1.0F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, 0.0F, false);

		BL = new ModelRenderer(this);
		BL.setRotationPoint(7.0F, -8.0F, 5.5F);
		bone.addChild(BL);
		BL.setTextureOffset(0, 0).addBox(-3.0F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, 0.0F, false);

		FL = new ModelRenderer(this);
		FL.setRotationPoint(9.0F, -8.0F, -6.5F);
		bone.addChild(FL);
		FL.setTextureOffset(0, 0).addBox(-5.0F, 0.0F, -2.5F, 5.0F, 8.0F, 5.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles (CyndershellEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.Head.rotateAngleX = headPitch * ((float) Math.PI / 180F);
		this.Head.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);
		this.BR.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2 * limbSwingAmount;
		this.BL.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2 * limbSwingAmount;
		this.FR.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2 * limbSwingAmount;
		this.FL.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2 * limbSwingAmount;
		this.Tail.rotateAngleX = MathHelper.cos(ageInTicks / 15) * 0.15F;
	}

	public void setRotationAngle (ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void render (MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		bone.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}
}