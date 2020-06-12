package fr.djomobil.utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnderWarPearl extends ItemStack{

	
	private final String NAME;
	private final ArrayList<String> LORE;
	private final Enchantment ENCHANT;
	private final int ENCHANT_LVL;
	private final boolean IGNORE_LVL_RESTRICTION;
	
	public EnderWarPearl() {
		
		super(Material.ENDER_PEARL);
		
		NAME = ChatColor.DARK_GREEN + "Magic Pearl";
		LORE = new ArrayList<>();
		LORE.add("This pearl got 2 options :");
		LORE.add("  - Teleport to an other island.");
		LORE.add("  - Kick other players into the void.");
		
		
		ENCHANT = Enchantment.KNOCKBACK;
		ENCHANT_LVL = 10;
		IGNORE_LVL_RESTRICTION = true;
		
		
		ItemMeta it = this.getItemMeta();
		
		it.setDisplayName(NAME);
		it.addEnchant(ENCHANT, ENCHANT_LVL, IGNORE_LVL_RESTRICTION);
		it.setLore(LORE);
		
		this.setItemMeta(it);
		
	}
	
}
