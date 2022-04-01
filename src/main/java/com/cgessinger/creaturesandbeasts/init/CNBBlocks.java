package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.blocks.LizardEggBlock;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CNBBlocks {
    public static final IntegerProperty EGGS_1_6 = IntegerProperty.create("eggs", 1, 6);

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CreaturesAndBeasts.MOD_ID);
    public static final RegistryObject<Block> LILYTAD_FLOWER = BLOCKS.register("lilytad_flower_block", () -> new FlowerBlock(MobEffects.HEAL, 5, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> POTTED_LILYTAD_FLOWER = BLOCKS.register("potted_lilytad_flower", () -> new FlowerPotBlock(LILYTAD_FLOWER.get(), BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));
    public static RegistryObject<Block> LIZARD_EGGS = BLOCKS.register("lizard_egg_block", LizardEggBlock::new);
}
