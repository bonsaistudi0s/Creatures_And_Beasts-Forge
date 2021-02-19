package com.cgessinger.creaturesandbeasts.common.items;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.entites.projectiles.LizardEggEntity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class LizardEgg
    extends Item
{

    public LizardEgg()
    {
        super( new Item.Properties().maxStackSize( 16 ).group( CreaturesAndBeasts.TAB ) );
    }

    public ActionResult<ItemStack> onItemRightClick( World worldIn, PlayerEntity playerIn, Hand handIn )
    {
        ItemStack itemstack = playerIn.getHeldItem( handIn );
        worldIn.playSound( null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
                           SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F,
                           0.4F / ( random.nextFloat() * 0.4F + 0.8F ) );
        if ( !worldIn.isRemote )
        {
            LizardEggEntity eggentity = new LizardEggEntity( worldIn, playerIn );
            eggentity.setItem( itemstack );
            eggentity.func_234612_a_( playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F );
            worldIn.addEntity( eggentity );
        }

        playerIn.addStat( Stats.ITEM_USED.get( this ) );
        if ( !playerIn.abilities.isCreativeMode )
        {
            itemstack.shrink( 1 );
        }

        return ActionResult.func_233538_a_( itemstack, worldIn.isRemote() );
    }
}
