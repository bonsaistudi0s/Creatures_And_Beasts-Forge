package com.cgessinger.creaturesandbeasts.client.layer;

import com.cgessinger.creaturesandbeasts.client.model.AgeableModelProvider;
import com.cgessinger.creaturesandbeasts.client.model.CindershellModel;
import com.cgessinger.creaturesandbeasts.common.entites.CindershellEntity;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class CindershellItemLayer
    extends LayerRenderer<CindershellEntity, AgeableModelProvider<CindershellEntity>>
{
    public CindershellItemLayer( IEntityRenderer<CindershellEntity, AgeableModelProvider<CindershellEntity>> entityRendererIn )
    {
        super( entityRendererIn );
    }

    @Override
    public void render( MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
                        CindershellEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount,
                        float partialTicks, float ageInTicks, float netHeadYaw, float headPitch )
    {
        ItemStack stack = entitylivingbaseIn.getHolding();
        if ( stack != ItemStack.EMPTY )
        {

            AgeableModelProvider<CindershellEntity> modelProvider = this.getEntityModel();
            if ( !entitylivingbaseIn.isChild() )
            {
                matrixStackIn.push();
                CindershellModel model = (CindershellModel) modelProvider.adultModel;
                model.Head.translateRotate( matrixStackIn );
                matrixStackIn.rotate( Vector3f.XP.rotation( -model.Head.rotateAngleX ) );
                matrixStackIn.translate( 0, 1.7 + MathHelper.cos( ageInTicks / 3 ) * 0.1F, -0.6 + MathHelper.cos( ageInTicks / 3 ) * 0.1F );
                matrixStackIn.rotate( Vector3f.XP.rotationDegrees( -90.0F ) );
                Minecraft.getInstance().getItemRenderer().renderItem( stack, TransformType.THIRD_PERSON_LEFT_HAND,
                                                                      packedLightIn, 0, matrixStackIn, bufferIn );
                matrixStackIn.pop();
            }
        }
    }
}
