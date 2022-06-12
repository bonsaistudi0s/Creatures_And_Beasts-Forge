package com.cgessinger.creaturesandbeasts.init;

import com.cgessinger.creaturesandbeasts.CreaturesAndBeasts;
import com.cgessinger.creaturesandbeasts.client.particle.CactemHealParticle;
import com.cgessinger.creaturesandbeasts.client.particle.MinipadFlowerParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = CreaturesAndBeasts.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CNBParticleTypes {

    public static DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CreaturesAndBeasts.MOD_ID);

    public static final RegistryObject<SimpleParticleType> PINK_MINIPAD_FLOWER = PARTICLE_TYPES.register("pink_minipad_flower", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> LIGHT_PINK_MINIPAD_FLOWER = PARTICLE_TYPES.register("light_pink_minipad_flower", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> YELLOW_MINIPAD_FLOWER = PARTICLE_TYPES.register("yellow_minipad_flower", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> CACTEM_HEAL_PARTICLE = PARTICLE_TYPES.register("heal", () -> new SimpleParticleType(false));

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event) {
        ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;

        particleEngine.register(PINK_MINIPAD_FLOWER.get(), MinipadFlowerParticle.Factory::new);
        particleEngine.register(LIGHT_PINK_MINIPAD_FLOWER.get(), MinipadFlowerParticle.Factory::new);
        particleEngine.register(YELLOW_MINIPAD_FLOWER.get(), MinipadFlowerParticle.Factory::new);
        particleEngine.register(CACTEM_HEAL_PARTICLE.get(), CactemHealParticle.Factory::new);
    }
}
