package com.cgessinger.creaturesandbeasts.client.render;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.model.LittleGrebeModel;
import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LittleGrebeRender extends MobRenderer<LittleGrebeEntity, LittleGrebeModel<LittleGrebeEntity>>
{
	protected static final ResourceLocation TEXTURE = new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/little_grebe.png");

	public LittleGrebeRender (EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn, new LittleGrebeModel<LittleGrebeEntity>(), 0.5F);
	}

	@Override
	public ResourceLocation getEntityTexture (LittleGrebeEntity entity)
	{
		return TEXTURE;
	}
}
