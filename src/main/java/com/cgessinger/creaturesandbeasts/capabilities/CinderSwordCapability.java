package com.cgessinger.creaturesandbeasts.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class CinderSwordCapability {
    public static final Capability<ICinderSwordUpdate> CINDER_SWORD_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});;

    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(ICinderSwordUpdate.class);
    }
}
