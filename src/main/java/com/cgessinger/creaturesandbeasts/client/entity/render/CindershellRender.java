package com.cgessinger.creaturesandbeasts.client.entity.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.entity.model.AgeableModelProvider;
import com.cgessinger.creaturesandbeasts.client.entity.model.BabyCindershellModel;
import com.cgessinger.creaturesandbeasts.client.entity.model.CindershellModel;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CindershellRender extends MobRenderer<CindershellEntity, AgeableModelProvider<CindershellEntity>> {
    protected static final ResourceLocation CHILD_TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/cindershell/baby_cindershell.png");
    protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/cindershell/cindershell.png");

    public CindershellRender(EntityRendererProvider.Context context) {
        super(context, new AgeableModelProvider<>(new BabyCindershellModel(), new CindershellModel()), 0.6F);
        this.addLayer(new CindershellGlowLayer(this));
        this.addLayer(new CindershellItemLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(CindershellEntity entity) {
        return entity.isBaby() ? CHILD_TEXTURE : TEXTURE;
    }
}
