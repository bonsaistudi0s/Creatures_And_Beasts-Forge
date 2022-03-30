package com.cgessinger.creaturesandbeasts.client.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.entites.AbstractSporelingEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SporelingModel<T extends AbstractSporelingEntity> extends AnimatedGeoModel<T>
{
	@Override
	public ResourceLocation getModelLocation (T t)
	{
		return t.getSporelingType().getModelLocation();
	}

	@Override
	public ResourceLocation getTextureLocation (T t)
	{
		return t.getSporelingType().getTextureLocation();
	}

	@Override
	public ResourceLocation getAnimationFileLocation (T t)
	{
		return new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/sporeling.json");
	}

    @Override
    public void setLivingAnimations( T entity, Integer uniqueID, AnimationEvent customPredicate )
    {
        super.setLivingAnimations( entity, uniqueID, customPredicate );

        if(!entity.getHolding().isEmpty())
        {
            this.getAnimationProcessor().getBone("Larm").setRotationX(90);
        }
    }

}
