package com.cgessinger.creaturesandbeasts.common.util;

import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeChickEntity;
import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeEntity;
import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class ModEventHandler
{
	@SubscribeEvent
	public static void livingSpawnEvent (LivingSpawnEvent.CheckSpawn event)
	{
		if (event.getEntityLiving() instanceof LittleGrebeEntity && event.getSpawnReason() == SpawnReason.CHUNK_GENERATION)
		{
			IWorld world = event.getWorld();
			int rndInt = event.getEntityLiving().getRNG().nextInt(2);
			if (!world.isRemote() && rndInt == 0)
			{
				LittleGrebeChickEntity child = ModEntityTypes.LITTLE_GREBE_CHICK.get().create(event.getEntityLiving().getEntityWorld());
				child.setPosition(event.getX(), event.getY(), event.getZ());
				world.addEntity(child);
			}
		}
	}

	@SubscribeEvent
	public static void onBlockActivate (PlayerInteractEvent.RightClickBlock event)
	{
			TileEntity te = event.getWorld().getTileEntity(event.getPos());
			Item heldItem = event.getPlayer().getHeldItem(event.getHand()).getItem();
			if (te instanceof JukeboxTileEntity)
			{
				JukeboxTileEntity box = (JukeboxTileEntity) te;
				boolean discOut = box.getRecord() != ItemStack.EMPTY;
				boolean discIn = heldItem instanceof MusicDiscItem;
				if (discOut || discIn)
				{
					List<LizardEntity> lizards = event.getWorld().getEntitiesWithinAABB(LizardEntity.class, event.getPlayer().getBoundingBox().grow(50));
					for (LizardEntity lizard : lizards)
					{
						lizard.setPartying(!discOut);
						/* If discOut is false, disIn must be true. If dicOut is true, discIn will not be checked anymore. So !discOut is enough */
					}
				}
			}
	}
}
