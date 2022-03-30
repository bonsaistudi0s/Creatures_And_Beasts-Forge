package com.cgessinger.creaturesandbeasts.events;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.entity.render.CindershellRender;
import com.cgessinger.creaturesandbeasts.client.entity.render.GrebeRender;
import com.cgessinger.creaturesandbeasts.client.entity.render.LilytadRender;
import com.cgessinger.creaturesandbeasts.client.entity.render.LizardRender;
import com.cgessinger.creaturesandbeasts.client.entity.render.SporelingRender;
import com.cgessinger.creaturesandbeasts.client.entity.render.YetiRender;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.items.ModSpawnEggItem;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreaturesAndBeasts.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CNBEntityTypes.LITTLE_GREBE.get(), GrebeRender::new);
        event.registerEntityRenderer(CNBEntityTypes.LIZARD.get(), LizardRender::new);
        event.registerEntityRenderer(CNBEntityTypes.CINDERSHELL.get(), CindershellRender::new);
        event.registerEntityRenderer(CNBEntityTypes.LILYTAD.get(), LilytadRender::new);
        event.registerEntityRenderer(CNBEntityTypes.FRIENDLY_SPORELING.get(), SporelingRender::new);
        event.registerEntityRenderer(CNBEntityTypes.HOSTILE_SPORELING.get(), SporelingRender::new);
        event.registerEntityRenderer(CNBEntityTypes.NEUTRAL_SPORELING.get(), SporelingRender::new);
        event.registerEntityRenderer(CNBEntityTypes.YETI.get(), YetiRender::new);
        event.registerEntityRenderer(CNBEntityTypes.LIZARD_EGG.get(), manager -> new ThrownItemRenderer<>(manager, 1.0F, true));
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
        ModSpawnEggItem.initSpawnEggs();
    }
}
