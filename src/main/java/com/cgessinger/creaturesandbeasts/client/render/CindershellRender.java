package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.layer.CindershellGlowLayer;
import com.cgessinger.creaturesandbeasts.client.model.CindershellModel;
import com.cgessinger.creaturesandbeasts.common.entites.CindershellEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class CindershellRender extends MobRenderer<CindershellEntity, CindershellModel<CindershellEntity>>
{
	protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/cindershell.png");

	public CindershellRender (EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn, new CindershellModel<>(), 0.5F);
		this.addLayer(new CindershellGlowLayer(this));
	}

	@Override
	public ResourceLocation getEntityTexture (CindershellEntity entity)
	{
		return TEXTURE;
	}
}
