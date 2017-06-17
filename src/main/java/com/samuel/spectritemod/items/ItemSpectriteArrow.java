package com.samuel.spectritemod.items;

import java.util.List;

import com.samuel.spectritemod.SpectriteMod;
import com.samuel.spectritemod.entities.EntitySpectriteArrow;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemSpectriteArrow extends ItemArrow {
	
	public ItemSpectriteArrow() {
		super();
		this.addPropertyOverride(new ResourceLocation("time"), SpectriteMod.ItemPropertyGetterSpectrite);
	}
	
	@Override
	public void addInformation(ItemStack stack,
		World worldIn, List<String> list, ITooltipFlag adva) {
		int lineCount = 0;
		boolean isLastLine = false;
		String curLine;
		while (!isLastLine) {
			isLastLine = (curLine = I18n
				.translateToLocal(("iteminfo." + getUnlocalizedName().substring(5) + ".l" +
				++lineCount))).endsWith("@");
			list.add(!isLastLine ? curLine : curLine
				.substring(0, curLine.length() - 1));
		}
	}
	
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
    {
		if (!shooter.getActiveItemStack().isEmpty() && shooter.getActiveItemStack().getItem() instanceof ItemBow) {
			ItemBow bowItem = (ItemBow) shooter.getActiveItemStack().getItem();
			int bowDamage = 2;
			
			if (bowItem instanceof ItemSpectriteBow) {
				if (!(bowItem instanceof ItemSpectriteBowSpecial)) {
					bowDamage = 9;
				}
			} else {
				bowDamage = 99;
			}
			
			if (shooter instanceof EntityPlayer) {
				((EntityPlayer) shooter).getCooldownTracker().setCooldown(Items.BOW, (int) Math.round(SpectriteMod.Config.spectriteToolCooldown * 20));
				((EntityPlayer) shooter).getCooldownTracker().setCooldown(SpectriteMod.ItemSpectriteBow, (int) Math.round(SpectriteMod.Config.spectriteToolCooldown * 20));
				((EntityPlayer) shooter).getCooldownTracker().setCooldown(SpectriteMod.ItemSpectriteBowSpecial, (int) Math.round(SpectriteMod.Config.spectriteToolCooldown * 20));
			}
			shooter.getActiveItemStack().damageItem(bowDamage, shooter);
		}
        return new EntitySpectriteArrow(worldIn, shooter);
    }
	
	public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player)
    {
        int enchant = net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.INFINITY, bow);
        return enchant <= 0 ? false : this.getClass() == ItemSpectriteArrow.class;
    }
}
