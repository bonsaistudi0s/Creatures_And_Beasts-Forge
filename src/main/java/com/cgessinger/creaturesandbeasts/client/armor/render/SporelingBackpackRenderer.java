package com.cgessinger.creaturesandbeasts.client.armor.render;

import com.cgessinger.creaturesandbeasts.client.armor.model.SporelingBackpackModel;
import com.cgessinger.creaturesandbeasts.items.SporelingBackpackItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@OnlyIn(Dist.CLIENT)
public class SporelingBackpackRenderer extends GeoArmorRenderer<SporelingBackpackItem> {

    public SporelingBackpackRenderer() {
        super(new SporelingBackpackModel());

        this.headBone = null;
        this.bodyBone = "main";
        this.rightArmBone = null;
        this.leftArmBone = null;
        this.rightLegBone = null;
        this.leftLegBone = null;
        this.rightBootBone = null;
        this.leftBootBone = null;
    }
}
