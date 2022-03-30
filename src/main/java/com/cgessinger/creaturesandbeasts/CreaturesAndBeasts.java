package com.cgessinger.creaturesandbeasts;

import com.cgessinger.creaturesandbeasts.client.entity.CNBClient;
import com.cgessinger.creaturesandbeasts.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import com.cgessinger.creaturesandbeasts.entities.FriendlySporelingEntity;
import com.cgessinger.creaturesandbeasts.entities.GrebeEntity;
import com.cgessinger.creaturesandbeasts.entities.HostileSporelingEntity;
import com.cgessinger.creaturesandbeasts.entities.LilytadEntity;
import com.cgessinger.creaturesandbeasts.entities.LizardEntity;
import com.cgessinger.creaturesandbeasts.entities.NeutralSporelingEntity;
import com.cgessinger.creaturesandbeasts.entities.YetiEntity;
import com.cgessinger.creaturesandbeasts.init.CNBBlocks;
import com.cgessinger.creaturesandbeasts.init.CNBEntityTypes;
import com.cgessinger.creaturesandbeasts.init.CNBItems;
import com.cgessinger.creaturesandbeasts.init.CNBSoundEvents;
import com.cgessinger.creaturesandbeasts.world.gen.ModEntitySpawns;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(CreaturesAndBeasts.MOD_ID)
public class CreaturesAndBeasts {
    public static final String MOD_ID = "cnb";
    public static final CreativeModeTab TAB = new CreativeModeTab("cnb_tab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(CNBItems.GREBE_SPAWN_EGG.get());
        }
    };

    public CreaturesAndBeasts() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::commonSetup);
        eventBus.addListener(this::clientSetup);

        ModLoadingContext.get().registerConfig(Type.COMMON, CNBConfig.COMMON_SPEC);

        CNBSoundEvents.SOUND_EVENTS.register(eventBus);
        CNBEntityTypes.ENTITY_TYPES.register(eventBus);
        CNBBlocks.BLOCKS.register(eventBus);
        CNBItems.ITEMS.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.addListener(CindershellEntity::onEntityAttributeModification);
        MinecraftForge.EVENT_BUS.addListener(FriendlySporelingEntity::onEntityAttributeModification);
        MinecraftForge.EVENT_BUS.addListener(GrebeEntity::onEntityAttributeModification);
        MinecraftForge.EVENT_BUS.addListener(HostileSporelingEntity::onEntityAttributeModification);
        MinecraftForge.EVENT_BUS.addListener(LilytadEntity::onEntityAttributeModification);
        MinecraftForge.EVENT_BUS.addListener(LizardEntity::onEntityAttributeModification);
        MinecraftForge.EVENT_BUS.addListener(NeutralSporelingEntity::onEntityAttributeModification);
        MinecraftForge.EVENT_BUS.addListener(YetiEntity::onEntityAttributeModification);

        GeckoLib.initialize();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModEntitySpawns.entitySpawnPlacementRegistry();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> CNBClient::init);
    }
}
