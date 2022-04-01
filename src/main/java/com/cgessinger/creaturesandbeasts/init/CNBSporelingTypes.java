package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.util.SporelingType;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.cgessinger.creaturesandbeasts.CreaturesAndBeasts.MOD_ID;
import static com.cgessinger.creaturesandbeasts.util.SporelingType.SporelingHostility.FRIENDLY;
import static com.cgessinger.creaturesandbeasts.util.SporelingType.SporelingHostility.HOSTILE;
import static com.cgessinger.creaturesandbeasts.util.SporelingType.SporelingHostility.NEUTRAL;

public class CNBSporelingTypes {
    // Make sure to change the initial size of this ArrayList when adding new sporeling variants
    private static final List<SporelingType> SPORELING_TYPES = new ArrayList<>(6);

    public static final SporelingType RED_OVERWORLD = registerWithCNBDirectory(MOD_ID, "red_overworld", "red_overworld", FRIENDLY);
    public static final SporelingType BROWN_OVERWORLD = registerWithCNBDirectory(MOD_ID, "brown_overworld", "brown_overworld", FRIENDLY);
    public static final SporelingType RED_NETHER = registerWithCNBDirectory(MOD_ID, "red_nether", "red_nether", HOSTILE);
    public static final SporelingType BROWN_NETHER = registerWithCNBDirectory(MOD_ID, "brown_nether", "brown_nether", HOSTILE);
    public static final SporelingType WARPED_FUNGUS = registerWithCNBDirectory(MOD_ID, "warped_fungus", "nether", NEUTRAL);
    public static final SporelingType CRIMSON_FUNGUS = registerWithCNBDirectory(MOD_ID, "crimson_fungus", "nether", NEUTRAL);

    private static SporelingType registerWithCNBDirectory(String namespace, String name, String modelName, SporelingType.SporelingHostility hostility) {
        return register(new SporelingType(new ResourceLocation(namespace, name), new ResourceLocation(MOD_ID, "geo/sporeling/sporeling_" + modelName + ".geo.json"), new ResourceLocation(MOD_ID, "textures/entity/sporeling/sporeling_" + name + ".png"), hostility));

    }

    private static SporelingType register(SporelingType sporelingType) {
        SPORELING_TYPES.add(sporelingType);
        return sporelingType;
    }

    public static void registerAll() {
        for (SporelingType sporelingType : SPORELING_TYPES) {
            SporelingType.registerSporelingType(sporelingType);
        }
    }
}
