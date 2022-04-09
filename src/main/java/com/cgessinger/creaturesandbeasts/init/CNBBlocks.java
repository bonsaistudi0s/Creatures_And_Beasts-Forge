package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.blocks.LizardEggBlock;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CNBBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CreaturesAndBeasts.MOD_ID);

    public static final RegistryObject<FlowerBlock> PINK_WATERLILY_BLOCK = BLOCKS.register("pink_waterlily_block", () -> new FlowerBlock(MobEffects.HEAL, 5, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<FlowerPotBlock> POTTED_PINK_WATERLILY = BLOCKS.register("potted_pink_waterlily", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, PINK_WATERLILY_BLOCK, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<FlowerBlock> LIGHT_PINK_WATERLILY_BLOCK = BLOCKS.register("light_pink_waterlily_block", () -> new FlowerBlock(MobEffects.HEAL, 5, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<FlowerPotBlock> POTTED_LIGHT_PINK_WATERLILY = BLOCKS.register("potted_light_pink_waterlily", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, LIGHT_PINK_WATERLILY_BLOCK, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));
    public static final RegistryObject<FlowerBlock> YELLOW_WATERLILY_BLOCK = BLOCKS.register("yellow_waterlily_block", () -> new FlowerBlock(MobEffects.HEAL, 5, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<FlowerPotBlock> POTTED_YELLOW_WATERLILY = BLOCKS.register("potted_yellow_waterlily", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, YELLOW_WATERLILY_BLOCK, BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)));

    public static RegistryObject<Block> LIZARD_EGGS = BLOCKS.register("lizard_egg_block", LizardEggBlock::new);
}
