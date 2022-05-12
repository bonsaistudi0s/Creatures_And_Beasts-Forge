package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
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
    private static final ResourceLocation END_WHALE_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/end_whale/end_whale.geo.json");

    public EndWhaleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EndWhaleModel());
        this.shadowRadius = 1.5F;
    }

    @Override
    public RenderType getRenderType(EndWhaleEntity animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.armorCutoutNoCull(textureLocation);
    }

    @Override
    protected void applyRotations(EndWhaleEntity endWhale, PoseStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(endWhale, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        float whaleRotY = endWhale.getViewYRot(partialTicks);
        float playerRotY = 0;
        float whaleRotX = endWhale.getViewXRot(partialTicks);
        float playerRotX = 0;
        Entity rider = endWhale.getFirstPassenger();

        if (rider != null) {
            playerRotY = rider.getViewYRot(partialTicks);
            playerRotX = rider.getViewXRot(partialTicks);

            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(Mth.wrapDegrees(whaleRotY - playerRotY) / 2));
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(Mth.wrapDegrees(whaleRotX - playerRotX)));
        }
    }
}
