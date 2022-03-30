package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.entity.model.AgeableModelProvider;
import com.cgessinger.creaturesandbeasts.client.entity.model.BabyCindershellModel;
import com.cgessinger.creaturesandbeasts.client.entity.model.CindershellModel;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CindershellGlowLayer extends RenderLayer<CindershellEntity, AgeableModelProvider<CindershellEntity>> {
    private static final RenderType RENDER_TYPE = RenderType.eyes(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/cindershell/cindershell_glow.png"));
    private final AgeableModelProvider<CindershellEntity> cindershellModel;

    public CindershellGlowLayer(RenderLayerParent<CindershellEntity, AgeableModelProvider<CindershellEntity>> entityRendererIn) {
        super(entityRendererIn);
        this.cindershellModel = new AgeableModelProvider<>(new BabyCindershellModel(), new CindershellModel());
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, CindershellEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.isBaby()) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(RENDER_TYPE);
            this.getParentModel().adultModel.renderToBuffer(matrixStackIn, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public AgeableModelProvider<CindershellEntity> getParentModel() {
        return this.cindershellModel;
    }
}
