package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.client.model.SporelingModel;
import com.cgessinger.creaturesandbeasts.common.entites.AbstractSporelingEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class SporelingRender<T extends AbstractSporelingEntity> extends GeoEntityRenderer<T>
{
    private IRenderTypeBuffer rtb;
    private ResourceLocation whTexture;
    private ItemStack heldItem;

	public SporelingRender (EntityRendererManager renderManager)
	{
		super(renderManager, new SporelingModel<>());
		this.shadowSize = 0.4F;
	}

    @Override
    public void renderEarly( T animatable, MatrixStack stackIn, float ticks, IRenderTypeBuffer renderTypeBuffer,
                             IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red,
                             float green, float blue, float partialTicks )
    {
        this.rtb = renderTypeBuffer;         
        this.whTexture = this.getTextureLocation(animatable);
        this.heldItem = animatable.getHolding();
        
        super.renderEarly( animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red,
                           green, blue, partialTicks );
    }

    @Override
    public void renderRecursively( GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn,
                                   int packedOverlayIn, float red, float green, float blue, float alpha )
    {
        if(bone.getName().equals("itemHolder"))
        {
            stack.push();
            stack.rotate(Vector3f.XP.rotationDegrees(-40));
            //stack.rotate(Vector3f.ZP.rotationDegrees(15));
            stack.translate( bone.getPivotX()/12.4F, bone.getPivotY()/11, bone.getPivotZ()/13 );
            stack.scale(0.5f, 0.5f, 0.5f);
            Minecraft.getInstance().getItemRenderer().renderItem(this.heldItem, TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
            stack.pop(); 
            
            //restore the render buffer - GeckoLib expects this state otherwise you'll have weird texture issues
            bufferIn = rtb.getBuffer(RenderType.getEntitySmoothCutout(this.whTexture));
        }

        super.renderRecursively( bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha );
    }

    @Override
    protected void applyRotations( T entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw,
                                   float partialTicks )
    {
        super.applyRotations( entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks );

        if(!entityLiving.getHolding().isEmpty())
        {

        }
    }
}
