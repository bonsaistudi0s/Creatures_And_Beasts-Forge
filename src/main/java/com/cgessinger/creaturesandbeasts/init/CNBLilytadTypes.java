package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.util.LilytadType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.cgessinger.creaturesandbeasts.CreaturesAndBeasts.MOD_ID;

public class CNBLilytadTypes {
    // Make sure to change the initial size of this ArrayList when adding new Lizard variants
    private static final List<LilytadType> LILYTAD_TYPES = new ArrayList<>(3);

    public static final LilytadType LIGHT_PINK = registerWithCNBDirectory(MOD_ID, "light_pink");
    public static final LilytadType PINK = registerWithCNBDirectory(MOD_ID, "pink");
    public static final LilytadType YELLOW = registerWithCNBDirectory(MOD_ID, "yellow");


    private static LilytadType registerWithCNBDirectory(String namespace, String name) {
        return registerWithCNBDirectory(() -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(namespace, "lilytad_flower_" + name)), namespace, name);
    }

    private static LilytadType registerWithCNBDirectory(@Nullable Item shearItem, String namespace, String name) {
        return registerWithCNBDirectory(() -> shearItem, namespace, name);
    }

    private static LilytadType registerWithCNBDirectory(@Nullable Supplier<Item> shearItem, String namespace, String name) {
        return register(new LilytadType(shearItem, new ResourceLocation(namespace, name), new ResourceLocation(MOD_ID, "textures/entity/lilytad/lilytad_" + name + ".png")));

    }

    private static LilytadType register(LilytadType lilytadType) {
        LILYTAD_TYPES.add(lilytadType);
        return lilytadType;
    }

    public static void registerAll() {
        for (LilytadType lilytadType : LILYTAD_TYPES) {
            LilytadType.registerLilytadType(lilytadType);
        }
    }
}
