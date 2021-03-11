package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.layer.CindershellGlowLayer;
import com.cgessinger.creaturesandbeasts.client.model.AgeableModelProvider;
import com.cgessinger.creaturesandbeasts.client.model.BabyCindershellModel;
import com.cgessinger.creaturesandbeasts.client.model.CindershellModel;
import com.cgessinger.creaturesandbeasts.common.entites.CindershellEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class CindershellRender
    extends MobRenderer<CindershellEntity, AgeableModelProvider<CindershellEntity>>
{
    protected static final ResourceLocation ADULT =
        new ResourceLocation( CreaturesAndBeasts.MOD_ID, "textures/model/entity/cindershell/cindershell.png" );

    protected static final ResourceLocation BABY =
        new ResourceLocation( CreaturesAndBeasts.MOD_ID, "textures/model/entity/cindershell/baby_cindershell.png" );

    public CindershellRender( EntityRendererManager renderManagerIn )
    {
        super( renderManagerIn, new AgeableModelProvider<>( new BabyCindershellModel(), new CindershellModel() ),
               0.6F );
        this.addLayer(new CindershellGlowLayer(this));
    }

    @Override
    public ResourceLocation getEntityTexture( CindershellEntity entity )
    {
        if ( entity.isChild() )
            return BABY;

        return ADULT;
    }
}
