package com.cgessinger.creaturesandbeasts.common.entites.projectiles;

import com.cgessinger.creaturesandbeasts.common.entites.LizardEntity;
import com.cgessinger.creaturesandbeasts.common.init.ModEntityTypes;
import com.cgessinger.creaturesandbeasts.common.init.ModItems;
import com.cgessinger.creaturesandbeasts.common.init.ModSoundEventTypes;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class LizardEggEntity
    extends ProjectileItemEntity
{
    public LizardEggEntity( EntityType<? extends LizardEggEntity> p_i50154_1_, World p_i50154_2_ )
    {
        super( p_i50154_1_, p_i50154_2_ );
    }

    public LizardEggEntity( World worldIn, LivingEntity throwerIn )
    {
        super( ModEntityTypes.LIZARD_EGG.get(), throwerIn, worldIn );
    }

    public LizardEggEntity( World worldIn, double x, double y, double z )
    {
        super( ModEntityTypes.LIZARD_EGG.get(), x, y, z, worldIn );
    }

    @OnlyIn( Dist.CLIENT )
    public void handleStatusUpdate( byte id )
    {
        if ( id == 3 )
        {
            for ( int i = 0; i < 8; ++i )
            {
                this.world.addParticle( new ItemParticleData( ParticleTypes.ITEM, this.getItem() ), this.getPosX(),
                                        this.getPosY(), this.getPosZ(),
                                        ( (double) this.rand.nextFloat() - 0.5D ) * 0.08D,
                                        ( (double) this.rand.nextFloat() - 0.5D ) * 0.08D,
                                        ( (double) this.rand.nextFloat() - 0.5D ) * 0.08D );
            }
        }

    }

    protected void onEntityHit( EntityRayTraceResult p_213868_1_ )
    {
        super.onEntityHit( p_213868_1_ );
        p_213868_1_.getEntity().attackEntityFrom( DamageSource.causeThrownDamage( this, this.func_234616_v_() ), 0.0F );
    }

    protected void onImpact( RayTraceResult result )
    {
        super.onImpact( result );
        if ( !this.world.isRemote )
        {
            if ( this.rand.nextFloat() > 0.3F )
            {
                BlockPos pos = this.getPosition();
                this.world.playSound( null, pos, ModSoundEventTypes.LIZARD_EGG_HATCH.get(), SoundCategory.BLOCKS, 1.0F, 0F);
                LizardEntity lizard = ModEntityTypes.LIZARD.get().create( this.world );
                lizard.setGrowingAge( -24000 );
                lizard.setLocationAndAngles( this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F );
                lizard.setVariant(lizard.getLizardTypeFromBiome(this.world.getBiome(pos).getCategory()));
                this.world.addEntity( lizard );
            }

            this.world.setEntityState( this, (byte) 3 );
            this.remove();
        }

    }

    protected Item getDefaultItem()
    {
        return ModItems.LIZARD_EGG.get();
    }

    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
