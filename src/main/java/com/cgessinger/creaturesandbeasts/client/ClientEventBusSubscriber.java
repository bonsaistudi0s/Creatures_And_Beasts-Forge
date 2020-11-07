package com.cgessinger.creaturesandbeasts.client;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.render.CyndershellRender;
import com.cgessinger.creaturesandbeasts.client.render.LittleGrebeChickRender;
import com.cgessinger.creaturesandbeasts.client.render.LittleGrebeRender;
import com.cgessinger.creaturesandbeasts.client.render.LizardRender;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
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
	}
}
