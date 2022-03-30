package com.cgessinger.creaturesandbeasts.common.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.common.entites.*;
import com.cgessinger.creaturesandbeasts.common.entites.projectiles.LizardEggEntity;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes
{
	public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, CreaturesAndBeasts.MOD_ID);

	// Entity Types

	/* CREATURES */
	public static final RegistryObject<EntityType<GrebeEntity>> LITTLE_GREBE = ENTITY_TYPES.register("little_grebe",
			() -> EntityType.Builder.of(GrebeEntity::new, MobCategory.CREATURE)
					.sized(0.5f, 0.6f)
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "little_grebe").toString()));

	public static final RegistryObject<EntityType<LizardEntity>> LIZARD = ENTITY_TYPES.register("lizard",
			() -> EntityType.Builder.of(LizardEntity::new, MobCategory.CREATURE)
					.sized(0.52f, 0.3f)
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "lizard").toString()));

	public static final RegistryObject<EntityType<CindershellEntity>> CINDERSHELL = ENTITY_TYPES.register("cindershell",
			() -> EntityType.Builder.of(CindershellEntity::new, MobCategory.MONSTER)
					.sized(1.25f, 1.45f)
					.fireImmune()
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "cindershell").toString()));

	public static final RegistryObject<EntityType<LilytadEntity>> LILYTAD = ENTITY_TYPES.register("lilytad",
			() -> EntityType.Builder.of(LilytadEntity::new, MobCategory.CREATURE)
					.sized(0.7f, 1.02f)
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "lilytad").toString()));

	public static final RegistryObject<EntityType<FriendlySporelingEntity>> FRIENDLY_SPORELING = ENTITY_TYPES.register("friendly_sporeling",
			() -> EntityType.Builder.of(FriendlySporelingEntity::new, MobCategory.CREATURE)
					.sized(0.6f, 0.85f)
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "friendly_sporeling").toString()));

	/* MONSTERS */
	public static final RegistryObject<EntityType<HostileSporelingEntity>> HOSTILE_SPORELING = ENTITY_TYPES.register("hostile_sporeling",
			() -> EntityType.Builder.of(HostileSporelingEntity::new, MobCategory.MONSTER)
					.sized(0.6f, 0.85f)
					.fireImmune()
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "hostile_sporeling").toString()));

	public static final RegistryObject<EntityType<NeutralSporelingEntity>> NEUTRAL_SPORELING = ENTITY_TYPES.register("neutral_sporeling",
			() -> EntityType.Builder.of(NeutralSporelingEntity::new, MobCategory.MONSTER)
					.sized(0.6f, 0.85f)
					.fireImmune()
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "neutral_sporeling").toString()));

	public static final RegistryObject<EntityType<YetiEntity>> YETI = ENTITY_TYPES.register("yeti",
			() -> EntityType.Builder.of(YetiEntity::new, MobCategory.MONSTER)
					.sized(1.55f, 2.05f)
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "yeti").toString()));

    /* PROJECTILES */
    public static final RegistryObject<EntityType<LizardEggEntity>> LIZARD_EGG = ENTITY_TYPES.register("lizard_egg",
			() -> EntityType.Builder.<LizardEggEntity>of(LizardEggEntity::new, MobCategory.MISC)
					.sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10)
					.build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "lizard_egg").toString()));
}
