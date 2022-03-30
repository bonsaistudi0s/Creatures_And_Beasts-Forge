package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.entities.GrebeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Animal;

public class LittleGrebeChickModel<T extends Animal> extends EntityModel<GrebeEntity> {
    private final ModelPart bone;
    private final ModelPart Head;
    private final ModelPart Rwing;
    private final ModelPart Lwing;
    private final ModelPart Lleg;
    private final ModelPart Rleg;

    public LittleGrebeChickModel() {
        texWidth = 32;
        texHeight = 32;

        bone = new ModelPart(this);
        bone.setPos(0.0F, 24.0F, 0.0F);
        setRotationAngle(bone, 0.0F, 3.1416F, 0.0F);
        bone.texOffs(0, 0).addBox(-2.0F, -5.0F, -3.0F, 4.0F, 3.0F, 6.0F, 0.0F, false);

        Head = new ModelPart(this);
        Head.setPos(0.0F, -4.5F, 2.0F);
        bone.addChild(Head);
        setRotationAngle(Head, -0.0436F, 0.0F, 0.0F);
        Head.texOffs(0, 9).addBox(-1.5F, -3.0F, -1.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);

        ModelPart bone2 = new ModelPart(this);
        bone2.setPos(0.0F, 0.0F, 0.0F);
        Head.addChild(bone2);
        bone2.texOffs(0, 0).addBox(-0.5F, -1.3F, 1.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);

        Rwing = new ModelPart(this);
        Rwing.setPos(2.0F, -4.5F, 1.5F);
        bone.addChild(Rwing);
        setRotationAngle(Rwing, 0.0873F, 0.0873F, 0.0F);
        Rwing.texOffs(9, 12).addBox(0.0F, -0.5F, -2.5F, 1.0F, 2.0F, 3.0F, 0.0F, true);

        Lwing = new ModelPart(this);
        Lwing.setPos(-2.0F, -4.5F, 1.5F);
        bone.addChild(Lwing);
        setRotationAngle(Lwing, 0.0873F, -0.0873F, 0.0F);
        Lwing.texOffs(9, 12).addBox(-1.0F, -0.5F, -2.5F, 1.0F, 2.0F, 3.0F, 0.0F, false);

        Lleg = new ModelPart(this);
        Lleg.setPos(-1.0F, -2.0F, 0.0F);
        bone.addChild(Lleg);
        Lleg.texOffs(14, 0).addBox(-1.5F, -0.005F, -0.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);

        Rleg = new ModelPart(this);
        Rleg.setPos(1.0F, -2.0F, 0.0F);
        bone.addChild(Rleg);
        Rleg.texOffs(14, 0).addBox(-0.5F, -0.005F, -0.5F, 2.0F, 2.0F, 2.0F, 0.0F, true);
    }

    @Override
    public void setupAnim(GrebeEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.Head.xRot = -headPitch * ((float) Math.PI / 180F);
        this.Head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.Rleg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.Lleg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
        this.Rwing.zRot = ageInTicks;
        this.Lwing.zRot = -ageInTicks;
    }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        bone.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }
}