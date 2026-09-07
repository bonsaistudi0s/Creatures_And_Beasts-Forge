package com.cgessinger.creaturesandbeasts;

import com.cgessinger.creaturesandbeasts.modules.CNBCreativeTabModule;
import net.fabricmc.api.ModInitializer;

public class CreaturesAndBeasts implements ModInitializer {

    @Override
    public void onInitialize() {
        CreaturesAndBeastsCommon.init();

        CNBCreativeTabModule.load();
    }
}
