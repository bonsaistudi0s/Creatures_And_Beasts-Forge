package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.client.entity.model.CindershellModel;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CindershellRenderer extends GeoEntityRenderer<CindershellEntity> {

    public CindershellRenderer(EntityRendererProvider.Context context) {
        super(context, new CindershellModel());
        this.addLayer(new CindershellGlowLayer(this));
        this.shadowRadius = 0.4F;
    }

    @Override
    protected float getDeathMaxRotation(CindershellEntity entityLivingBaseIn) {
        return 0;
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("head")) {
            stack.pushPose();
            stack.mulPose(Vector3f.XP.rotation(-bone.getRotationX()));
            stack.translate(0.0D, 0.5D, -1.0D);
            stack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(this.mainHand, ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.popPose();
            bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
