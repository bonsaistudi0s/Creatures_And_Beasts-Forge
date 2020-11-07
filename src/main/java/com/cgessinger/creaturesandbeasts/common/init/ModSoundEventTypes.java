package com.cgessinger.creaturesandbeasts.common.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSoundEventTypes
{
	public static DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CreaturesAndBeasts.MOD_ID);

	public static final RegistryObject<SoundEvent> LITTLE_GREBE_AMBIENT = SOUND_EVENTS.register("entity.little_grebe.ambient", () ->
			new SoundEvent(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "entity.little_grebe.ambient"))
	);

	public static final RegistryObject<SoundEvent> LITTLE_GREBE_CHICK_AMBIENT = SOUND_EVENTS.register("entity.little_grebe_chick.ambient", () ->
		new SoundEvent(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "entity.little_grebe_chick.ambient"))
	);
}
