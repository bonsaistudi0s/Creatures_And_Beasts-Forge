package com.cgessinger.creaturesandbeasts.common.config;

import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;

public class EntityConfig 
{
    private final ForgeConfigSpec.ConfigValue<Boolean> active;
    private final ForgeConfigSpec.ConfigValue<Integer> spawnWeight;
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> biomes;

    public boolean shouldExist;
    public int spawnRate;
    public List<? extends String> spawnBiomes;

    public EntityConfig(ForgeConfigSpec.ConfigValue<Boolean> act, ForgeConfigSpec.ConfigValue<Integer> spawn,
            ForgeConfigSpec.ConfigValue<List<? extends String>> biome) 
            {
        active = act;
        spawnWeight = spawn;
        biomes = biome;
    }

    public void bake() 
    {
        this.shouldExist = active.get();
        this.spawnRate = spawnWeight.get();
        this.spawnBiomes = biomes.get();
    }
}
