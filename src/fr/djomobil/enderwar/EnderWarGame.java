package fr.djomobil.enderwar;


import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import fr.djomobil.utils.EnderWarPearl;

public class EnderWarGame {

	private EnderWar main;
	
	public EnderWarGame(EnderWar main) {
		this.main = main;
	}

	public void launchGame() {
		
		// Spawn id pointer
		int i = 1;
		
		for(Player p : main.getPlayers()) {
			
			
			p.teleport(main.getGameSpawns().get(i));
			p.setGameMode(GameMode.SURVIVAL);
			p.sendMessage(main.prefix + ChatColor.GREEN + "Let's go, use your " + ChatColor.DARK_GREEN + "ender pearl " + ChatColor.GREEN + "for push other player into the void or for teleport.\n"
									+ " Good Luck.");
			
			p.getInventory().addItem(new EnderWarPearl());
			
			i++;
		}
		
	}

	

}
