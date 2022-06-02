package com.cgessinger.creaturesandbeasts.client.armor.render;

import com.cgessinger.creaturesandbeasts.client.armor.model.FlowerCrownModel;
import com.cgessinger.creaturesandbeasts.items.FlowerCrownItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@OnlyIn(Dist.CLIENT)
public class FlowerCrownRenderer extends GeoArmorRenderer<FlowerCrownItem> {
    public FlowerCrownRenderer() {
        super(new FlowerCrownModel());

        this.headBone = "group";
        this.bodyBone = null;
        this.rightArmBone = null;
        this.leftArmBone = null;
        this.rightLegBone = null;
        this.leftLegBone = null;
        this.rightBootBone = null;
        this.leftBootBone = null;
    }
}
