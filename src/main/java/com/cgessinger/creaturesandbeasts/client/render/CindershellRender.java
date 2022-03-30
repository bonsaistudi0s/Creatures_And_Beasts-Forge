package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.layer.CindershellGlowLayer;
import com.cgessinger.creaturesandbeasts.client.layer.CindershellItemLayer;
import com.cgessinger.creaturesandbeasts.client.model.AgeableModelProvider;
import com.cgessinger.creaturesandbeasts.client.model.BabyCindershellModel;
import com.cgessinger.creaturesandbeasts.client.model.CindershellModel;
import com.cgessinger.creaturesandbeasts.common.entites.CindershellEntity;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class CindershellRender
    extends MobRenderer<CindershellEntity, AgeableModelProvider<CindershellEntity>>
{
    protected static final ResourceLocation ADULT =
        new ResourceLocation( CreaturesAndBeasts.MOD_ID, "textures/model/entity/cindershell/cindershell.png" );

    protected static final ResourceLocation BABY =
        new ResourceLocation( CreaturesAndBeasts.MOD_ID, "textures/model/entity/cindershell/baby_cindershell.png" );

    public CindershellRender(EntityRendererProvider.Context context)
    {
        super(context, new AgeableModelProvider<>(new BabyCindershellModel(), new CindershellModel()), 0.6F);
        this.addLayer(new CindershellGlowLayer(this));
        this.addLayer(new CindershellItemLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation( CindershellEntity entity )
    {
        if ( entity.isBaby() )
            return BABY;

        return ADULT;
    }
}
