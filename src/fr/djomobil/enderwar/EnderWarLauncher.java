package fr.djomobil.enderwar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;


public class EnderWarLauncher extends BukkitRunnable{

	private EnderWar main;
	private int timer = 5;
	
	public EnderWarLauncher(EnderWar main) {
		this.main = main;
	}

	@Override
	public void run() {

		timer --;
		
		Bukkit.broadcastMessage(main.prefix + ChatColor.AQUA + "Start in : " + ChatColor.RED + timer + ChatColor.AQUA + " s");
		
		if(main.getPlayers().size() < main.getEnderWarConfig().getMinPlayers()) {
			cancel();
			Bukkit.broadcastMessage(main.prefix + ChatColor.DARK_RED + "Cancel ... Not enought player for start");
			main.setState(EnderWarState.LOBBY);
		}
		
		if(timer == 0) {
			cancel();
			main.setState(EnderWarState.GAME);
			Bukkit.broadcastMessage(main.prefix + ChatColor.RED + "Go ! Good Luck.");
			EnderWarGame game = new EnderWarGame(main);
			game.launchGame();
		}
		
	}

}
