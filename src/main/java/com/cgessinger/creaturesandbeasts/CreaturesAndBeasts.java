package com.cgessinger.creaturesandbeasts;

import com.cgessinger.creaturesandbeasts.common.config.CNBConfig;
import com.cgessinger.creaturesandbeasts.common.entites.CindershellEntity;
import com.cgessinger.creaturesandbeasts.common.entites.FriendlySporelingEntity;
import com.cgessinger.creaturesandbeasts.common.entites.GrebeEntity;
import com.cgessinger.creaturesandbeasts.common.entites.HostileSporelingEntity;
import com.cgessinger.creaturesandbeasts.common.entites.LilytadEntity;
import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import com.cgessinger.creaturesandbeasts.common.entites.NeutralSporelingEntity;
import com.cgessinger.creaturesandbeasts.common.entites.YetiEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModBlockRegistry;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.init.ModItems;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import com.cgessinger.creaturesandbeasts.common.world.gen.ModEntitySpawns;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;

@Mod(CreaturesAndBeasts.MOD_ID)
public class CreaturesAndBeasts
{
	public static final String MOD_ID = "cnb";

	public CreaturesAndBeasts ()
	{
		ModLoadingContext.get().registerConfig(Type.COMMON, CNBConfig.COMMON_SPEC);

		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		// Register the setup method for modloading
		eventBus.addListener(this::setup);
		// Register the doClientStuff method for modloading
		eventBus.addListener(this::doClientStuff);

		ModSoundEventTypes.SOUND_EVENTS.register(eventBus);
		ModEntityTypes.ENTITY_TYPES.register(eventBus);
		ModBlockRegistry.BLOCKS.register(eventBus);
		ModItems.ITEMS.register(eventBus);
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

	private void setup (final FMLCommonSetupEvent event)
	{
		event.enqueueWork(() -> {

		});
		/*
		 * This registers the spawn placement settings we config for any mob that needs
		 * it.
		 */
		ModEntitySpawns.entitySpawnPlacementRegistry();
	}

	private void doClientStuff (final FMLClientSetupEvent event)
	{
		ItemBlockRenderTypes.setRenderLayer(ModBlockRegistry.POTTED_LILYTAD_FLOWER.get(), RenderType.cutout());
	}

	public static final CreativeModeTab TAB = new CreativeModeTab("cnb_tab")
	{
		@Override
		public ItemStack makeIcon ()
		{
			return new ItemStack(ModItems.GREBE_SPAWN_EGG.get());
		}
	};
}
