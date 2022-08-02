package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.containers.CinderFurnaceContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CNBContainerTypes {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, CreaturesAndBeasts.MOD_ID);

    public static final RegistryObject<MenuType<CinderFurnaceContainer>> CINDER_FURNACE_CONTAINER = CONTAINER_TYPES.register("cinder_furnace_container", () -> IForgeMenuType.create(((windowId, inv, data) -> new CinderFurnaceContainer(windowId, inv))));
}
