package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.entity.model.AgeableModelProvider;
import com.cgessinger.creaturesandbeasts.client.entity.model.LittleGrebeChickModel;
import com.cgessinger.creaturesandbeasts.client.entity.model.LittleGrebeModel;
import com.cgessinger.creaturesandbeasts.entities.GrebeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GrebeRender extends MobRenderer<GrebeEntity, AgeableModelProvider<GrebeEntity>> {
    protected static final ResourceLocation CHILD_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/little_grebe_chick.png");
    protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/little_grebe.png");

    public GrebeRender(EntityRendererProvider.Context context) {
        super(context, new AgeableModelProvider<>(new LittleGrebeChickModel<>(), new LittleGrebeModel<>()), 0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(GrebeEntity entity) {
        return entity.isBaby() ? CHILD_TEXTURE : TEXTURE;
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    @Override
    protected float getBob(GrebeEntity livingBase, float partialTicks) {
        float f = Mth.lerp(partialTicks, livingBase.oFlap, livingBase.wingRotation);
        float f1 = Mth.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.destPos);
        return (Mth.sin(f) + 1.0F) * f1;
    }

    @Override
    protected void scale(GrebeEntity entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        if (entitylivingbaseIn.isPassenger() && entitylivingbaseIn.isBaby()) {
            matrixStackIn.scale(0.75F, 0.75F, 0.75F);
        }
        super.scale(entitylivingbaseIn, matrixStackIn, partialTickTime);
    }
}
