package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.layer.CyndershellGlowLayer;
import com.cgessinger.creaturesandbeasts.client.model.CyndershellModel;
import com.cgessinger.creaturesandbeasts.common.entites.CyndershellEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class CyndershellRender extends MobRenderer<CyndershellEntity, CyndershellModel<CyndershellEntity>>
{
	protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/cyndershell.png");

	public CyndershellRender (EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn, new CyndershellModel<>(), 0.5F);
		this.addLayer(new CyndershellGlowLayer(this));
	}

	@Override
	public ResourceLocation getEntityTexture (CyndershellEntity entity)
	{
		return TEXTURE;
	}
}
