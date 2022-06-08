package com.cgessinger.creaturesandbeasts.mixin;

import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Mixin(GeoEntityRenderer.class)
public class MixinGeoEntityRenderer<T extends LivingEntity & IAnimatable> {

    @ModifyVariable(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "STORE"), remap = false)
    private boolean CNB_stopSporelingRotatingOnPlayer(boolean value, T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        if (entity instanceof SporelingEntity && entity.getVehicle() instanceof Player) {
            return false;
        } else {
            return value;
        }
    }
}
