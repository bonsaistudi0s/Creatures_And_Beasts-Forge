package com.cgessinger.creaturesandbeasts.mixin;

import com.cgessinger.creaturesandbeasts.entities.EndWhaleEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class MixinLivingEntityRenderer<T extends LivingEntity> {
    private boolean shouldSitTemp;

    @Inject(method = "setupRotations(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V", at = @At("RETURN"))
    private void CNB_setupWhaleRidingRotations(T entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci) {
        if (entity.getVehicle() instanceof EndWhaleEntity endWhale) {
            float whaleRotY = endWhale.getViewYRot(partialTicks);
            float playerRotY = entity.getViewYRot(partialTicks);
            float whaleRotX = endWhale.getViewXRot(partialTicks);
            float playerRotX = entity.getViewXRot(partialTicks);
            stack.mulPose(Vector3f.ZP.rotationDegrees(Mth.wrapDegrees(whaleRotY - playerRotY) / 2));
            stack.mulPose(Vector3f.XP.rotationDegrees(Mth.wrapDegrees(whaleRotX - playerRotX)));
        }
    }

    @ModifyVariable(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/Mth;rotLerp(FFF)F", ordinal = 1), ordinal = 0)
    private boolean CNB_stopPlayerRotatingOnWhale(boolean value, T entity, float p_115309_, float p_115310_, PoseStack stack, MultiBufferSource bufferIn, int p_115313_) {
        this.shouldSitTemp = value;

        if (entity.getVehicle() instanceof EndWhaleEntity) {
            return false;
        } else {
            return value;
        }
    }

    @ModifyVariable(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/Mth;lerp(FFF)F", ordinal = 0), ordinal = 0)
    private boolean CNB_resetShouldSit(boolean value, T entity, float p_115309_, float p_115310_, PoseStack stack, MultiBufferSource bufferIn, int p_115313_) {
        return this.shouldSitTemp;
    }
}
