// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.12.2 or 1.15.2 (same format for both) for entity models animated with GeckoLib
// Paste this class into your mod and follow the documentation for GeckoLib to use animations. You can find the documentation here: https://github.com/bernie-g/geckolib
// Blockbench plugin created by Gecko
package com.cgessinger.creaturesandbeasts.client.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib.animation.model.AnimatedEntityModel;
import software.bernie.geckolib.animation.render.AnimatedModelRenderer;

public class LizardModel<T extends LizardEntity> extends AnimatedEntityModel<LizardEntity> {

	private final AnimatedModelRenderer bone;
	private final AnimatedModelRenderer bone3;
	private final AnimatedModelRenderer bone4;
	private final AnimatedModelRenderer bone9;
	private final AnimatedModelRenderer bone6;
	private final AnimatedModelRenderer bone14;
	private final AnimatedModelRenderer bone8;
	private final AnimatedModelRenderer bone2;
	private final AnimatedModelRenderer bone5;
	private final AnimatedModelRenderer bone7;

	public LizardModel()
	{
		textureWidth = 64;
		textureHeight = 64;
		bone = new AnimatedModelRenderer(this);
		bone.setRotationPoint(0.0F, 24.0F, 5.0F);
		setRotationAngle(bone, -0.1309F, 0.0F, 0.0F);
		bone.setTextureOffset(16, 4).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 3.0F, 6.0F, 0.0F, false);
		bone.setModelRendererName("bone");
		this.registerModelRenderer(bone);

		bone3 = new AnimatedModelRenderer(this);
		bone3.setRotationPoint(0.0F, 0.0F, 0.0049F);
		bone.addChild(bone3);
		setRotationAngle(bone3, 0.1309F, 0.0F, 0.0F);
		bone3.setTextureOffset(0, 19).addBox(-1.0F, -2.0F, -0.0049F, 2.0F, 2.0F, 5.0F, 0.0F, false);
		bone3.setModelRendererName("bone3");
		this.registerModelRenderer(bone3);

		bone4 = new AnimatedModelRenderer(this);
		bone4.setRotationPoint(0.0F, -0.9876F, 5.0F);
		bone3.addChild(bone4);
		bone4.setTextureOffset(0, 10).addBox(-1.0F, -1.0124F, -0.0049F, 2.0F, 2.0F, 7.0F, 0.0F, false);
		bone4.setModelRendererName("bone4");
		this.registerModelRenderer(bone4);

		bone9 = new AnimatedModelRenderer(this);
		bone9.setRotationPoint(1.0F, -1.4251F, -3.4705F);
		bone.addChild(bone9);
		setRotationAngle(bone9, 0.0F, -0.3491F, 0.3054F);
		bone9.setTextureOffset(16, 0).addBox(0.0F, -0.5F, -0.5F, 5.0F, 1.0F, 2.0F, 0.0F, false);
		bone9.setModelRendererName("bone9");
		this.registerModelRenderer(bone9);

		bone6 = new AnimatedModelRenderer(this);
		bone6.setRotationPoint(-1.0F, -1.4251F, -3.4705F);
		bone.addChild(bone6);
		setRotationAngle(bone6, 0.0F, 0.3491F, -0.3054F);
		bone6.setTextureOffset(16, 0).addBox(-5.0F, -0.5F, -0.5F, 5.0F, 1.0F, 2.0F, 0.0F, true);
		bone6.setModelRendererName("bone6");
		this.registerModelRenderer(bone6);

		bone14 = new AnimatedModelRenderer(this);
		bone14.setRotationPoint(0.0F, -1.5F, -5.0F);
		bone.addChild(bone14);
		setRotationAngle(bone14, -0.0873F, 0.0F, 0.0F);
		bone14.setTextureOffset(0, 0).addBox(-2.5F, -2.0641F, -5.981F, 5.0F, 4.0F, 6.0F, 0.0F, false);
		bone14.setModelRendererName("bone14");
		this.registerModelRenderer(bone14);

		bone8 = new AnimatedModelRenderer(this);
		bone8.setRotationPoint(0.0F, -1.0F, -5.8F);
		bone14.addChild(bone8);
		setRotationAngle(bone8, 0.0873F, 0.0F, 0.0F);
		bone8.setTextureOffset(39, 6).addBox(-1.5F, -0.4F, -1.0F, 3.0F, 2.0F, 1.0F, 0.0F, false);
		bone8.setModelRendererName("bone8");
		this.registerModelRenderer(bone8);

		bone2 = new AnimatedModelRenderer(this);
		bone2.setRotationPoint(0.0F, 0.4892F, -0.715F);
		bone8.addChild(bone2);
		setRotationAngle(bone2, 0.1308F, 0.0F, 0.0F);
		bone2.setTextureOffset(14, 15).addBox(-2.0F, -2.0387F, -3.815F, 4.0F, 4.0F, 4.0F, 0.0F, false);
		bone2.setTextureOffset(36, 17).addBox(-1.5F, -0.0387F, -4.815F, 3.0F, 2.0F, 1.0F, 0.0F, false);
		bone2.setModelRendererName("bone2");
		this.registerModelRenderer(bone2);

		bone5 = new AnimatedModelRenderer(this);
		bone5.setRotationPoint(2.0F, 0.1924F, -4.4351F);
		bone14.addChild(bone5);
		setRotationAngle(bone5, 0.1745F, 0.2618F, 0.6545F);
		bone5.setTextureOffset(0, 30).addBox(-0.2F, -0.5F, -0.5F, 5.0F, 1.0F, 2.0F, 0.0F, false);
		bone5.setModelRendererName("bone5");
		this.registerModelRenderer(bone5);

		bone7 = new AnimatedModelRenderer(this);
		bone7.setRotationPoint(-2.0F, 0.1924F, -4.4351F);
		bone14.addChild(bone7);
		setRotationAngle(bone7, 0.1745F, -0.2618F, -0.6545F);
		bone7.setTextureOffset(0, 30).addBox(-4.8F, -0.5F, -0.5F, 5.0F, 1.0F, 2.0F, 0.0F, true);
		bone7.setModelRendererName("bone7");
		this.registerModelRenderer(bone7);

		this.rootBones.add(bone);
	}

    @Override
    public ResourceLocation getAnimationFileLocation()
    {
        return new ResourceLocation(CreaturesAndBeasts.MOD_ID, "animations/lizard.json");
    }
}