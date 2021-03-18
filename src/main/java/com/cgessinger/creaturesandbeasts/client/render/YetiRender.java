package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.client.model.YetiModel;
import com.cgessinger.creaturesandbeasts.common.entites.YetiEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

@OnlyIn( Dist.CLIENT )
public class YetiRender<T extends YetiEntity>
    extends GeoEntityRenderer<T>
{
    private IRenderTypeBuffer rtb;

    private ResourceLocation whTexture;

    private ItemStack renderItem;

    private boolean isChild;

    public YetiRender( EntityRendererManager renderManager )
    {
        super( renderManager, new YetiModel<>() );
        this.shadowSize = 0.7F;
    }

    @Override
    public void renderEarly( T animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer,
                             IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red,
                             float green, float blue, float partialTicks )
    {
        this.rtb = renderTypeBuffer;
        this.whTexture = this.getTextureLocation( animatable );
        this.renderItem = animatable.getHolding();
        this.isChild = animatable.isChild();

        super.renderEarly( animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                           red, green, blue, partialTicks );
    }

    @Override
    public void renderRecursively( GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn,
                                   int packedOverlayIn, float red, float green, float blue, float alpha )
    {
        if ( renderItem != ItemStack.EMPTY )
        {
            if ( bone.getName().equals( "itemHolder" ) )
            {
                stack.push();
                if ( isChild )
                {
                    stack.translate( 0, 0.3, 0.2 );
                }
                else
                {

                    stack.translate( 0.25, 0.6, 0 );
                }
                Minecraft.getInstance().getItemRenderer().renderItem( renderItem, TransformType.THIRD_PERSON_LEFT_HAND,
                                                                      packedLightIn, packedOverlayIn, stack, this.rtb );
                stack.pop();

                // restore the render buffer - GeckoLib expects this state otherwise you'll have weird texture issues
                bufferIn = rtb.getBuffer( RenderType.getEntitySmoothCutout( this.whTexture ) );
            }
        }

        super.renderRecursively( bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha );
    }

    @Override
    public RenderType getRenderType( T animatable, float partialTicks, MatrixStack stack,
                                     @Nullable IRenderTypeBuffer renderTypeBuffer,
                                     @Nullable IVertexBuilder vertexBuilder, int packedLightIn,
                                     ResourceLocation textureLocation )
    {
        return RenderType.getEntityTranslucent( getTextureLocation( animatable ) );
    }
}
