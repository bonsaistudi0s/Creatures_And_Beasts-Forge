package com.cgessinger.creaturesandbeasts.common.util;

import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeChickEntity;
import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModEventHandler
{
	@SubscribeEvent
	public static void livingSpawnEvent(LivingSpawnEvent.CheckSpawn event)
	{
		if(event.getEntityLiving() instanceof LittleGrebeEntity && event.getSpawnReason() == SpawnReason.CHUNK_GENERATION)
		{
			IWorld world = event.getWorld();
			int rndInt = event.getEntityLiving().getRNG().nextInt(2);
			if(!world.isRemote() && rndInt == 0)
			{
				LittleGrebeChickEntity child = ModEntityTypes.LITTLE_GREBE_CHICK.get().create(event.getEntityLiving().getEntityWorld());
				child.setPosition(event.getX(), event.getY(), event.getZ());
				world.addEntity(child);
			}
		}
	}
}
