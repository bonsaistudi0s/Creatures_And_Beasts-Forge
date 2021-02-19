package com.cgessinger.creaturesandbeasts.common.blocks;

import java.util.Random;

import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModBlockRegistry;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.init.ModItems;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class LizardEggBlock
    extends Block
{
    private static final VoxelShape THREE_EGG_SHAPE = Block.makeCuboidShape( 6.0D, 0.0D, 6.0D, 12.0D, 2D, 12.0D );

    public static final IntegerProperty EGGS = ModBlockRegistry.EGGS_1_6;

    public static final IntegerProperty VARIANT_0 = IntegerProperty.create("variant_0", 0, 3);
    public static final IntegerProperty VARIANT_1 = IntegerProperty.create("variant_1", 0, 3);

    public LizardEggBlock()
    {
        super( AbstractBlock.Properties.create( Material.DRAGON_EGG,
                                                MaterialColor.SAND ).hardnessAndResistance( 0.5F ).sound( SoundType.METAL ).harvestLevel( 1 ).tickRandomly().notSolid() );

        this.setDefaultState( this.stateContainer.getBaseState().with( EGGS, Integer.valueOf( 6 ) ).with( VARIANT_0, 0 ).with( VARIANT_1, 2 ) );
    }

    @Override
    protected void fillStateContainer( Builder<Block, BlockState> builder )
    {
        super.fillStateContainer( builder );
        builder.add( EGGS, VARIANT_0, VARIANT_1 );
    }

    public void randomTick( BlockState state, ServerWorld worldIn, BlockPos pos, Random random )
    {
        if ( this.canGrow( worldIn ) )
        {
            this.removeOneEgg( worldIn, pos, state );

            worldIn.playEvent( 2001, pos, Block.getStateId( state ) );
            LizardEntity lizard = ModEntityTypes.LIZARD.get().create( worldIn );
            lizard.setGrowingAge( -24000 );
            lizard.setVariant( lizard.getRNG().nextBoolean() ? state.get(VARIANT_0) : state.get(VARIANT_1) );
            lizard.setLocationAndAngles( pos.getX() + 0.3D, pos.getY(), pos.getZ() + 0.3D, 0.0F, 0.0F );
            worldIn.addEntity( lizard );
        }

    }

    @Override
    public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context )
    {
        return THREE_EGG_SHAPE;
    }

    @Override
    public void spawnAdditionalDrops( BlockState state, ServerWorld worldIn, BlockPos pos, ItemStack stack )
    {
        for ( int i = 0; i < state.get( EGGS ); i++ )
        {
            spawnAsEntity( worldIn, pos, new ItemStack( ModItems.LIZARD_EGG.get() ) );
        }
    }

    private boolean canGrow( World worldIn )
    {
        float f = worldIn.func_242415_f( 1.0F );
        if ( (double) f < 0.69D && (double) f > 0.65D )
        {
            return true;
        }
        else
        {
            return worldIn.rand.nextInt( 200 ) == 0;
        }
    }

    private void removeOneEgg( World worldIn, BlockPos pos, BlockState state )
    {
        worldIn.playSound( null, pos, ModSoundEventTypes.LIZARD_EGG_HATCH.get(), SoundCategory.BLOCKS, 1.0F, 0F);
        int i = state.get( EGGS );
        if ( i <= 1 )
        {
            worldIn.destroyBlock( pos, false );
        }
        else
        {
            worldIn.setBlockState( pos, state.with( EGGS, Integer.valueOf( i - 1 ) ), 2 );
            worldIn.playEvent( 2001, pos, Block.getStateId( state ) );
        }

    }
}
