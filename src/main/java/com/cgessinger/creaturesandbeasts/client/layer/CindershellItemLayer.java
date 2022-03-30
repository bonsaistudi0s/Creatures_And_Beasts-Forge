package com.cgessinger.creaturesandbeasts.client.layer;

import com.cgessinger.creaturesandbeasts.client.model.AgeableModelProvider;
import com.cgessinger.creaturesandbeasts.client.model.CindershellModel;
import com.cgessinger.creaturesandbeasts.common.entites.CindershellEntity;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;

public class CindershellItemLayer
    extends RenderLayer<CindershellEntity, AgeableModelProvider<CindershellEntity>>
{
    public CindershellItemLayer( RenderLayerParent<CindershellEntity, AgeableModelProvider<CindershellEntity>> entityRendererIn )
    {
        super( entityRendererIn );
    }

    @Override
    public void render( PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn,
                        CindershellEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount,
                        float partialTicks, float ageInTicks, float netHeadYaw, float headPitch )
    {
        ItemStack stack = entitylivingbaseIn.getHolding();
        if ( stack != ItemStack.EMPTY )
        {

            AgeableModelProvider<CindershellEntity> modelProvider = this.getParentModel();
            if ( !entitylivingbaseIn.isBaby() )
            {
                matrixStackIn.pushPose();
                CindershellModel model = (CindershellModel) modelProvider.adultModel;
                model.Head.translateAndRotate( matrixStackIn );
                matrixStackIn.mulPose( Vector3f.XP.rotation( -model.Head.xRot ) );
                matrixStackIn.translate( 0, 1.7 + Mth.cos( ageInTicks / 3 ) * 0.1F, -0.6 + Mth.cos( ageInTicks / 3 ) * 0.1F );
                matrixStackIn.mulPose( Vector3f.XP.rotationDegrees( -90.0F ) );
                Minecraft.getInstance().getItemRenderer().renderStatic( stack, TransformType.THIRD_PERSON_LEFT_HAND,
                                                                      packedLightIn, 0, matrixStackIn, bufferIn );
                matrixStackIn.popPose();
            }
        }
    }
}
