package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@OnlyIn(Dist.CLIENT)
public class CindershellGlowLayer extends GeoLayerRenderer<CindershellEntity> {
    private static final ResourceLocation GLOW_LAYER = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/cindershell/cindershell_glow.png");
    private static final ResourceLocation CINDERSHELL_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/cindershell/cindershell.geo.json");
    private static final ResourceLocation CINDERSHELL_FURNACE_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/cindershell/cindershell_furnace.geo.json");

    public CindershellGlowLayer(IGeoRenderer<CindershellEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, CindershellEntity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entityLivingBaseIn.isBaby()) {
            RenderType renderType = RenderType.eyes(GLOW_LAYER);
            matrixStackIn.pushPose();
            this.getRenderer().render(this.getEntityModel().getModel(entityLivingBaseIn.hasFurnace() ? CINDERSHELL_FURNACE_MODEL : CINDERSHELL_MODEL), entityLivingBaseIn, partialTicks, renderType, matrixStackIn, bufferIn, bufferIn.getBuffer(renderType), packedLightIn, LivingEntityRenderer.getOverlayCoords(entityLivingBaseIn, 0.0F), 1f, 1f, 1f, 1f);
            matrixStackIn.popPose();
        }
    }

    @Override
    public RenderType getRenderType(ResourceLocation textureLocation) {
        return RenderType.eyes(textureLocation);
    }
}
