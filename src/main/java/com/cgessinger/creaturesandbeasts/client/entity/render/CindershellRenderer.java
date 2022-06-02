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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
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
        if (bone.getName().equals("itemHolder")) {
            stack.pushPose();
            stack.translate(0, 0.62D, -1.52D);
            stack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(this.mainHand, ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.popPose();
            bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
