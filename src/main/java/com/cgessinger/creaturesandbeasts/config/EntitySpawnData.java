package com.cgessinger.creaturesandbeasts.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlFormat;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;

public record EntitySpawnData(ResourceLocation entityType, ResourceLocation biome, int spawnWeight, int minCount, int maxCount) {

    public ResourceLocation getEntityTypeLocation() {
        return entityType;
    }

    @Nullable
    public EntityType<? extends Entity> getEntityType() {
        return ForgeRegistries.ENTITIES.getValue(this.getEntityTypeLocation());
    }

    public ResourceLocation getBiomeLocation() {
        return biome;
    }

    @Nullable
    public ResourceKey<Biome> getBiome() {
        return ResourceKey.create(Registry.BIOME_REGISTRY, biome);
    }

    public int getMinCount() {
        return minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getSpawnWeight() {
        return spawnWeight;
    }

    public Config toConfig() {
        CommentedConfig config = CommentedConfig.of(() -> new HashMap<>(5), TomlFormat.instance());

        config.set("entityType", this.getEntityTypeLocation().toString());
        config.set("biome", this.getBiomeLocation().toString());
        config.set("spawnWeight", this.getSpawnWeight());
        config.set("minCount", this.getMinCount());
        config.set("maxCount", this.getMaxCount());

        config.setComment("entityType", " Determines what entity this spawn is applied to.");
        config.setComment("biome", " Determines what biome this entity should spawn in.");
        config.setComment("spawnWeight", " Determines the spawn weight of the entity.\n Range: [0, 1000000]");
        config.setComment("minCount", " Determines the minimum number of entities to spawn.\n Range: [0, 1000000]");
        config.setComment("maxCount", " Determines the maximum number of entities to spawn.\n Range: [0, 1000000]");

        return config;
    }

    @Override
    public String toString() {
        return "SpawnData [entityType=" + entityType + ", biome=" + biome + ", minCount=" + minCount + ", maxCount=" + maxCount + ", spawnWeight=" + spawnWeight + "]";
    }

    public static EntitySpawnData fromConfig(Config config) {
        return new EntitySpawnData(
                ResourceLocation.tryParse(config.get("entityType")),
                ResourceLocation.tryParse(config.get("biome")),
                config.getInt("spawnWeight"),
                config.getInt("minCount"),
                config.getInt("maxCount"));
    }

    public static EntitySpawnData of(EntityType<? extends Entity> entityType, ResourceKey<Biome> biome, int spawnWeight, int minCount, int maxCount) {
        return new EntitySpawnData(ForgeRegistries.ENTITIES.getKey(entityType), biome.location(), spawnWeight, minCount, maxCount);
    }

    public static EntitySpawnData of(ResourceLocation entityType, ResourceKey<Biome> biome, int spawnWeight, int minCount, int maxCount) {
        return new EntitySpawnData(entityType, biome.location(), spawnWeight, minCount, maxCount);
    }

    public static EntitySpawnData of(ResourceLocation entityType, ResourceLocation biome, int spawnWeight, int minCount, int maxCount) {
        return new EntitySpawnData(entityType, biome, spawnWeight, minCount, maxCount);
    }
}
