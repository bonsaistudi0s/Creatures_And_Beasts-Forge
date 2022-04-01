package com.cgessinger.creaturesandbeasts.blocks;

import com.cgessinger.creaturesandbeasts.entities.LizardEntity;
import com.cgessinger.creaturesandbeasts.init.CNBBlocks;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBItems;
import com.cgessinger.creaturesandbeasts.init.CNBLizardTypes;
import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import com.cgessinger.creaturesandbeasts.util.LizardType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

public class LizardEggBlock extends Block {
    public static final IntegerProperty EGGS = CNBBlocks.EGGS_1_6;
    private static final VoxelShape THREE_EGG_SHAPE = Block.box(6.0D, 0.0D, 6.0D, 12.0D, 2D, 12.0D);
    private LizardType parent1;
    private LizardType parent2;

    public LizardEggBlock() {
        super(BlockBehaviour.Properties.of(Material.EGG, MaterialColor.SAND).strength(0.5F).sound(SoundType.METAL).strength(1.5F, 6.0F).randomTicks().noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(EGGS, 6));
        this.parent1 = CNBLizardTypes.DESERT;
        this.parent2 = CNBLizardTypes.JUNGLE;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EGGS);
    }

    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
        if (this.canGrow(worldIn)) {
            this.removeOneEgg(worldIn, pos, state);
            worldIn.levelEvent(2001, pos, Block.getId(state));
            LizardEntity lizard = CNBEntityTypes.LIZARD.get().create(worldIn);
            lizard.setAge(-24000);
            lizard.setLizardType(lizard.getRandom().nextBoolean() ? this.parent1 : this.parent2);
            lizard.moveTo(pos.getX() + 0.3D, pos.getY(), pos.getZ() + 0.3D, 0.0F, 0.0F);
            worldIn.addFreshEntity(lizard);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return THREE_EGG_SHAPE;
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel worldIn, BlockPos pos, ItemStack stack) {
        for (int i = 0; i < state.getValue(EGGS); i++) {
            popResource(worldIn, pos, new ItemStack(CNBItems.LIZARD_EGG.get()));
        }
    }

    private boolean canGrow(Level worldIn) {
        float f = worldIn.getTimeOfDay(1.0F);
        if ((double) f < 0.69D && (double) f > 0.65D) {
            return true;
        } else {
            return worldIn.random.nextInt(200) == 0;
        }
    }

    private void removeOneEgg(Level worldIn, BlockPos pos, BlockState state) {
        worldIn.playSound(null, pos, CNBSoundEvents.LIZARD_EGG_HATCH.get(), SoundSource.BLOCKS, 1.0F, 0F);
        int i = state.getValue(EGGS);
        if (i <= 1) {
            worldIn.destroyBlock(pos, false);
        } else {
            worldIn.setBlock(pos, state.setValue(EGGS, i - 1), 2);
            worldIn.levelEvent(2001, pos, Block.getId(state));
        }
    }

    public void setParents(LizardType parent1, LizardType parent2) {
        this.parent1 = parent1;
        this.parent2 = parent2;
    }
}
