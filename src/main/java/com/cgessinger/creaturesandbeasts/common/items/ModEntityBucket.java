package com.cgessinger.creaturesandbeasts.common.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;

public class ModEntityBucket
    extends BucketItem
{
	private final Lazy<? extends EntityType<?>> type;

    public ModEntityBucket(final RegistryObject<? extends EntityType<?>> entityType, Fluid fluid,  Properties builder )
    {
        super( () -> fluid, builder );
		this.type = Lazy.of(entityType::get);
    }

    @Override
    public void inventoryTick( ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected )
    {
        if ( !stack.getOrCreateTag().contains( "age" ) )
        {
            stack.getOrCreateTag().putInt( "age", -24000 );
        }
        super.inventoryTick( stack, worldIn, entityIn, itemSlot, isSelected );
    }

    public void onLiquidPlaced( World worldIn, ItemStack p_203792_2_, BlockPos pos )
    {
        if ( worldIn instanceof ServerWorld )
        {
            this.placeCindershell( (ServerWorld) worldIn, p_203792_2_, pos );
        }
    }

    protected void playEmptySound( PlayerEntity player, IWorld worldIn, BlockPos pos )
    {
        worldIn.playSound( player, pos, SoundEvents.ITEM_BUCKET_EMPTY_FISH, SoundCategory.NEUTRAL, 1.0F, 1.0F );
    }

    private void placeCindershell( ServerWorld worldIn, ItemStack stack, BlockPos pos )
    {
        this.type.get().spawn( worldIn, stack, (PlayerEntity) null, pos, SpawnReason.BUCKET, true, false );
    }
}
