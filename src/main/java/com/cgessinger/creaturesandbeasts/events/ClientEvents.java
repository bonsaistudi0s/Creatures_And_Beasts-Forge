package com.cgessinger.creaturesandbeasts.events;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.entity.render.CindershellRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.LilytadRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.LittleGrebeRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.LizardRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.MinipadRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.SporelingRenderer;
import com.cgessinger.creaturesandbeasts.client.entity.render.YetiRenderer;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.items.CNBSpawnEggItem;
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
        event.registerEntityRenderer(CNBEntityTypes.LITTLE_GREBE.get(), LittleGrebeRenderer::new);
        event.registerEntityRenderer(CNBEntityTypes.LIZARD.get(), LizardRenderer::new);
        event.registerEntityRenderer(CNBEntityTypes.CINDERSHELL.get(), CindershellRenderer::new);
        event.registerEntityRenderer(CNBEntityTypes.LILYTAD.get(), LilytadRenderer::new);
        event.registerEntityRenderer(CNBEntityTypes.SPORELING.get(), SporelingRenderer::new);
        event.registerEntityRenderer(CNBEntityTypes.YETI.get(), YetiRenderer::new);
        event.registerEntityRenderer(CNBEntityTypes.MINIPAD.get(), MinipadRenderer::new);
        event.registerEntityRenderer(CNBEntityTypes.LIZARD_EGG.get(), manager -> new ThrownItemRenderer<>(manager, 1.0F, true));
    }

    @SubscribeEvent
    public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
        CNBSpawnEggItem.initUnaddedEggs();
    }
}
