package fr.djomobil.utils;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import fr.djomobil.enderwar.EnderWar;

public class ConfigUtils {
	
	private EnderWar main;

	public ConfigUtils(EnderWar enderWar) {
		this.main = enderWar;
	}

	
	// Location parser
	private Location getParseLoc(String spawn) {
		spawn.replace(" ", "");
		String[] str = spawn.split(",");
		double x = Double.valueOf(str[0]);
		double y = Double.valueOf(str[1]);
		double z = Double.valueOf(str[2]);
		return new Location(Bukkit.getWorld(main.getConfig().getString("Locations.worldname")),x,y,z);
	}
	
	
	/* Config getters */
	
	// Get the lobby spawn
	public Location getLobbySpawn() {	
		return getParseLoc(main.getConfig().getString("Locations.Spawns.lobby"));
	}
	
	
	// Get the spectators spawn
	public Location getSpectatorSpawn() {
		return getParseLoc(main.getConfig().getString("Locations.Spawns.spectator"));
	}
	
	/* Island spawns */
	
	
	// Get all player's spawns for the game
	public HashMap<Integer, Location> getGameSpawns(){
	
		HashMap<Integer, Location> locations = new HashMap<>();	
		
		
		ConfigurationSection section = main.getConfig().getConfigurationSection("Locations.Spawns.Islands");
		
		//for evry spawn...
		for(String i : section.getKeys(false)) {
			String loc = section.getString(i);
			locations.put(Integer.valueOf(i), getParseLoc(loc)); //Add a new location
		}
		
		return locations;
	}
	
	
	// Get minimum players to start
	public int getMinPlayers() {
		return main.getConfig().getInt("MinPlayers");
	}
	
	public int getMaxPlayers() {
		return main.getConfig().getInt("MaxPlayers");
	}
	
	public int getDieLayer() {
		return main.getConfig().getInt("DieLayer");
	}



}
