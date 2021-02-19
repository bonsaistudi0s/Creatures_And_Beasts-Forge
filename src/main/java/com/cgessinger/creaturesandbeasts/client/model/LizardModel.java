package com.cgessinger.creaturesandbeasts.client.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class LizardModel extends AnimatedGeoModel<LizardEntity>
{
	public LizardModel ()
	{
	}

	@Override
	public ResourceLocation getModelLocation (LizardEntity object)
	{
		return new ResourceLocation(CreaturesAndBeasts.MOD_ID, "geo/lizard.geo.json");
	}

	@Override
	public ResourceLocation getTextureLocation (LizardEntity object)
	{
		return object.getLizardType().getTextureLocation(object.isSad());
	}

	@Override
	public ResourceLocation getAnimationFileLocation (LizardEntity object)
	{
		return new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/lizard.json");
	}

	@Override
	public void setLivingAnimations (LizardEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate)
	{
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		IBone head = this.getAnimationProcessor().getBone("bone8");

		if(entity.isSad())
		{
			head.setRotationX(0.2182F);
			return;
		}

		EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
		head.setRotationX(extraData.headPitch * ((float)Math.PI / 180F));
		head.setRotationY(extraData.netHeadYaw * ((float)Math.PI / 180F));
	}
}