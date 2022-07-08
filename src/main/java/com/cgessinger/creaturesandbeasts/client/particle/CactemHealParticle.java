package com.cgessinger.creaturesandbeasts.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CactemHealParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    public CactemHealParticle(ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprites) {
        super(level, x, y, z);
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;
        this.quadSize *= level.getRandom().nextDouble() * 0.6D + 0.4D;
        this.lifetime = level.getRandom().nextInt(15) + 20;
        this.sprites = sprites;
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.yd += level.getRandom().nextDouble() * 0.01D;
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel levelIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new CactemHealParticle(levelIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}
