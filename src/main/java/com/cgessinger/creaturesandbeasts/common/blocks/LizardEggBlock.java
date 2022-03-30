package com.cgessinger.creaturesandbeasts.common.blocks;

import java.util.Random;

import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModBlockRegistry;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.init.ModItems;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class LizardEggBlock
    extends Block
{
    private static final VoxelShape THREE_EGG_SHAPE = Block.box( 6.0D, 0.0D, 6.0D, 12.0D, 2D, 12.0D );

    public static final IntegerProperty EGGS = ModBlockRegistry.EGGS_1_6;

    public static final IntegerProperty VARIANT_0 = IntegerProperty.create("variant_0", 0, 3);
    public static final IntegerProperty VARIANT_1 = IntegerProperty.create("variant_1", 0, 3);

    public LizardEggBlock()
    {
        super( BlockBehaviour.Properties.of( Material.EGG,
                                                MaterialColor.SAND ).strength( 0.5F ).sound( SoundType.METAL ).harvestLevel( 1 ).randomTicks().noOcclusion() );

        this.registerDefaultState( this.stateDefinition.any().setValue( EGGS, Integer.valueOf( 6 ) ).setValue( VARIANT_0, 0 ).setValue( VARIANT_1, 2 ) );
    }

    @Override
    protected void createBlockStateDefinition( Builder<Block, BlockState> builder )
    {
        super.createBlockStateDefinition( builder );
        builder.add( EGGS, VARIANT_0, VARIANT_1 );
    }

    public void randomTick( BlockState state, ServerLevel worldIn, BlockPos pos, Random random )
    {
        if ( this.canGrow( worldIn ) )
        {
            this.removeOneEgg( worldIn, pos, state );

            worldIn.levelEvent( 2001, pos, Block.getId( state ) );
            LizardEntity lizard = ModEntityTypes.LIZARD.get().create( worldIn );
            lizard.setAge( -24000 );
            lizard.setVariant( lizard.getRandom().nextBoolean() ? state.getValue(VARIANT_0) : state.getValue(VARIANT_1) );
            lizard.moveTo( pos.getX() + 0.3D, pos.getY(), pos.getZ() + 0.3D, 0.0F, 0.0F );
            worldIn.addFreshEntity( lizard );
        }

    }

    @Override
    public VoxelShape getShape( BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context )
    {
        return THREE_EGG_SHAPE;
    }

    @Override
    public void spawnAfterBreak( BlockState state, ServerLevel worldIn, BlockPos pos, ItemStack stack )
    {
        for ( int i = 0; i < state.getValue( EGGS ); i++ )
        {
            popResource( worldIn, pos, new ItemStack( ModItems.LIZARD_EGG.get() ) );
        }
    }

    private boolean canGrow( Level worldIn )
    {
        float f = worldIn.getTimeOfDay( 1.0F );
        if ( (double) f < 0.69D && (double) f > 0.65D )
        {
            return true;
        }
        else
        {
            return worldIn.random.nextInt( 200 ) == 0;
        }
    }

    private void removeOneEgg( Level worldIn, BlockPos pos, BlockState state )
    {
        worldIn.playSound( null, pos, ModSoundEventTypes.LIZARD_EGG_HATCH.get(), SoundSource.BLOCKS, 1.0F, 0F);
        int i = state.getValue( EGGS );
        if ( i <= 1 )
        {
            worldIn.destroyBlock( pos, false );
        }
        else
        {
            worldIn.setBlock( pos, state.setValue( EGGS, Integer.valueOf( i - 1 ) ), 2 );
            worldIn.levelEvent( 2001, pos, Block.getId( state ) );
        }

    }
}
