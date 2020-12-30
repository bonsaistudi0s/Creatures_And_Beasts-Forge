package com.cgessinger.creaturesandbeasts.client.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.entites.LilytadEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class LilytadModel extends AnimatedGeoModel<LilytadEntity>
{
	public LilytadModel ()
	{

	}

	@Override
	public ResourceLocation getModelLocation (LilytadEntity object)
	{
		return new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/lilytad.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation (LilytadEntity object)
	{
		return new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/lilytad.png");
	}

	@Override
	public ResourceLocation getAnimationFileLocation (LilytadEntity object)
	{
		return new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/lilytad.json");
	}
}
