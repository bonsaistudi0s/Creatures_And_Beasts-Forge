package com.cgessinger.creaturesandbeasts.client;

import com.cgessinger.creaturesandbeasts.capabilities.CinderSwordCapability;
import com.cgessinger.creaturesandbeasts.capabilities.ICinderSwordUpdate;
import com.cgessinger.creaturesandbeasts.init.CNBBlocks;
import com.cgessinger.creaturesandbeasts.init.CNBItems;
import com.cgessinger.creaturesandbeasts.items.CinderSwordItem;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CNBClient {
    public static void init() {
        ItemBlockRenderTypes.setRenderLayer(CNBBlocks.POTTED_PINK_WATERLILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CNBBlocks.POTTED_LIGHT_PINK_WATERLILY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(CNBBlocks.POTTED_YELLOW_WATERLILY.get(), RenderType.cutout());

        ItemProperties.register(CNBItems.CINDER_SWORD.get(), new ResourceLocation("imbued"), (itemStack, clientWorld, livingEntity, integer) ->
                itemStack.getItem() instanceof CinderSwordItem && itemStack.getCapability(CinderSwordCapability.CINDER_SWORD_CAPABILITY).map(ICinderSwordUpdate::getImbued).get() ? 1.0F : 0.0F);
    }
}
