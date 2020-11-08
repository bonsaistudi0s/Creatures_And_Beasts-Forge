package com.cgessinger.creaturesandbeasts.common.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.entites.CyndershellEntity;
import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeChickEntity;
import com.cgessinger.creaturesandbeasts.common.entites.LittleGrebeEntity;
import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes
{
	public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, CreaturesAndBeasts.MOD_ID);

	// Entity Types
	public static final RegistryObject<EntityType<LittleGrebeEntity>> LITTLE_GREBE = ENTITY_TYPES.register("little_grebe",
			() -> EntityType.Builder.create(LittleGrebeEntity::new, EntityClassification.CREATURE)
					.size(0.5f, 0.6f)
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "little_grebe").toString()));

	public static final RegistryObject<EntityType<LittleGrebeChickEntity>> LITTLE_GREBE_CHICK = ENTITY_TYPES.register("little_grebe_chick",
			() -> EntityType.Builder.create(LittleGrebeChickEntity::new, EntityClassification.CREATURE)
					.size(0.5f, 0.6f)
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "little_grebe_chick").toString()));

	public static final RegistryObject<EntityType<LizardEntity>> LIZARD = ENTITY_TYPES.register("lizard",
			() -> EntityType.Builder.create(LizardEntity::new, EntityClassification.CREATURE)
					.size(0.52f, 0.3f)
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "lizard").toString()));

	public static final RegistryObject<EntityType<CyndershellEntity>> CYNDERSHELL = ENTITY_TYPES.register("cyndershell",
			() -> EntityType.Builder.create(CyndershellEntity::new, EntityClassification.CREATURE)
					.size(1.7f, 1.7f)
					.immuneToFire()
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "cyndershell").toString()));
}
