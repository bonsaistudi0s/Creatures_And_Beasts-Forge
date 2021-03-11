package com.cgessinger.creaturesandbeasts.client.layer;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.model.AgeableModelProvider;
import com.cgessinger.creaturesandbeasts.client.model.BabyCindershellModel;
import com.cgessinger.creaturesandbeasts.client.model.CindershellModel;
import com.cgessinger.creaturesandbeasts.common.entites.CindershellEntity;
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
public class CindershellGlowLayer extends LayerRenderer<CindershellEntity, AgeableModelProvider<CindershellEntity>>
{
	private static final RenderType RENDER_TYPE = RenderType.getEyes(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "textures/model/entity/cindershell/cindershell_glow.png"));
	private final AgeableModelProvider<CindershellEntity> cindershellModel;

	public CindershellGlowLayer (IEntityRenderer<CindershellEntity, AgeableModelProvider<CindershellEntity>> entityRendererIn)
	{
		super(entityRendererIn);
		this.cindershellModel = new AgeableModelProvider<>( new BabyCindershellModel(), new CindershellModel() );
	}

	@Override
	public void render (MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, CindershellEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch)
	{
        if(!entitylivingbaseIn.isChild())
        {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RENDER_TYPE);
            this.getEntityModel().adultModel.render(matrixStackIn, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
	}

	@Override
	public AgeableModelProvider<CindershellEntity> getEntityModel ()
	{
		return this.cindershellModel;
	}
}
