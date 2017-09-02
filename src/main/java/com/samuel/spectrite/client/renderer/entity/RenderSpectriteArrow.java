package com.samuel.spectrite.client.renderer.entity;

import com.google.common.collect.Maps;
import com.samuel.spectrite.Spectrite;
import com.samuel.spectrite.entities.EntitySpectriteArrow;
import com.samuel.spectrite.etc.SpectriteHelper;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderSpectriteArrow<T extends EntitySpectriteArrow> extends RenderArrow<T> {
	
	private static final Map<String, ResourceLocation> ARROW_TEXTURE_RES_MAP = Maps.<String, ResourceLocation>newHashMap();

	public RenderSpectriteArrow(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(T entity) {
		
		int curFrame = SpectriteHelper.getCurrentSpectriteFrame(entity.getEntityWorld());
        String textureLoc = String.format("%s:textures/entities/spectrite_arrow/%d.png", Spectrite.MOD_ID, curFrame);
		ResourceLocation resourceLocation = ARROW_TEXTURE_RES_MAP.get(textureLoc);
		
        if (resourceLocation == null)
        {
            resourceLocation = new ResourceLocation(textureLoc);
            ARROW_TEXTURE_RES_MAP.put(textureLoc, resourceLocation);
        }
		
		return resourceLocation;
	}
}