package fr.djomobil.enderwar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;


public class EnderWarFinish extends BukkitRunnable {

	private EnderWar main;
	private int timer = 10;

	public EnderWarFinish(EnderWar main) {
		this.main = main;
	}

	@Override
	public void run() {
		
		Bukkit.broadcastMessage(main.prefix + ChatColor.RED + "Server reload in : " + ChatColor.DARK_RED + timer + ChatColor.RED + " s");
		
		// Reaload the server for being ready for the next game
		if(timer == 0) {
			Bukkit.reload();
			cancel();
		}
		
		timer --;
	}

}
