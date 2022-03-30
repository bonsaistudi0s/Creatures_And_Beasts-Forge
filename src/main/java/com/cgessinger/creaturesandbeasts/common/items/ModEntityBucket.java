package com.cgessinger.creaturesandbeasts.common.items;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;

import net.minecraft.world.item.Item.Properties;

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
    public void inventoryTick( ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected )
    {
        if ( !stack.getOrCreateTag().contains( "age" ) )
        {
            stack.getOrCreateTag().putInt( "age", -24000 );
        }
        super.inventoryTick( stack, worldIn, entityIn, itemSlot, isSelected );
    }

    public void checkExtraContent( Level worldIn, ItemStack p_203792_2_, BlockPos pos )
    {
        if ( worldIn instanceof ServerLevel )
        {
            this.placeCindershell( (ServerLevel) worldIn, p_203792_2_, pos );
        }
    }

    protected void playEmptySound( Player player, LevelAccessor worldIn, BlockPos pos )
    {
        worldIn.playSound( player, pos, SoundEvents.BUCKET_EMPTY_FISH, SoundSource.NEUTRAL, 1.0F, 1.0F );
    }

    private void placeCindershell( ServerLevel worldIn, ItemStack stack, BlockPos pos )
    {
        this.type.get().spawn( worldIn, stack, (Player) null, pos, MobSpawnType.BUCKET, true, false );
    }
}
