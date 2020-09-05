package com.cgessinger.creaturesandbeasts.client.model;
import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.MathHelper;

public class LittleGrebeModel<L extends AnimalEntity> extends EntityModel<LittleGrebeEntity>
{
	private final ModelRenderer bone;
	private final ModelRenderer Lwing;
	private final ModelRenderer Rwing;
	private final ModelRenderer Neck;
	private final ModelRenderer Head;
	private final ModelRenderer Lleg;
	private final ModelRenderer bone6;
	private final ModelRenderer Rleg;
	private final ModelRenderer bone9;

	public LittleGrebeModel() {
		textureWidth = 32;
		textureHeight = 32;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 24.0F, 0.0F);
		bone.setTextureOffset(0, 0).addBox(-2.5F, -7.0F, -4.0F, 5.0F, 4.0F, 8.0F, 0.0F, false);

		Lwing = new ModelRenderer(this);
		Lwing.setRotationPoint(3.0F, -6.5F, -2.5F);
		bone.addChild(Lwing);
		setRotationAngle(Lwing, -0.0873F, -0.0873F, 0.0F);
		Lwing.setTextureOffset(9, 16).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 7.0F, 0.0F, false);

		Rwing = new ModelRenderer(this);
		Rwing.setRotationPoint(-3.0F, -6.5F, -2.5F);
		bone.addChild(Rwing);
		setRotationAngle(Rwing, -0.0873F, 0.0873F, 0.0F);
		Rwing.setTextureOffset(9, 16).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 7.0F, 0.0F, true);

		Neck = new ModelRenderer(this);
		Neck.setRotationPoint(0.0F, -7.0F, -2.5F);
		bone.addChild(Neck);
		setRotationAngle(Neck, -0.1745F, 0.0F, 0.0F);
		Neck.setTextureOffset(0, 0).addBox(-1.0F, -2.7F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, -2.0F, 0.0F);
		Neck.addChild(Head);
		setRotationAngle(Head, 0.2182F, 0.0F, 0.0F);
		Head.setTextureOffset(18, 0).addBox(-1.5F, -2.5F, -2.4F, 3.0F, 3.0F, 4.0F, 0.0F, false);
		Head.setTextureOffset(0, 4).addBox(0.0F, -0.5F, -4.4F, 0.0F, 1.0F, 2.0F, 0.0F, false);

		Lleg = new ModelRenderer(this);
		Lleg.setRotationPoint(1.5F, -3.5F, 0.5F);
		bone.addChild(Lleg);
		Lleg.setTextureOffset(18, 18).addBox(-0.5F, -0.505F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

		bone6 = new ModelRenderer(this);
		bone6.setRotationPoint(0.0F, 3.5F, 0.5F);
		Lleg.addChild(bone6);
		bone6.setTextureOffset(12, 12).addBox(-1.5F, -0.005F, -3.0F, 3.0F, 0.0F, 3.0F, 0.0F, false);

		Rleg = new ModelRenderer(this);
		Rleg.setRotationPoint(-1.5F, -3.5F, 0.5F);
		bone.addChild(Rleg);
		Rleg.setTextureOffset(18, 18).addBox(-0.5F, -0.505F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, true);

		bone9 = new ModelRenderer(this);
		bone9.setRotationPoint(0.0F, 3.5F, 0.5F);
		Rleg.addChild(bone9);
		bone9.setTextureOffset(12, 12).addBox(-1.5F, -0.005F, -3.0F, 3.0F, 0.0F, 3.0F, 0.0F, true);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles (LittleGrebeEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		this.Head.rotateAngleX = headPitch * ((float)Math.PI / 270F);
		this.Head.rotateAngleY = netHeadYaw * ((float)Math.PI / 270F);
		this.Neck.rotateAngleX = this.Head.rotateAngleX;
		this.Neck.rotateAngleY = this.Head.rotateAngleY;
		this.Rleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.Lleg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.Rwing.rotateAngleZ = ageInTicks%3;
		this.Lwing.rotateAngleZ = -(ageInTicks%3);
	}

	@Override
	public void render (MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		bone.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}
}