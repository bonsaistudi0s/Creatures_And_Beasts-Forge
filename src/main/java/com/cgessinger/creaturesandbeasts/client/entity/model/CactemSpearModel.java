package com.cgessinger.creaturesandbeasts.client.entity.model;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CactemSpearModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "thrown_cactem_spear"), "main");
    private final ModelPart spear;

	public CactemSpearModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.spear = root.getChild("spear");
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("spear", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -1.375F, 1.0F, 1.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 12).addBox(0.0F, -1.5F, -7.375F, 0.0F, 3.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.5F, 0.0F, -7.375F, 3.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(0, 4).addBox(-1.0F, -1.0F, -1.375F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.5F, -4.625F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.spear.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}