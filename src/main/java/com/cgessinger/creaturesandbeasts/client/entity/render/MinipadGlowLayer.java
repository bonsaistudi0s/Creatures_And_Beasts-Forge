package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.MinipadEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@OnlyIn(Dist.CLIENT)
public class MinipadGlowLayer extends GeoLayerRenderer {
    private static final ResourceLocation MINIPAD_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/minipad/minipad.geo.json");

    public MinipadGlowLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Entity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        long time = entityLivingBaseIn.level.getDayTime();

        if (entityLivingBaseIn instanceof MinipadEntity minipadEntity && minipadEntity.isGlowing()) {
            RenderType eyesTexture = RenderType.entityTranslucent(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/minipad/minipad_eyes_glow.png"));

            RenderType flowerGlow = RenderType.eyes(minipadEntity.getMinipadType().getGlowTextureLocation());
            RenderType flowerTranslucent = RenderType.entityTranslucent(minipadEntity.getMinipadType().getGlowTextureLocation());

            matrixStackIn.pushPose();

            this.getRenderer().render(this.getEntityModel().getModel(MINIPAD_MODEL), entityLivingBaseIn, partialTicks, flowerGlow, matrixStackIn, bufferIn, bufferIn.getBuffer(flowerGlow), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 0.0f);
            this.getRenderer().render(this.getEntityModel().getModel(MINIPAD_MODEL), entityLivingBaseIn, partialTicks, flowerTranslucent, matrixStackIn, bufferIn, bufferIn.getBuffer(flowerTranslucent), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, (float) Math.pow((time-18000)/5000f, 2));

            this.getRenderer().render(this.getEntityModel().getModel(MINIPAD_MODEL), entityLivingBaseIn, partialTicks, eyesTexture, matrixStackIn, bufferIn, bufferIn.getBuffer(eyesTexture), packedLightIn, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, (float) -Math.pow((time-18000)/5000f, 2) + 1);

            matrixStackIn.popPose();
        }
    }
}
