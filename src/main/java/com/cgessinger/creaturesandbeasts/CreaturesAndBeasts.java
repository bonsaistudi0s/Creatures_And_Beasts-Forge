package com.cgessinger.creaturesandbeasts;

import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CreaturesAndBeasts.MOD_ID)
public class CreaturesAndBeasts
{
	public static final String MOD_ID = "cab1";

	public CreaturesAndBeasts ()
	{
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		// Register the setup method for modloading
		eventBus.addListener(this::setup);
		// Register the doClientStuff method for modloading
		eventBus.addListener(this::doClientStuff);

		ModEntityTypes.ENTITY_TYPES.register(eventBus);
		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup (final FMLCommonSetupEvent event)
	{
		DeferredWorkQueue.runLater(() -> {
			GlobalEntityTypeAttributes.put(ModEntityTypes.LITTLE_GREBE.get(), LittleGrebeEntity.setCustomAttributes().func_233813_a_());
		});
	}

	private void doClientStuff (final FMLClientSetupEvent event)
	{
	}
}
