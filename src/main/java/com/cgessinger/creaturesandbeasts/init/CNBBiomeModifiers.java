package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.world.biome.modifiers.AddCostedSpawnsBiomeModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CNBBiomeModifiers {

    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, CreaturesAndBeasts.MOD_ID);

    public static final RegistryObject<Codec<AddCostedSpawnsBiomeModifier>> ADD_COSTED_SPAWNS = BIOME_MODIFIERS.register("add_costed_spawns", () -> AddCostedSpawnsBiomeModifier.CODEC);

}
