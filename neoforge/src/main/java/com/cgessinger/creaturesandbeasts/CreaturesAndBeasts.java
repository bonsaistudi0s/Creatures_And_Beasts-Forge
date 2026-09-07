package com.cgessinger.creaturesandbeasts;


import com.cgessinger.creaturesandbeasts.client.CNBClient;
import com.cgessinger.creaturesandbeasts.modules.CNBCreativeTabModule;
import com.cgessinger.creaturesandbeasts.modules.CNBDataComponentTypeModule;
import com.cgessinger.creaturesandbeasts.modules.CNBItemModule;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(value = CreaturesAndBeastsConstants.MOD_ID)
public class CreaturesAndBeasts {
    public CreaturesAndBeasts(IEventBus eventBus) {
        eventBus.addListener(this::onClientSetup);

        CreaturesAndBeastsCommon.init();

        eventBus.addListener(this::registerCreativeTab);

        if (FMLEnvironment.dist.isClient()) {
            CNBClient.init();
        }
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        ItemProperties.register(CNBItemModule.CACTEM_SPEAR.get(), ResourceLocation.withDefaultNamespace("throwing"), (item, resourceLocation, entity, itemPropertyFunction) -> entity != null && entity.isUsingItem() && entity.getUseItem() == item ? 1.0F : 0.0F);
        ItemProperties.register(CNBItemModule.CINDER_SWORD.get(), ResourceLocation.fromNamespaceAndPath(CreaturesAndBeastsConstants.MOD_ID, "imbue_level"), (item, resourcelocation, entity, itemPropertyFunction) -> item.getOrDefault(CNBDataComponentTypeModule.IMBUE_LEVEL, 0));
    }

    public void registerCreativeTab(RegisterEvent event) {
        if (event.getRegistry().equals(BuiltInRegistries.CREATIVE_MODE_TAB)) {
            CNBCreativeTabModule.load();
        }
    }
}