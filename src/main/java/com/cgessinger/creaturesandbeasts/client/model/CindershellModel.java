package com.cgessinger.creaturesandbeasts.client.model;

import com.cgessinger.creaturesandbeasts.common.entites.CindershellEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class CindershellModel
    extends EntityModel<CindershellEntity>
{
    private final ModelRenderer bone;

    private final ModelRenderer bone2;

    public final ModelRenderer Head;

    private final ModelRenderer Jaw;

    public final ModelRenderer bone4;

    private final ModelRenderer Tail;

    private final ModelRenderer bone3;

    private final ModelRenderer BL;

    private final ModelRenderer BR;

    private final ModelRenderer FL;

    private final ModelRenderer FR;

    public CindershellModel()
    {
        textureWidth = 128;
        textureHeight = 128;

        bone = new ModelRenderer( this );
        bone.setRotationPoint( 0.0F, 24.0F, 0.0F );
        bone.setTextureOffset( 0, 38 ).addBox( -7.0F, -23.0F, -7.0F, 14.0F, 3.0F, 14.0F, 0.0F, false );
        bone.setTextureOffset( 0, 0 ).addBox( -10.0F, -20.0F, -12.0F, 20.0F, 15.0F, 23.0F, 0.0F, false );

        bone2 = new ModelRenderer( this );
        bone2.setRotationPoint( 0.0F, 0.0F, 0.0F );
        bone.addChild( bone2 );

        Head = new ModelRenderer( this );
        Head.setRotationPoint( 0.0F, -10.0F, -10.0F );
        bone2.addChild( Head );

        Jaw = new ModelRenderer( this );
        Jaw.setRotationPoint( 0.0F, 0.3F, -2.1F );
        Head.addChild( Jaw );
        setRotationAngle( Jaw, 0.1745F, 0.0F, 0.0F );
        Jaw.setTextureOffset( 44, 44 ).addBox( -3.5F, -1.0F, -10.77F, 7.0F, 4.0F, 12.0F, 0.0F, false );

        bone4 = new ModelRenderer( this );
        bone4.setRotationPoint( 0.0F, 0.0F, 0.0F );
        Head.addChild( bone4 );
        setRotationAngle( bone4, 0.0873F, 0.0F, 0.0F );
        bone4.setTextureOffset( 0, 8 ).addBox( -1.5F, -6.0F, -11.0F, 3.0F, 3.0F, 3.0F, 0.0F, false );
        bone4.setTextureOffset( 0, 55 ).addBox( -3.0F, -2.98F, -12.0F, 6.0F, 4.0F, 12.0F, 0.0F, false );

        Tail = new ModelRenderer( this );
        Tail.setRotationPoint( 0.0F, -9.0F, 9.0F );
        bone.addChild( Tail );

        bone3 = new ModelRenderer( this );
        bone3.setRotationPoint( 0.0F, 0.0F, 1.0F );
        Tail.addChild( bone3 );
        setRotationAngle( bone3, -0.0873F, 0.0F, 0.0F );
        bone3.setTextureOffset( 36, 60 ).addBox( -4.0F, -3.0F, -1.0F, 8.0F, 5.0F, 8.0F, 0.0F, false );
        bone3.setTextureOffset( 0, 0 ).addBox( -2.0F, -3.0F, 7.0F, 4.0F, 2.0F, 6.0F, 0.0F, false );

        BL = new ModelRenderer( this );
        BL.setRotationPoint( 9.0F, -8.0F, 9.5F );
        bone.addChild( BL );
        setRotationAngle( BL, 0.0F, 0.0F, -0.0873F );
        BL.setTextureOffset( 63, 0 ).addBox( -3.0F, -1.0F, -3.5F, 6.0F, 9.0F, 6.0F, 0.0F, false );

        BR = new ModelRenderer( this );
        BR.setRotationPoint( -9.0F, -8.0F, 9.5F );
        bone.addChild( BR );
        setRotationAngle( BR, 0.0F, 0.0F, 0.0873F );
        BR.setTextureOffset( 63, 0 ).addBox( -3.0F, -1.0F, -3.5F, 6.0F, 9.0F, 6.0F, 0.0F, true );

        FL = new ModelRenderer( this );
        FL.setRotationPoint( 9.0F, -8.0F, -11.5F );
        bone.addChild( FL );
        setRotationAngle( FL, -0.0873F, 0.0F, -0.0873F );
        FL.setTextureOffset( 63, 0 ).addBox( -4.0F, -1.0F, -2.5F, 6.0F, 9.0F, 6.0F, 0.0F, false );

        FR = new ModelRenderer( this );
        FR.setRotationPoint( -9.0F, -8.0F, -11.5F );
        bone.addChild( FR );
        setRotationAngle( FR, -0.0873F, 0.0F, 0.0873F );
        FR.setTextureOffset( 64, 1 ).addBox( -2.0F, -1.0F, -1.5F, 6.0F, 9.0F, 5.0F, 0.0F, true );
    }

    @Override
    public void setRotationAngles( CindershellEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
                                   float netHeadYaw, float headPitch )
    {
        this.Head.rotateAngleX = headPitch * ( (float) Math.PI / 180F );
        this.Head.rotateAngleY = netHeadYaw * ( (float) Math.PI / 180F );
        this.BR.rotateAngleX = MathHelper.cos( limbSwing * 0.6662F ) * 2 * limbSwingAmount;
        this.BL.rotateAngleX = MathHelper.cos( limbSwing * 0.6662F + (float) Math.PI ) * 2 * limbSwingAmount;
        this.FR.rotateAngleX = MathHelper.cos( limbSwing * 0.6662F + (float) Math.PI ) * 2 * limbSwingAmount;
        this.FL.rotateAngleX = MathHelper.cos( limbSwing * 0.6662F ) * 2 * limbSwingAmount;
        this.Tail.rotateAngleX = MathHelper.cos( ageInTicks / 15 ) * 0.15F;
        
        if (entityIn.getAnimationHandler("eat_controller").isAnimating())
        {
            this.Head.rotateAngleX = MathHelper.cos( ageInTicks / 3 ) * 0.07F;
        }
    }

    public void setRotationAngle( ModelRenderer modelRenderer, float x, float y, float z )
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void render( MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
                        float red, float green, float blue, float alpha )
    {
        bone.render( matrixStackIn, bufferIn, packedLightIn, packedOverlayIn );
    }
}