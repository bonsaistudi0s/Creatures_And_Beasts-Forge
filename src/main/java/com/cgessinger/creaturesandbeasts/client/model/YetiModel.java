package com.cgessinger.creaturesandbeasts.client.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.entites.YetiEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class YetiModel<T extends YetiEntity> extends AnimatedGeoModel<T>
{
	@Override
	public ResourceLocation getModelLocation (T t)
	{
		if(t.isBaby())
		{
			return new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/yeti/baby_yeti.geo.json");
		}
		return new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/yeti/yeti.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation (T t)
	{
		if(t.isBaby())
		{
			return new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/yeti/baby_yeti.png");
		}
		return new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/yeti/yeti.png");
	}

	@Override
	public ResourceLocation getAnimationFileLocation (T t)
	{
		return new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/yeti.json");
	}
}
