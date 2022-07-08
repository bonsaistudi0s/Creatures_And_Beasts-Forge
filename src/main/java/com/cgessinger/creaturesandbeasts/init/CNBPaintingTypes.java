package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CNBPaintingTypes {
    public static final DeferredRegister<Motive> PAINTINGS = DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, CreaturesAndBeasts.MOD_ID);

    public static final RegistryObject<Motive> LILYTAD_PAINTING = PAINTINGS.register("lilytad", () -> new Motive(16, 16));
}
