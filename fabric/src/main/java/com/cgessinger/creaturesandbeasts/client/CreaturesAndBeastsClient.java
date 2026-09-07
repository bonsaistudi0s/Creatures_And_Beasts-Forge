package com.cgessinger.creaturesandbeasts.client;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeastsConstants;
import com.cgessinger.creaturesandbeasts.modules.CNBBlockModule;
import com.cgessinger.creaturesandbeasts.modules.CNBDataComponentTypeModule;
import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CreaturesAndBeastsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CNBClient.init();

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                CNBBlockModule.LIGHT_PINK_WATERLILY_BLOCK.get(),
                CNBBlockModule.PINK_WATERLILY_BLOCK.get(),
                CNBBlockModule.YELLOW_WATERLILY_BLOCK.get(),
                CNBBlockModule.POTTED_LIGHT_PINK_WATERLILY.get(),
                CNBBlockModule.POTTED_PINK_WATERLILY.get(),
                CNBBlockModule.POTTED_YELLOW_WATERLILY.get());

        ItemProperties.register(CNBItemModule.CACTEM_SPEAR.get(), ResourceLocation.withDefaultNamespace("throwing"), (item, resourceLocation, entity, itemPropertyFunction) -> entity != null && entity.isUsingItem() && entity.getUseItem() == item ? 1.0F : 0.0F);
        ItemProperties.register(CNBItemModule.CINDER_SWORD.get(), ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "imbue_level"), new ClampedItemPropertyFunction() {
            @Override
            public float call(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
                return Mth.clamp(this.unclampedCall(itemStack, clientLevel, livingEntity, i), 0.0F, 4.0F);
            }

            @Override
            public float unclampedCall(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
                return itemStack.getOrDefault(CNBDataComponentTypeModule.IMBUE_LEVEL, 0);
            }
        });
    }
}
