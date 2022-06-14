package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CNBPaintingTypes {
    public static final DeferredRegister<PaintingVariant> PAINTINGS = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, CreaturesAndBeasts.MOD_ID);

    public static final RegistryObject<PaintingVariant> LILYTAD_PAINTING = PAINTINGS.register("lilytad", () -> new PaintingVariant(16, 16));
}
