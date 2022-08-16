package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import com.cgessinger.creaturesandbeasts.init.CNBSporelingTypes;
import com.cgessinger.creaturesandbeasts.util.SporelingType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class SporelingModel extends AnimatedGeoModel<SporelingEntity> {
    private static final ResourceLocation SPORELING_ANIMATIONS = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/sporeling.json");

    private static final ResourceLocation GOOMY_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/sporeling/sporeling_goomy.geo.json");
    private static final ResourceLocation SHRIMPSNAIL_MODEL = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/entity/sporeling/sporeling_shrimpsnail.geo.json");

    private static final ResourceLocation BIT0_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/sporeling/sporeling_bit0.png");
    private static final ResourceLocation LISTACALISTA_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/sporeling/sporeling_listacalista.png");
    private static final ResourceLocation YUNGWILDER_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/sporeling/sporeling_yungwilder.png");
    private static final ResourceLocation GOOMY_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/sporeling/sporeling_goomy.png");
    private static final ResourceLocation SHRIMPSNAIL_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/entity/sporeling/sporeling_shrimpsnail.png");

    @Override
    public ResourceLocation getModelLocation(SporelingEntity entity) {
        if (entity.hasCustomName() && entity.getSporelingType().getHostility().equals(SporelingType.SporelingHostility.FRIENDLY)) {
            String customName = entity.getCustomName().getString();
            if (customName.equals("Bit0") || customName.equals("ListaCalista") || customName.equals("yungwilder")) {
                return CNBSporelingTypes.RED_OVERWORLD.getModelLocation();
            } else if (customName.equals("Goomy")) {
                return GOOMY_MODEL;
            } else if (customName.equals("ShrimpSnail")) {
                return SHRIMPSNAIL_MODEL;
            }
        }
        return entity.getSporelingType().getModelLocation();
    }

    @Override
    public ResourceLocation getTextureLocation(SporelingEntity entity) {
        if (entity.hasCustomName() && entity.getSporelingType().getHostility().equals(SporelingType.SporelingHostility.FRIENDLY)) {
            String customName = entity.getCustomName().getString();
            switch (customName) {
                case "Bit0":
                    return BIT0_TEXTURE;
                case "ListaCalista":
                    return LISTACALISTA_TEXTURE;
                case "yungwilder" :
                    return YUNGWILDER_TEXTURE;
                case "Goomy":
                    return GOOMY_TEXTURE;
                case "ShrimpSnail":
                    return SHRIMPSNAIL_TEXTURE;
            }
        }

        return entity.getSporelingType().getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(SporelingEntity entity) {
        return SPORELING_ANIMATIONS;
    }
}
