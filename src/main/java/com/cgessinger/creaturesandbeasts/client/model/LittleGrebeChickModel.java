package com.cgessinger.creaturesandbeasts.client.model;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeChickEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib.animation.model.AnimatedEntityModel;

public class LittleGrebeChickModel <T extends LittleGrebeChickEntity> extends AnimatedEntityModel<LittleGrebeChickEntity>
{
	private final ModelRenderer bone;
	private final ModelRenderer Head;
	private final ModelRenderer bone2;
	private final ModelRenderer Rwing;
	private final ModelRenderer Lwing;
	private final ModelRenderer Lleg;
	private final ModelRenderer Rleg;

	public LittleGrebeChickModel () {
		textureWidth = 32;
		textureHeight = 32;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, 24.0F, 0.0F);
		setRotationAngle(bone, 0.0F, 3.1416F, 0.0F);
		bone.setTextureOffset(0, 0).addBox(-2.0F, -5.0F, -3.0F, 4.0F, 3.0F, 6.0F, 0.0F, false);

		Head = new ModelRenderer(this);
		Head.setRotationPoint(0.0F, -4.5F, 2.0F);
		bone.addChild(Head);
		setRotationAngle(Head, -0.0436F, 0.0F, 0.0F);
		Head.setTextureOffset(0, 9).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(0.0F, 0.0F, 0.0F);
		Head.addChild(bone2);
		bone2.setTextureOffset(0, 0).addBox(-0.5F, -1.3F, 1.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		Rwing = new ModelRenderer(this);
		Rwing.setRotationPoint(2.0F, -4.5F, 1.5F);
		bone.addChild(Rwing);
		setRotationAngle(Rwing, 0.0873F, 0.0873F, 0.0F);
		Rwing.setTextureOffset(9, 12).addBox(0.0F, -0.5F, -2.5F, 1.0F, 2.0F, 3.0F, 0.0F, true);

		Lwing = new ModelRenderer(this);
		Lwing.setRotationPoint(-2.0F, -4.5F, 1.5F);
		bone.addChild(Lwing);
		setRotationAngle(Lwing, 0.0873F, -0.0873F, 0.0F);
		Lwing.setTextureOffset(9, 12).addBox(-1.0F, -0.5F, -2.5F, 1.0F, 2.0F, 3.0F, 0.0F, false);

		Lleg = new ModelRenderer(this);
		Lleg.setRotationPoint(-1.0F, -2.0F, 0.0F);
		bone.addChild(Lleg);
		Lleg.setTextureOffset(14, 0).addBox(-1.5F, -0.005F, -0.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

		Rleg = new ModelRenderer(this);
		Rleg.setRotationPoint(1.0F, -2.0F, 0.0F);
		bone.addChild(Rleg);
		Rleg.setTextureOffset(14, 0).addBox(-0.5F, -0.005F, -0.5F, 2.0F, 2.0F, 2.0F, 0.0F, true);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void render (MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		bone.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}

	@Override
	public ResourceLocation getAnimationFileLocation ()
	{
		return new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/little_grebe_chick_anim.json");
	}
}