package com.cgessinger.creaturesandbeasts;

import com.cgessinger.creaturesandbeasts.capabilities.CinderSwordCapability;
import com.cgessinger.creaturesandbeasts.client.CNBClient;
import com.cgessinger.creaturesandbeasts.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.events.CNBEvents;
import com.cgessinger.creaturesandbeasts.init.CNBBiomeModifiers;
import com.cgessinger.creaturesandbeasts.init.CNBBlocks;
import com.cgessinger.creaturesandbeasts.init.CNBContainerTypes;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBItems;
import com.cgessinger.creaturesandbeasts.init.CNBLilytadTypes;
import com.cgessinger.creaturesandbeasts.init.CNBLizardTypes;
import com.cgessinger.creaturesandbeasts.init.CNBLootModifiers;
import com.cgessinger.creaturesandbeasts.init.CNBMinipadTypes;
import com.cgessinger.creaturesandbeasts.init.CNBPaintingTypes;
import com.cgessinger.creaturesandbeasts.init.CNBParticleTypes;
import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import com.cgessinger.creaturesandbeasts.init.CNBSporelingTypes;
import com.cgessinger.creaturesandbeasts.world.gen.ModEntitySpawns;
import com.electronwill.nightconfig.core.io.ParsingException;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.infernalstudios.config.Config;

import java.io.IOException;

@Mod(CreaturesAndBeasts.MOD_ID)
public class CreaturesAndBeasts {
    public static final String MOD_ID = "cnb";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final CreativeModeTab TAB = new CreativeModeTab("cnb_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(CNBItems.GREBE_SPAWN_EGG.get());
        }
    };

    public CreaturesAndBeasts() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::clientSetup);
        eventBus.addListener(this::registerCapabilities);

        CNBParticleTypes.PARTICLE_TYPES.register(eventBus);
        CNBBlocks.BLOCKS.register(eventBus);
        CNBItems.ITEMS.register(eventBus);
        CNBContainerTypes.CONTAINER_TYPES.register(eventBus);
        CNBPaintingTypes.PAINTINGS.register(eventBus);
        CNBSoundEvents.SOUND_EVENTS.register(eventBus);
        CNBEntityTypes.ENTITY_TYPES.register(eventBus);
        CNBLootModifiers.LOOT_MODIFIERS.register(eventBus);
        CNBBiomeModifiers.BIOME_MODIFIERS.register(eventBus);

        CNBSporelingTypes.registerAll();
        CNBLizardTypes.registerAll();
        CNBLilytadTypes.registerAll();
        CNBMinipadTypes.registerAll();

        MinecraftForge.EVENT_BUS.register(new CNBEvents());

        try {
            CNBConfig.CONFIG = Config
                    .builder(FMLPaths.CONFIGDIR.get().resolve("creaturesandbeasts-common.toml"))
                    .loadClass(CNBConfig.class)
                    .build();
        } catch (IllegalStateException | IllegalArgumentException | IOException | ParsingException e) {
            throw new RuntimeException(
                    "Failed to load Creatures and Beasts config" +
                            (e instanceof ParsingException ? ", try fixing/deleting your config file" : ""), e);
        }

        CNBConfig.CONFIG.onReload(stage -> {
            if (stage == Config.ReloadStage.PRE) {
                CreaturesAndBeasts.LOGGER.debug("Reloading Creatures and Beasts config");
            }
        });
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModEntitySpawns.entitySpawnPlacementRegistry();

        // Register New Flowers to be Able to Place in Pots
        FlowerPotBlock flowerPot = (FlowerPotBlock) Blocks.FLOWER_POT;
        flowerPot.addPlant(CNBBlocks.PINK_WATERLILY_BLOCK.getId(), CNBBlocks.POTTED_PINK_WATERLILY);
        flowerPot.addPlant(CNBBlocks.LIGHT_PINK_WATERLILY_BLOCK.getId(), CNBBlocks.POTTED_LIGHT_PINK_WATERLILY);
        flowerPot.addPlant(CNBBlocks.YELLOW_WATERLILY_BLOCK.getId(), CNBBlocks.POTTED_YELLOW_WATERLILY);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> CNBClient::init);
        event.enqueueWork(() -> {
            ItemProperties.register(CNBItems.CACTEM_SPEAR.get(), new ResourceLocation("throwing"), (item, resourceLocation, entity, itemPropertyFunction) -> entity != null && entity.isUsingItem() && entity.getUseItem() == item ? 1.0F : 0.0F);
        });
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        CinderSwordCapability.register(event);
    }
}
