package com.samuel.spectrite.etc;

import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class SpectriteExplosion extends Explosion {

    /** whether or not the explosion sets fire to blocks around it */
    private final boolean causesFire;
    /** whether or not this explosion spawns smoke particles */
    private final boolean damagesTerrain;
    private final boolean overrideSound;
    private final Random random;
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    private final float size;

    @SideOnly(Side.CLIENT)
    public SpectriteExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean causesFire, boolean damagesTerrain, boolean overrideSound, List<BlockPos> affectedPositions)
    {
        super(worldIn, entityIn, x, y, z, size, causesFire, damagesTerrain, affectedPositions);

        this.random = new Random();
        this.world = worldIn;
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.causesFire = causesFire;
        this.damagesTerrain = damagesTerrain;
        this.overrideSound = overrideSound;
    }

    public SpectriteExplosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean flaming, boolean damagesTerrain, boolean overrideSound) {
        super(worldIn, entityIn, x, y, z, size, flaming, damagesTerrain);

        this.random = new Random();
        this.world = worldIn;
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.causesFire = flaming;
        this.damagesTerrain = damagesTerrain;
        this.overrideSound = overrideSound;
    }

    @Override
    public void doExplosionB(boolean spawnParticles)
    {
        if (!this.overrideSound) {
            this.world.playSound(null, this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
        }

        Spectrite.Proxy.spawnSpectriteExplosionHugeParticle(this.world, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D, this.size);

        List<BlockPos> affectedBlockPositions = this.getAffectedBlockPositions();

        if (this.damagesTerrain)
        {
            for (BlockPos blockpos : affectedBlockPositions)
            {
                IBlockState iblockstate = this.world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (spawnParticles)
                {
                    double d0 = (double)((float)blockpos.getX() + this.world.rand.nextFloat());
                    double d1 = (double)((float)blockpos.getY() + this.world.rand.nextFloat());
                    double d2 = (double)((float)blockpos.getZ() + this.world.rand.nextFloat());
                    double d3 = d0 - this.x;
                    double d4 = d1 - this.y;
                    double d5 = d2 - this.z;
                    double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 = d3 / d6;
                    d4 = d4 / d6;
                    d5 = d5 / d6;
                    double d7 = 0.5D / (d6 / (double)this.size + 0.1D);
                    d7 = d7 * (double)(this.world.rand.nextFloat() * this.world.rand.nextFloat() + 0.3F);
                    d3 = d3 * d7;
                    d4 = d4 * d7;
                    d5 = d5 * d7;
                    Spectrite.Proxy.spawnSpectriteExplosionParticle(this.world, false,(d0 + this.x) / 2.0D, (d1 + this.y) / 2.0D, (d2 + this.z) / 2.0D, d3, d4, d5);
                    Spectrite.Proxy.spawnSpectriteSmokeNormalParticle(this.world, d0, d1, d2, d3, d4, d5);
                }

                if (iblockstate.getMaterial() != Material.AIR)
                {
                    if (block.canDropFromExplosion(this))
                    {
                        block.dropBlockAsItemWithChance(this.world, blockpos, this.world.getBlockState(blockpos), 1.0F / this.size, 0);
                    }

                    block.onBlockExploded(this.world, blockpos, this);
                }
            }
        }

        if (this.causesFire)
        {
            for (BlockPos blockpos1 : affectedBlockPositions)
            {
                if (this.world.getBlockState(blockpos1).getMaterial() == Material.AIR && this.world.getBlockState(blockpos1.down()).isFullBlock() && this.random.nextInt(3) == 0)
                {
                    this.world.setBlockState(blockpos1, ModBlocks.spectrite_fire.getDefaultState());
                }
            }
        }
    }
}
