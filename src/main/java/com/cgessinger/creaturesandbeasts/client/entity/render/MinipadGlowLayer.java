package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.MinipadEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class MinipadGlowLayer extends GeoLayerRenderer {
    private static final ResourceLocation MINIPAD_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/minipad/minipad.geo.json");

    public MinipadGlowLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityLivingBaseIn instanceof MinipadEntity minipadEntity && minipadEntity.isGlowing()) {
            RenderType eyesRender = RenderType.eyes(minipadEntity.getMinipadType().getGlowTextureLocation());
            RenderType translucentRender = RenderType.entityTranslucent(minipadEntity.getMinipadType().getGlowTextureLocation());

            matrixStackIn.pushPose();
            this.getRenderer().render(this.getEntityModel().getModel(MINIPAD_MODEL), entityLivingBaseIn, partialTicks, eyesRender, matrixStackIn, bufferIn, bufferIn.getBuffer(eyesRender), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1.0f);
            this.getRenderer().render(this.getEntityModel().getModel(MINIPAD_MODEL), entityLivingBaseIn, partialTicks, translucentRender, matrixStackIn, bufferIn, bufferIn.getBuffer(translucentRender), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, Math.abs((((ageInTicks - partialTicks) % 100) - 50)) / 50);

            matrixStackIn.popPose();
        }
    }
}
