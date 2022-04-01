package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.entities.CindershellEntity;
import com.cgessinger.creaturesandbeasts.entities.LittleGrebeEntity;
import com.cgessinger.creaturesandbeasts.entities.LilytadEntity;
import com.cgessinger.creaturesandbeasts.entities.LizardEggEntity;
import com.cgessinger.creaturesandbeasts.entities.LizardEntity;
import com.cgessinger.creaturesandbeasts.entities.SporelingEntity;
import com.cgessinger.creaturesandbeasts.entities.YetiEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CNBEntityTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, CreaturesAndBeasts.MOD_ID);

    /* CREATURES */
    public static final RegistryObject<EntityType<LittleGrebeEntity>> LITTLE_GREBE = ENTITY_TYPES.register("little_grebe", () -> EntityType.Builder.of(LittleGrebeEntity::new, MobCategory.CREATURE).sized(0.5f, 0.6f).build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "little_grebe").toString()));
    public static final RegistryObject<EntityType<LizardEntity>> LIZARD = ENTITY_TYPES.register("lizard", () -> EntityType.Builder.of(LizardEntity::new, MobCategory.CREATURE).sized(0.52f, 0.3f).build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "lizard").toString()));
    public static final RegistryObject<EntityType<CindershellEntity>> CINDERSHELL = ENTITY_TYPES.register("cindershell", () -> EntityType.Builder.of(CindershellEntity::new, MobCategory.MONSTER).sized(1.25f, 1.45f).fireImmune().build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "cindershell").toString()));
    public static final RegistryObject<EntityType<LilytadEntity>> LILYTAD = ENTITY_TYPES.register("lilytad", () -> EntityType.Builder.of(LilytadEntity::new, MobCategory.CREATURE).sized(0.7f, 1.02f).build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "lilytad").toString()));
    public static final RegistryObject<EntityType<SporelingEntity>> SPORELING = ENTITY_TYPES.register("sporeling", () -> EntityType.Builder.of(SporelingEntity::new, MobCategory.CREATURE).sized(0.6f, 0.85f).build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "sporeling").toString()));

    /* MONSTERS */
    public static final RegistryObject<EntityType<YetiEntity>> YETI = ENTITY_TYPES.register("yeti", () -> EntityType.Builder.of(YetiEntity::new, MobCategory.MONSTER).sized(1.55f, 2.05f).build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "yeti").toString()));

    /* PROJECTILES */
    public static final RegistryObject<EntityType<LizardEggEntity>> LIZARD_EGG = ENTITY_TYPES.register("lizard_egg", () -> EntityType.Builder.<LizardEggEntity>of(LizardEggEntity::new, MobCategory.MISC).sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).build(new ResourceLocation(CreaturesAndBeasts.MOD_ID, "lizard_egg").toString()));
}
