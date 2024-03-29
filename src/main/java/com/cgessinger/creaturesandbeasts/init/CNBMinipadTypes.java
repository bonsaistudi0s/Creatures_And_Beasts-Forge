package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.util.MinipadType;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.cgessinger.creaturesandbeasts.CreaturesAndBeasts.MOD_ID;

public class CNBMinipadTypes {
    // Make sure to change the initial size of this ArrayList when adding new Lizard variants
    private static final List<MinipadType> MINIPAD_TYPES = new ArrayList<>(3);

    public static final MinipadType LIGHT_PINK = registerWithCNBDirectory(MOD_ID, "light_pink", CNBParticleTypes.LIGHT_PINK_MINIPAD_FLOWER);
    public static final MinipadType PINK = registerWithCNBDirectory(MOD_ID, "pink", CNBParticleTypes.PINK_MINIPAD_FLOWER);
    public static final MinipadType YELLOW = registerWithCNBDirectory(MOD_ID, "yellow", CNBParticleTypes.YELLOW_MINIPAD_FLOWER);

    private static MinipadType registerWithCNBDirectory(String namespace, String name, Supplier<SimpleParticleType> particle) {
        return registerWithCNBDirectory(() -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(namespace, name + "_minipad_flower")), () -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(namespace, name + "_minipad_flower_glow")), namespace, name, particle);
    }

    private static MinipadType registerWithCNBDirectory(@Nullable Item shearItem, @Nullable Item glowShearItem, String namespace, String name, Supplier<SimpleParticleType> particle) {
        return registerWithCNBDirectory(() -> shearItem, () -> glowShearItem, namespace, name, particle);
    }

    private static MinipadType registerWithCNBDirectory(@Nullable Supplier<Item> shearItem, @Nullable Supplier<Item> glowShearItem, String namespace, String name, Supplier<SimpleParticleType> particle) {
        return register(new MinipadType(shearItem, glowShearItem, new ResourceLocation(namespace, name), Pair.of(new ResourceLocation(MOD_ID, "textures/entity/minipad/minipad_" + name + ".png"), new ResourceLocation(MOD_ID, "textures/entity/minipad/minipad_" + name + "_glow.png")), particle));
    }

    private static MinipadType register(MinipadType minipadType) {
        MINIPAD_TYPES.add(minipadType);
        return minipadType;
    }

    public static void registerAll() {
        for (MinipadType minipadType : MINIPAD_TYPES) {
            MinipadType.registerMinipadType(minipadType);
        }
    }
}
