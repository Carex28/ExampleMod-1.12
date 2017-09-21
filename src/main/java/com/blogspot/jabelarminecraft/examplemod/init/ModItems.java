/**
    Copyright (C) 2017 by jabelar

    This file is part of jabelar's Minecraft Forge modding examples; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    For a copy of the GNU General Public License see <http://www.gnu.org/licenses/>.
*/
package com.blogspot.jabelarminecraft.examplemod.init;

import java.util.Set;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.items.ItemCowHide;
import com.blogspot.jabelarminecraft.examplemod.items.ItemHorseHide;
import com.blogspot.jabelarminecraft.examplemod.items.ItemPigSkin;
import com.blogspot.jabelarminecraft.examplemod.items.ItemSheepSkin;
import com.blogspot.jabelarminecraft.examplemod.items.ItemSwordExtended;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

// TODO: Auto-generated Javadoc
// @ObjectHolder(MainMod.MODID)
public class ModItems {
//	public static class ArmorMaterials {
//		public static final ItemArmor.ArmorMaterial ARMOUR_MATERIAL_REPLACEMENT = EnumHelper.addArmorMaterial(Constants.RESOURCE_PREFIX + "replacement", Constants.RESOURCE_PREFIX + "replacement", 15, new int[]{1, 4, 5, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, (float) 0);
//	}
//
//	public static class ToolMaterials {
//		public static final Item.ToolMaterial TOOL_MATERIAL_GLOWSTONE = EnumHelper.addToolMaterial("glowstone", 1, 5, 0.5f, 1.0f, 10);
//	}

    // instantiate items
	public final static ItemCowHide COW_HIDE = new ItemCowHide();
	public final static ItemSheepSkin SHEEP_SKIN = new ItemSheepSkin();
	public final static ItemPigSkin PIG_SKIN = new ItemPigSkin();
	public final static ItemHorseHide HORSE_HIDE = new ItemHorseHide();
	public final static ItemSwordExtended SWORD_EXTENDED = new ItemSwordExtended(ToolMaterial.IRON);

	public static final Set<Item> SET_ITEMS = ImmutableSet.of(
			COW_HIDE,
			SHEEP_SKIN,
			PIG_SKIN,
			HORSE_HIDE,
			SWORD_EXTENDED
			);


	/**
	 * Initialize this mod's {@link Item}s with any post-registration data.
	 */
	private static void initialize() 
	{
		// You can put furnace smelting recipes here
	}

	@Mod.EventBusSubscriber(modid = MainMod.MODID)
	public static class RegistrationHandler 
	{
		/**
		 * Register this mod's {@link Item}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Item> event) 
		{
			final IForgeRegistry<Item> registry = event.getRegistry();

	        System.out.println("Registering items");

			for (final Item item : SET_ITEMS) {
				registry.register(item);
				// DEBUG
				System.out.println("Registering item: "+item.getRegistryName());
			}
			
			initialize();
		}
		
		@SubscribeEvent
		public static void onModelEvent(final ModelRegistryEvent event) 
		{
			//DEBUG
			System.out.println("Registering item models");
			
			registerItemModels();
		}
	}
	
    /**
     * Register item models.
     */
    public static void registerItemModels()
    {
		for (final Item item : SET_ITEMS) {
			registerItemModel(item);
			// DEBUG
			System.out.println("Registering item model for: "+item.getRegistryName());
		}
    }
    
    /**
     * Register item model.
     *
     * @param parItem the par item
     */
    public static void registerItemModel(Item parItem)
    {
    	registerItemModel(parItem, 0);
    }
    
    /**
     * Register item model.
     *
     * @param parItem the par item
     * @param parMetaData the par meta data
     */
    public static void registerItemModel(Item parItem, int parMetaData)
    {
        ModelLoader.setCustomModelResourceLocation(parItem, parMetaData, new ModelResourceLocation(MainMod.MODID + ":" + parItem.getUnlocalizedName().substring(5), "inventory"));
    }
}
