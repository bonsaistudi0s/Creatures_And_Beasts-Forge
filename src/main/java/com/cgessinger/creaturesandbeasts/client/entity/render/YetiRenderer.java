package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.client.entity.model.YetiModel;
import com.cgessinger.creaturesandbeasts.entities.YetiEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class YetiRenderer extends GeoEntityRenderer<YetiEntity> {
    private YetiEntity entity;

    public YetiRenderer(EntityRendererProvider.Context context) {
        super(context, new YetiModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    public void renderEarly(YetiEntity animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.entity = animatable;
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("ItemHolder")) {
            stack.pushPose();

            if (this.entity != null && this.entity.isBaby()) {
                stack.translate(bone.getPositionX() + 0.05D, bone.getPositionY() + 0.3D, bone.getPositionZ() + 0.15D);
                stack.mulPose(Vector3f.ZP.rotationDegrees(-10.0F));
                stack.mulPose(Vector3f.YP.rotationDegrees(-43.0F));
                stack.mulPose(Vector3f.ZP.rotationDegrees(10.0F));
            } else if (this.entity != null){
                stack.translate(bone.getPositionX() + 0.3D, bone.getPositionY() + 1.0D, bone.getPositionZ());
            }
            Minecraft.getInstance().getItemRenderer().renderStatic(this.entity.getHolding(), ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.popPose();
            bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public RenderType getRenderType(YetiEntity entity, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(entity));
    }
}
