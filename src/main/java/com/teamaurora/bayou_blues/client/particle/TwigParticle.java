package com.teamaurora.bayou_blues.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

/**
 * 引火物烧尽时飞散的「碎枝」粒子。移植自 Better Badlands 的
 * {@code com.teamaurora.better_badlands.client.particle.TwigParticle}。
 *
 * <p><b>移植注记</b>（1.16.5 → 1.20.1）：
 * <ul>
 *   <li>{@code SpriteTexturedParticle} 在 1.20.1 更名为 {@link TextureSheetParticle}
 *       （它才是持有 sprite 的实现类；{@code SingleQuadParticle} 是它的抽象父类）。</li>
 *   <li>{@code IParticleFactory} → {@link ParticleProvider}；{@code IAnimatedSprite} → {@link SpriteSet}。</li>
 *   <li>{@code pickSprite} 在 1.20.1 是 {@code TextureSheetParticle} 的实例方法，
 *       因此必须继承 {@code TextureSheetParticle} 才能调用。</li>
 * </ul>
 */
public class TwigParticle extends TextureSheetParticle {

    protected TwigParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        this.xd *= 0.8F;
        this.yd *= 0.8F;
        this.zd *= 0.8F;
        this.yd = this.random.nextFloat() * 0.15F + 0.05F;
        this.quadSize *= this.random.nextFloat() * 2.0F + 0.2F;
        this.lifetime = (int) (16.0 / (Math.random() * 0.8 + 0.2));
        this.pickSprite(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float partialTick) {
        int i = super.getLightColor(partialTick);
        int k = i >> 16 & 0xFF;
        return 0xF0 | k << 16;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.quadSize;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float f = (float) this.age / (float) this.lifetime;
        if (0.75 < (double) f && this.random.nextInt(15) == 0) {
            this.level.addParticle(ParticleTypes.SMOKE, this.x, this.y, this.z, this.xd, this.yd, this.zd);
        }
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd -= 0.03;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.999F;
            this.yd *= 0.999F;
            this.zd *= 0.999F;
            if (this.onGround) {
                this.xd *= 0.7F;
                this.zd *= 0.7F;
            }
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Factory(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double xd, double yd, double zd) {
            return new TwigParticle(level, x, y, z, sprites);
        }
    }
}
