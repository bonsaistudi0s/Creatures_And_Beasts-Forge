package com.cgessinger.creaturesandbeasts.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class EntityConfig {
    private final ForgeConfigSpec.ConfigValue<Boolean> active;
    private final ForgeConfigSpec.ConfigValue<Integer> spawnWeight;
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> biomes;

    public boolean shouldExist;
    public int spawnRate;
    public List<? extends String> spawnBiomes;

    public EntityConfig(ForgeConfigSpec.ConfigValue<Boolean> act, ForgeConfigSpec.ConfigValue<Integer> spawn, ForgeConfigSpec.ConfigValue<List<? extends String>> biome) {
        active = act;
        spawnWeight = spawn;
        biomes = biome;
    }

    public static EntityConfig createConfigForEntity(ForgeConfigSpec.Builder builder, String name, Boolean dfActive, int dfWeight, List<String> dfBiomes) {
        ForgeConfigSpec.ConfigValue<Boolean> active = builder
                .comment("This defines whether or not the " + name + "s should exist (default: " + dfActive + ", when changing this to false EVERY entity of this type will be deleted!)")
                .translation("cnb.configgui." + name + "_active")
                .define(name + " active", dfActive);

        ForgeConfigSpec.ConfigValue<Integer> spawnWeight = builder
                .comment("This defines the spawn rate of the " + name + "s (default: " + dfWeight + ")")
                .translation("cnb.configgui." + name + "_weight")
                .define(name + " spawn weight", dfWeight);

        ForgeConfigSpec.ConfigValue<List<? extends String>> biomes = builder
                .comment("This defines which biomes the " + name + "s will spawn in")
                .translation("cnb.configgui." + name + "_biomes")
                .defineList(name + " biomes", dfBiomes, o -> o instanceof String);

        return new EntityConfig(active, spawnWeight, biomes);
    }

    public void bake() {
        this.shouldExist = active.get();
        this.spawnRate = spawnWeight.get();
        this.spawnBiomes = biomes.get();
    }
}
