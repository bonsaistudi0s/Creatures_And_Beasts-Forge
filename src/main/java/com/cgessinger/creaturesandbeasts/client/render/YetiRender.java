package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.client.model.YetiModel;
import com.cgessinger.creaturesandbeasts.common.entites.YetiEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

@OnlyIn( Dist.CLIENT )
public class YetiRender<T extends YetiEntity>
    extends GeoEntityRenderer<T>
{
    private MultiBufferSource rtb;

    private ResourceLocation whTexture;

    private ItemStack renderItem;

    private boolean isChild;

    public YetiRender(EntityRendererProvider.Context context)
    {
        super( context, new YetiModel<>() );
        this.shadowRadius = 0.7F;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return getTextureLocation(entity);
    }

    @Override
    public void renderEarly( T animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer,
                             VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red,
                             float green, float blue, float partialTicks )
    {
        this.rtb = renderTypeBuffer;
        this.whTexture = this.getTextureLocation( animatable );
        this.renderItem = animatable.getHolding();
        this.isChild = animatable.isBaby();

        super.renderEarly( animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn,
                           red, green, blue, partialTicks );
    }

    @Override
    public void renderRecursively( GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn,
                                   int packedOverlayIn, float red, float green, float blue, float alpha )
    {
        if ( renderItem != ItemStack.EMPTY )
        {
            if ( bone.getName().equals( "itemHolder" ) )
            {
                stack.pushPose();
                if ( isChild )
                {
                    stack.translate( 0, 0.3, 0.2 );
                }
                else
                {

                    stack.translate( 0.25, 0.6, 0 );
                }
                Minecraft.getInstance().getItemRenderer().renderStatic( renderItem, TransformType.THIRD_PERSON_LEFT_HAND,
                                                                      packedLightIn, packedOverlayIn, stack, this.rtb, 0);
                stack.popPose();

                // restore the render buffer - GeckoLib expects this state otherwise you'll have weird texture issues
                bufferIn = rtb.getBuffer( RenderType.entitySmoothCutout( this.whTexture ) );
            }
        }

        super.renderRecursively( bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha );
    }

    @Override
    public RenderType getRenderType( T animatable, float partialTicks, PoseStack stack,
                                     @Nullable MultiBufferSource renderTypeBuffer,
                                     @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                     ResourceLocation textureLocation )
    {
        return RenderType.entityTranslucent( getTextureLocation( animatable ) );
    }
}
