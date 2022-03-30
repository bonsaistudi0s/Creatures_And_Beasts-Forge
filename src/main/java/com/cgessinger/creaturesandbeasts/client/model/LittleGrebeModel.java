package com.cgessinger.creaturesandbeasts.client.model;

import com.cgessinger.creaturesandbeasts.common.entites.GrebeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.util.Mth;

public class LittleGrebeModel<L extends Animal> extends EntityModel<GrebeEntity>
{
	private final ModelPart bone;
	private final ModelPart Head;
	private final ModelPart Rwing;
	private final ModelPart Lwing;
	private final ModelPart Neck;
	private final ModelPart Lleg;
	private final ModelPart Rleg;

	public LittleGrebeModel() {
		texWidth = 32;
		texHeight = 32;

		bone = new ModelPart(this);
		bone.setPos(0.0F, 24.0F, 0.0F);
		bone.texOffs(0, 0).addBox(-2.5F, -7.0F, -4.0F, 5.0F, 4.0F, 8.0F, 0.0F, false);

		Lwing = new ModelPart(this);
		Lwing.setPos(3.0F, -6.5F, -2.5F);
		bone.addChild(Lwing);
		setRotationAngle(Lwing, -0.0873F, -0.0873F, 0.0F);
		Lwing.texOffs(9, 16).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 7.0F, 0.0F, false);

		Rwing = new ModelPart(this);
		Rwing.setPos(-3.0F, -6.5F, -2.5F);
		bone.addChild(Rwing);
		setRotationAngle(Rwing, -0.0873F, 0.0873F, 0.0F);
		Rwing.texOffs(9, 16).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 7.0F, 0.0F, true);

		Neck = new ModelPart(this);
		Neck.setPos(0.0F, -7.0F, -2.5F);
		bone.addChild(Neck);
		setRotationAngle(Neck, -0.1745F, 0.0F, 0.0F);
		Neck.texOffs(0, 0).addBox(-1.0F, -2.7F, -1.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		Head = new ModelPart(this);
		Head.setPos(0.0F, -2.0F, 0.0F);
		Neck.addChild(Head);
		setRotationAngle(Head, 0.2182F, 0.0F, 0.0F);
		Head.texOffs(18, 0).addBox(-1.5F, -2.5F, -2.4F, 3.0F, 3.0F, 4.0F, 0.0F, false);
		Head.texOffs(0, 4).addBox(0.0F, -0.5F, -4.4F, 0.0F, 1.0F, 2.0F, 0.0F, false);

		Lleg = new ModelPart(this);
		Lleg.setPos(1.5F, -3.5F, 0.5F);
		bone.addChild(Lleg);
		Lleg.texOffs(18, 18).addBox(-0.5F, -0.505F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);

		ModelPart bone6 = new ModelPart(this);
		bone6.setPos(0.0F, 3.5F, 0.5F);
		Lleg.addChild(bone6);
		bone6.texOffs(12, 12).addBox(-1.5F, -0.005F, -3.0F, 3.0F, 0.0F, 3.0F, 0.0F, false);

		Rleg = new ModelPart(this);
		Rleg.setPos(-1.5F, -3.5F, 0.5F);
		bone.addChild(Rleg);
		Rleg.texOffs(18, 18).addBox(-0.5F, -0.505F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, true);

		ModelPart bone9 = new ModelPart(this);
		bone9.setPos(0.0F, 3.5F, 0.5F);
		Rleg.addChild(bone9);
		bone9.texOffs(12, 12).addBox(-1.5F, -0.005F, -3.0F, 3.0F, 0.0F, 3.0F, 0.0F, true);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public void setupAnim (GrebeEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		float max = 90 * ((float)Math.PI / 180F);
		this.Head.xRot = headPitch * ((float)Math.PI / 180F);
		this.Head.yRot = limitInRange(netHeadYaw * ((float)Math.PI / 180F), -max, max);
		this.Neck.xRot = this.bone.xRot;
		this.Neck.yRot = this.bone.yRot;
		this.Rleg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.Lleg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
		this.Rwing.zRot = ageInTicks;
		this.Lwing.zRot = -ageInTicks;

		if(entityIn.isInWater())
		{
			this.Rleg.xRot -= 100;
			this.Lleg.xRot -= 100;
		}
	}

	@Override
	public void renderToBuffer (PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{
		bone.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}

	private float limitInRange(float value, float min, float max)
	{
		return Math.min((Math.max(value, min)), max);
	}
}