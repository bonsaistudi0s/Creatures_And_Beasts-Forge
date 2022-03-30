package com.cgessinger.creaturesandbeasts.client;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.render.*;
import com.cgessinger.creaturesandbeasts.common.entites.projectiles.LizardEggEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.items.ModSpawnEggItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreaturesAndBeasts.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.LITTLE_GREBE.get(), GrebeRender::new);
        event.registerEntityRenderer(ModEntityTypes.LIZARD.get(), LizardRender::new);
        event.registerEntityRenderer(ModEntityTypes.CINDERSHELL.get(), CindershellRender::new);
        event.registerEntityRenderer(ModEntityTypes.LILYTAD.get(), LilytadRender::new);
        event.registerEntityRenderer(ModEntityTypes.FRIENDLY_SPORELING.get(), SporelingRender::new);
        event.registerEntityRenderer(ModEntityTypes.HOSTILE_SPORELING.get(), SporelingRender::new);
        event.registerEntityRenderer(ModEntityTypes.NEUTRAL_SPORELING.get(), SporelingRender::new);
        event.registerEntityRenderer(ModEntityTypes.YETI.get(), YetiRender::new);
        event.registerEntityRenderer(ModEntityTypes.LIZARD_EGG.get(),
                manager -> new ThrownItemRenderer<LizardEggEntity>(manager, 1.0F, true));
    }

	@SubscribeEvent
	public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event)
	{
		ModSpawnEggItem.initSpawnEggs();
	}
}
