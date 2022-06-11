package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.client.entity.model.MinipadModel;
import com.cgessinger.creaturesandbeasts.entities.MinipadEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class MinipadRenderer extends GeoEntityRenderer<MinipadEntity> {
    public MinipadRenderer(EntityRendererProvider.Context context) {
        super(context, new MinipadModel());
        this.addLayer(new MinipadGlowLayer(this));
        this.shadowRadius = 0.4F;
    }

    @Override
    public RenderType getRenderType(MinipadEntity animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }
}
