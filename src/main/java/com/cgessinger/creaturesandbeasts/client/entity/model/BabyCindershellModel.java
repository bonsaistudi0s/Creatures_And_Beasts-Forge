package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class BabyCindershellModel extends EntityModel<CindershellEntity> {
    private final ModelPart bone;

    private final ModelPart bone2;

    private final ModelPart Head;

    private final ModelPart bone4;

    private final ModelPart Tail;

    private final ModelPart bone3;

    private final ModelPart BR;

    private final ModelPart BL;

    private final ModelPart FL;

    private final ModelPart FR;

    public BabyCindershellModel() {
        texWidth = 32;
        texHeight = 32;

        bone = new ModelPart(this);
        bone.setPos(0.0F, 22.0F, -2.0F);
        bone.texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 4.0F, 8.0F, 0.0F, false);

        bone2 = new ModelPart(this);
        bone2.setPos(0.0F, -1.0F, -3.0F);
        bone.addChild(bone2);

        Head = new ModelPart(this);
        Head.setPos(0.0F, 0.0F, 0.0F);
        bone2.addChild(Head);

        bone4 = new ModelPart(this);
        bone4.setPos(0.0F, 1.0F, 7.0F);
        Head.addChild(bone4);
        setRotationAngle(bone4, 0.0873F, 0.0F, 0.0F);
        bone4.texOffs(0, 5).addBox(-0.5F, -4.0F, -9.7F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        bone4.texOffs(0, 12).addBox(-1.5F, -2.98F, -10.7F, 3.0F, 2.0F, 5.0F, 0.0F, false);

        Tail = new ModelPart(this);
        Tail.setPos(0.0F, -1.5F, 4.5F);
        bone.addChild(Tail);

        bone3 = new ModelPart(this);
        bone3.setPos(0.0F, 0.5F, 1.0F);
        Tail.addChild(bone3);
        setRotationAngle(bone3, -0.0873F, 0.0F, 0.0F);
        bone3.texOffs(11, 12).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 3.0F, 0.0F, false);

        BR = new ModelPart(this);
        BR.setPos(-2.8F, -1.0F, 4.7F);
        bone.addChild(BR);
        setRotationAngle(BR, 0.0F, 0.0F, 0.0873F);
        BR.texOffs(0, 0).addBox(-1.2F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);

        BL = new ModelPart(this);
        BL.setPos(2.8F, -1.0F, 4.7F);
        bone.addChild(BL);
        setRotationAngle(BL, 0.0F, 0.0F, -0.0873F);
        BL.texOffs(0, 0).addBox(-0.8F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        FL = new ModelPart(this);
        FL.setPos(3.0F, -1.0F, -2.5F);
        bone.addChild(FL);
        setRotationAngle(FL, -0.0873F, 0.0F, -0.0873F);
        FL.texOffs(0, 0).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 3.0F, 2.0F, 0.0F, false);

        FR = new ModelPart(this);
        FR.setPos(-3.0F, -1.0F, -2.5F);
        bone.addChild(FR);
        setRotationAngle(FR, -0.0873F, 0.0F, 0.0873F);
        FR.texOffs(0, 0).addBox(-1.0F, 0.0F, -1.5F, 2.0F, 3.0F, 2.0F, 0.0F, true);
    }

    @Override
    public void setupAnim(CindershellEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.Head.xRot = headPitch * ((float) Math.PI / 180F);
        this.Head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.BR.xRot = Mth.cos(limbSwing * 0.6662F) * 2 * limbSwingAmount;
        this.BL.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2 * limbSwingAmount;
        this.FR.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2 * limbSwingAmount;
        this.FL.xRot = Mth.cos(limbSwing * 0.6662F) * 2 * limbSwingAmount;
        this.Tail.xRot = Mth.cos(ageInTicks / 15) * 0.15F;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        bone.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}