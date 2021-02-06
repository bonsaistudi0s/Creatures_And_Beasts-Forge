package com.cgessinger.creaturesandbeasts.common.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.entites.*;
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

	/* CREATURES */
	public static final RegistryObject<EntityType<GrebeEntity>> LITTLE_GREBE = ENTITY_TYPES.register("little_grebe",
			() -> EntityType.Builder.create(GrebeEntity::new, EntityClassification.CREATURE)
					.size(0.5f, 0.6f)
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "little_grebe").toString()));

	public static final RegistryObject<EntityType<LizardEntity>> LIZARD = ENTITY_TYPES.register("lizard",
			() -> EntityType.Builder.create(LizardEntity::new, EntityClassification.CREATURE)
					.size(0.52f, 0.3f)
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "lizard").toString()));

	public static final RegistryObject<EntityType<CindershellEntity>> CINDERSHELL = ENTITY_TYPES.register("cindershell",
			() -> EntityType.Builder.create(CindershellEntity::new, EntityClassification.MONSTER)
					.size(1.25f, 1.45f)
					.immuneToFire()
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "cindershell").toString()));

	public static final RegistryObject<EntityType<LilytadEntity>> LILYTAD = ENTITY_TYPES.register("lilytad",
			() -> EntityType.Builder.create(LilytadEntity::new, EntityClassification.CREATURE)
					.size(0.7f, 1.0f)
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "lilytad").toString()));

	public static final RegistryObject<EntityType<FriendlySporelingEntity>> FRIENDLY_SPORELING = ENTITY_TYPES.register("friendly_sporeling",
			() -> EntityType.Builder.create(FriendlySporelingEntity::new, EntityClassification.CREATURE)
					.size(0.6f, 0.85f)
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "friendly_sporeling").toString()));

	/* MONSTERS */
	public static final RegistryObject<EntityType<HostileSporelingEntity>> HOSTILE_SPORELING = ENTITY_TYPES.register("hostile_sporeling",
			() -> EntityType.Builder.create(HostileSporelingEntity::new, EntityClassification.MONSTER)
					.size(0.6f, 0.85f)
					.immuneToFire()
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "hostile_sporeling").toString()));

	public static final RegistryObject<EntityType<NeutralSporelingEntity>> NEUTRAL_SPORELING = ENTITY_TYPES.register("neutral_sporeling",
			() -> EntityType.Builder.create(NeutralSporelingEntity::new, EntityClassification.MONSTER)
					.size(0.6f, 0.85f)
					.immuneToFire()
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "neutral_sporeling").toString()));

	public static final RegistryObject<EntityType<YetiEntity>> YETI = ENTITY_TYPES.register("yeti",
			() -> EntityType.Builder.create(YetiEntity::new, EntityClassification.MONSTER)
					.size(1.4f, 1.9f)
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "yeti").toString()));
}
