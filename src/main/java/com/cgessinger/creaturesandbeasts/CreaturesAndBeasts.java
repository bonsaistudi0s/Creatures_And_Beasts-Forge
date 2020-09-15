package com.cgessinger.creaturesandbeasts;

import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeChickEntity;
import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.init.ModItems;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CreaturesAndBeasts.MOD_ID)
public class CreaturesAndBeasts
{
	public static final String MOD_ID = "cnb";

	public CreaturesAndBeasts ()
	{
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		// Register the setup method for modloading
		eventBus.addListener(this::setup);
		// Register the doClientStuff method for modloading
		eventBus.addListener(this::doClientStuff);

		ModEntityTypes.ENTITY_TYPES.register(eventBus);
		ModItems.ITEMS.register(eventBus);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup (final FMLCommonSetupEvent event)
	{
		DeferredWorkQueue.runLater(() -> {
			GlobalEntityTypeAttributes.put(ModEntityTypes.LITTLE_GREBE.get(), LittleGrebeEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.LITTLE_GREBE_CHICK.get(), LittleGrebeChickEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.LIZARD.get(), LittleGrebeChickEntity.setCustomAttributes().create());
		});
	}

	private void doClientStuff (final FMLClientSetupEvent event)
	{
	}

	public static final ItemGroup TAB = new ItemGroup("cnb_tab") {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.GREBE_SPAWN_EGG.get());
		}
	};
}
