package com.cgessinger.creaturesandbeasts.client;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.model.SporelingModel;
import com.cgessinger.creaturesandbeasts.client.render.*;
import com.cgessinger.creaturesandbeasts.common.entites.CyndershellEntity;
import com.cgessinger.creaturesandbeasts.common.entites.FriendlySporelingEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.items.ModSpawnEggItem;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CreaturesAndBeasts.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber
{
	@SubscribeEvent
	public static void onClientSetup (FMLClientSetupEvent event)
	{
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.LITTLE_GREBE.get(), LittleGrebeRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.LITTLE_GREBE_CHICK.get(), LittleGrebeChickRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.LIZARD.get(), LizardRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.CYNDERSHELL.get(), CyndershellRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.FRIENDLY_SPORELING.get(), SporelingRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.HOSTILE_SPORELING.get(), SporelingRender::new);
	}

	@SubscribeEvent
	public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event)
	{
		ModSpawnEggItem.initSpawnEggs();
	}
}
