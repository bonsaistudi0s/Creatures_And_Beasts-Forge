package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.client.entity.model.SporelingModel;
import com.cgessinger.creaturesandbeasts.entities.AbstractSporelingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class SporelingRender<T extends AbstractSporelingEntity> extends GeoEntityRenderer<T> {
    private MultiBufferSource rtb;
    private ResourceLocation whTexture;
    private ItemStack heldItem;

    public SporelingRender(EntityRendererProvider.Context context) {
        super(context, new SporelingModel<>());
        this.shadowRadius = 0.4F;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return getTextureLocation(entity);
    }

    @Override
    public void renderEarly(T animatable, PoseStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.rtb = renderTypeBuffer;
        this.whTexture = this.getTextureLocation(animatable);
        this.heldItem = animatable.getHolding();

        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("itemHolder")) {
            stack.pushPose();
            stack.mulPose(Vector3f.XP.rotationDegrees(-90));
            stack.translate(-0.3, 0, 0);
            stack.scale(0.5f, 0.5f, 0.5f);
            Minecraft.getInstance().getItemRenderer().renderStatic(this.heldItem, TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.popPose();

            // restore the render buffer - GeckoLib expects this state otherwise you'll have weird texture issues
            bufferIn = rtb.getBuffer(RenderType.entitySmoothCutout(this.whTexture));
        }

        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
