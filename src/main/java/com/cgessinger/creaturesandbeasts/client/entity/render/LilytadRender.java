package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.client.entity.model.LilytadModel;
import com.cgessinger.creaturesandbeasts.entities.LilytadEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

public class LilytadRender extends GeoEntityRenderer<LilytadEntity> {
    public LilytadRender(EntityRendererProvider.Context context) {
        super(context, new LilytadModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    public ResourceLocation getTextureLocation(LilytadEntity entity) {
        return getTextureLocation(entity);
    }

    @Override
    public RenderType getRenderType(LilytadEntity animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}
