package com.cgessinger.creaturesandbeasts.client.entity;

import com.cgessinger.creaturesandbeasts.init.CNBBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CNBClient {
    public static void init() {
        ItemBlockRenderTypes.setRenderLayer(CNBBlocks.POTTED_LILYTAD_FLOWER.get(), RenderType.cutout());
    }
}
