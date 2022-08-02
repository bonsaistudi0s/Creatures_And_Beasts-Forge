package com.cgessinger.creaturesandbeasts.world.biome.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.random.Weight;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

import net.minecraftforge.common.world.BiomeModifier.Phase;

public record AddCostedSpawnsBiomeModifier(EntityType<? extends Entity> entityType, List<CostSpawnerData> spawnerData) implements BiomeModifier {

    public static final Codec<AddCostedSpawnsBiomeModifier> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("entityType").forGetter(AddCostedSpawnsBiomeModifier::entityType),
            CostSpawnerData.LIST_CODEC.fieldOf("spawns").forGetter(AddCostedSpawnsBiomeModifier::spawnerData)
    ).apply(builder, AddCostedSpawnsBiomeModifier::new));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();
            for (CostSpawnerData costSpawnerData : this.spawnerData) {
                if (costSpawnerData.biomes().contains(biome)) {
                    spawns.addSpawn(costSpawnerData.category, new MobSpawnSettings.SpawnerData(this.entityType, costSpawnerData.weight, costSpawnerData.min, costSpawnerData.max));

                    if (costSpawnerData.cost > 0.0D && costSpawnerData.budget > 0.0D) {
                        spawns.addMobCharge(this.entityType, costSpawnerData.cost, costSpawnerData.budget);
                    }
                }
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return CODEC;
    }


    private record CostSpawnerData(HolderSet<Biome> biomes, MobCategory category, Weight weight, int min, int max, double cost, double budget) {
        private static final Codec<CostSpawnerData> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Biome.LIST_CODEC.fieldOf("biomes").forGetter(CostSpawnerData::biomes),
                MobCategory.CODEC.fieldOf("category").forGetter(CostSpawnerData::category),
                Weight.CODEC.fieldOf("weight").forGetter(CostSpawnerData::weight),
                Codec.INT.fieldOf("min").forGetter(CostSpawnerData::min),
                Codec.INT.fieldOf("max").forGetter(CostSpawnerData::max),
                Codec.DOUBLE.optionalFieldOf("cost", 0.0D).forGetter(CostSpawnerData::cost),
                Codec.DOUBLE.optionalFieldOf("budget", 0.0D).forGetter(CostSpawnerData::budget)
        ).apply(builder, CostSpawnerData::new));

        private static final Codec<List<CostSpawnerData>> LIST_CODEC = CODEC.listOf();
    }
}
