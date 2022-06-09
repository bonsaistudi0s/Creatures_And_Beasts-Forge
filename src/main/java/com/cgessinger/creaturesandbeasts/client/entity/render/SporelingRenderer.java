package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.client.entity.model.SporelingModel;
import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.geo.render.built.GeoBone;

@OnlyIn(Dist.CLIENT)
public class SporelingRenderer extends LeadableGeoEntityRenderer<SporelingEntity> {

    public SporelingRenderer(EntityRendererProvider.Context context) {
        super(context, new SporelingModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public void render(SporelingEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        boolean isRidingCrouch = false;
        boolean isRidingAttacking = false;

        if (entity.getVehicle() instanceof Player player && player.isCrouching()) {
            stack.pushPose();
            float yRotLerped = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
            stack.mulPose(Vector3f.XP.rotationDegrees(-25.0F * Mth.cos(yRotLerped * Mth.DEG_TO_RAD)));
            stack.mulPose(Vector3f.ZP.rotationDegrees(-25.0F * Mth.sin(yRotLerped * Mth.DEG_TO_RAD)));
            isRidingCrouch = true;
        }

        if (entity.getVehicle() instanceof Player player && player.getAttackAnim(partialTicks) > 0) {
            stack.pushPose();
            float rotation = player.getAttackAnim(partialTicks);
            stack.mulPose(Vector3f.YP.rotationDegrees(-Mth.sin(Mth.sqrt(rotation) * ((float)Math.PI * 2F)) * 0.2F * Mth.RAD_TO_DEG));
            isRidingAttacking = true;
        }

        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

        if (isRidingCrouch) {
            stack.popPose();
        }
        if (isRidingAttacking) {
            stack.popPose();
        }
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("itemHolder")) {
            stack.pushPose();
            stack.scale(0.5F, 0.5F, 0.5F);
            stack.translate(0.6F, 0.1F, -0.1F);
            stack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(mainHand, TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, 0);
            stack.popPose();

            // restore the render buffer - GeckoLib expects this state otherwise you'll have weird texture issues
            bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
        }

        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
