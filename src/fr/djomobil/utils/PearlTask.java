package fr.djomobil.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.djomobil.eventsmanager.PluginManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PearlTask extends BukkitRunnable {

	private Player p;
	private int timer = 5;
	private PluginManager pm;
	
	public PearlTask(Player p, PluginManager pluginManager) {
		this.p = p;
		this.pm = pluginManager;
	}

	@Override
	public void run() {
		
		
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "" + timer + " s"));
		
		
		if(timer == 0) {
			pm.setPearlUsable(true);
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "Your " + ChatColor.GREEN + "Magic Pearl " + ChatColor.RED + "is now can be used !"));
			cancel();
		}
		timer --;
	}

}
