package com.cgessinger.creaturesandbeasts;

import com.cgessinger.creaturesandbeasts.common.entites.CyndershellEntity;
import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeChickEntity;
import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeEntity;
import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.init.ModItems;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
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

		ModSoundEventTypes.SOUND_EVENTS.register(eventBus);
		ModEntityTypes.ENTITY_TYPES.register(eventBus);
		ModItems.ITEMS.register(eventBus);
		MinecraftForge.EVENT_BUS.register(this);
	}

	private void setup (final FMLCommonSetupEvent event)
	{
		event.enqueueWork(() -> {
			GlobalEntityTypeAttributes.put(ModEntityTypes.LITTLE_GREBE.get(), LittleGrebeEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.LITTLE_GREBE_CHICK.get(), LittleGrebeChickEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.CYNDERSHELL.get(), CyndershellEntity.setCustomAttributes().create());
			GlobalEntityTypeAttributes.put(ModEntityTypes.LIZARD.get(), LizardEntity.setCustomAttributes().create());

			//EntitySpawnPlacementRegistry.register(ModEntityTypes.CYNDERSHELL.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CyndershellEntity::canAnimalSpawn);
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
