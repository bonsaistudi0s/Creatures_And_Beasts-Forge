package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.client.entity.model.EndWhaleModel;
import com.cgessinger.creaturesandbeasts.entities.EndWhaleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class EndWhaleRenderer extends GeoEntityRenderer<EndWhaleEntity> {

    public EndWhaleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EndWhaleModel());
        this.shadowRadius = 1.5F;
    }

    @Override
    public RenderType getRenderType(EndWhaleEntity animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }

    @Override
    protected void applyRotations(EndWhaleEntity endWhale, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(endWhale, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        float whaleRotY = endWhale.getViewYRot(partialTicks);
        float wantedRotY;
        float whaleRotX = endWhale.getViewXRot(partialTicks);
        float wantedRotX;
        Entity rider = endWhale.getFirstPassenger();

        if (rider != null) {
            wantedRotY = rider.getViewYRot(partialTicks);
            wantedRotX = rider.getViewXRot(partialTicks);
        } else {
            wantedRotY = endWhale.yBodyRot;
            wantedRotX = endWhale.getXRot();
        }

        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(Mth.wrapDegrees(whaleRotY - wantedRotY) / 2));
        matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(Mth.wrapDegrees(whaleRotX - wantedRotX)));
    }
}
