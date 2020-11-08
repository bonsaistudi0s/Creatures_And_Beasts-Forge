package com.cgessinger.creaturesandbeasts.client.layer;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.model.CyndershellModel;
import com.cgessinger.creaturesandbeasts.common.entites.CyndershellEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CyndershellGlowLayer extends LayerRenderer<CyndershellEntity, CyndershellModel<CyndershellEntity>>
{
	private static final RenderType RENDER_TYPE = RenderType.getEntityTranslucent(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/cyndershell_glow.png"));
	private final CyndershellModel<CyndershellEntity> cyndershellModel;

	public CyndershellGlowLayer (IEntityRenderer<CyndershellEntity, CyndershellModel<CyndershellEntity>> entityRendererIn)
	{
		super(entityRendererIn);
		this.cyndershellModel = new CyndershellModel<>();
	}

	@Override
	public void render (MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, CyndershellEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RENDER_TYPE);
		this.getEntityModel().render(matrixStackIn, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public CyndershellModel<CyndershellEntity> getEntityModel ()
	{
		return this.cyndershellModel;
	}
}
