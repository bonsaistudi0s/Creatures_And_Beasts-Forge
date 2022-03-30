package com.cgessinger.creaturesandbeasts.client.model;

import com.cgessinger.creaturesandbeasts.common.entites.CindershellEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class CindershellModel
    extends EntityModel<CindershellEntity>
{
    private final ModelPart bone;

    private final ModelPart bone2;

    public final ModelPart Head;

    private final ModelPart Jaw;

    public final ModelPart bone4;

    private final ModelPart Tail;

    private final ModelPart bone3;

    private final ModelPart BL;

    private final ModelPart BR;

    private final ModelPart FL;

    private final ModelPart FR;

    public CindershellModel()
    {
        texWidth = 128;
        texHeight = 128;

        bone = new ModelPart( this );
        bone.setPos( 0.0F, 24.0F, 0.0F );
        bone.texOffs( 0, 38 ).addBox( -7.0F, -23.0F, -7.0F, 14.0F, 3.0F, 14.0F, 0.0F, false );
        bone.texOffs( 0, 0 ).addBox( -10.0F, -20.0F, -12.0F, 20.0F, 15.0F, 23.0F, 0.0F, false );

        bone2 = new ModelPart( this );
        bone2.setPos( 0.0F, 0.0F, 0.0F );
        bone.addChild( bone2 );

        Head = new ModelPart( this );
        Head.setPos( 0.0F, -10.0F, -10.0F );
        bone2.addChild( Head );

        Jaw = new ModelPart( this );
        Jaw.setPos( 0.0F, 0.3F, -2.1F );
        Head.addChild( Jaw );
        setRotationAngle( Jaw, 0.1745F, 0.0F, 0.0F );
        Jaw.texOffs( 44, 44 ).addBox( -3.5F, -1.0F, -10.77F, 7.0F, 4.0F, 12.0F, 0.0F, false );

        bone4 = new ModelPart( this );
        bone4.setPos( 0.0F, 0.0F, 0.0F );
        Head.addChild( bone4 );
        setRotationAngle( bone4, 0.0873F, 0.0F, 0.0F );
        bone4.texOffs( 0, 8 ).addBox( -1.5F, -6.0F, -11.0F, 3.0F, 3.0F, 3.0F, 0.0F, false );
        bone4.texOffs( 0, 55 ).addBox( -3.0F, -2.98F, -12.0F, 6.0F, 4.0F, 12.0F, 0.0F, false );

        Tail = new ModelPart( this );
        Tail.setPos( 0.0F, -9.0F, 9.0F );
        bone.addChild( Tail );

        bone3 = new ModelPart( this );
        bone3.setPos( 0.0F, 0.0F, 1.0F );
        Tail.addChild( bone3 );
        setRotationAngle( bone3, -0.0873F, 0.0F, 0.0F );
        bone3.texOffs( 36, 60 ).addBox( -4.0F, -3.0F, -1.0F, 8.0F, 5.0F, 8.0F, 0.0F, false );
        bone3.texOffs( 0, 0 ).addBox( -2.0F, -3.0F, 7.0F, 4.0F, 2.0F, 6.0F, 0.0F, false );

        BL = new ModelPart( this );
        BL.setPos( 9.0F, -8.0F, 9.5F );
        bone.addChild( BL );
        setRotationAngle( BL, 0.0F, 0.0F, -0.0873F );
        BL.texOffs( 63, 0 ).addBox( -3.0F, -1.0F, -3.5F, 6.0F, 9.0F, 6.0F, 0.0F, false );

        BR = new ModelPart( this );
        BR.setPos( -9.0F, -8.0F, 9.5F );
        bone.addChild( BR );
        setRotationAngle( BR, 0.0F, 0.0F, 0.0873F );
        BR.texOffs( 63, 0 ).addBox( -3.0F, -1.0F, -3.5F, 6.0F, 9.0F, 6.0F, 0.0F, true );

        FL = new ModelPart( this );
        FL.setPos( 9.0F, -8.0F, -11.5F );
        bone.addChild( FL );
        setRotationAngle( FL, -0.0873F, 0.0F, -0.0873F );
        FL.texOffs( 63, 0 ).addBox( -4.0F, -1.0F, -2.5F, 6.0F, 9.0F, 6.0F, 0.0F, false );

        FR = new ModelPart( this );
        FR.setPos( -9.0F, -8.0F, -11.5F );
        bone.addChild( FR );
        setRotationAngle( FR, -0.0873F, 0.0F, 0.0873F );
        FR.texOffs( 64, 1 ).addBox( -2.0F, -1.0F, -1.5F, 6.0F, 9.0F, 5.0F, 0.0F, true );
    }

    @Override
    public void setupAnim( CindershellEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
                                   float netHeadYaw, float headPitch )
    {
        this.Head.xRot = headPitch * ( (float) Math.PI / 180F );
        this.Head.yRot = netHeadYaw * ( (float) Math.PI / 180F );
        this.BR.xRot = Mth.cos( limbSwing * 0.6662F ) * 2 * limbSwingAmount;
        this.BL.xRot = Mth.cos( limbSwing * 0.6662F + (float) Math.PI ) * 2 * limbSwingAmount;
        this.FR.xRot = Mth.cos( limbSwing * 0.6662F + (float) Math.PI ) * 2 * limbSwingAmount;
        this.FL.xRot = Mth.cos( limbSwing * 0.6662F ) * 2 * limbSwingAmount;
        this.Tail.xRot = Mth.cos( ageInTicks / 15 ) * 0.15F;
        
        if (entityIn.getAnimationHandler("eat_controller").isAnimating())
        {
            this.Head.xRot = Mth.cos( ageInTicks / 3 ) * 0.07F;
        }
    }

    public void setRotationAngle( ModelPart modelRenderer, float x, float y, float z )
    {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    @Override
    public void renderToBuffer( PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn,
                        float red, float green, float blue, float alpha )
    {
        bone.render( matrixStackIn, bufferIn, packedLightIn, packedOverlayIn );
    }
}