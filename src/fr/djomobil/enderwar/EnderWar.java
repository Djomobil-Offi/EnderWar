package fr.djomobil.enderwar;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.djomobil.eventsmanager.PluginManager;
import fr.djomobil.utils.ConfigUtils;
import fr.djomobil.eventsmanager.OtherEvents;
public class EnderWar extends JavaPlugin{
	
	public final String prefix = ChatColor.GREEN + "[" + ChatColor.RED +"EnderWar"+ ChatColor.GREEN +"] " + ChatColor.RESET;
	public String motd;
	
	private EnderWarState state;
	
	private ArrayList<Player> players;
	private ArrayList<Player> spectators;
	
	private Location lobbySpawn, spectatorSpawn;
	
	private HashMap<Integer, Location> gameSpawns;
	
	private ConfigUtils configUtils;
	
	
	/* Enabling of the plugin */
	@Override
	public void onEnable() {
		
		// save the default file configuration
		saveDefaultConfig();
		
		//For the config gestion
		configUtils = new ConfigUtils(this);
		
		// Set the list for players and spectators
		this.players = new ArrayList<Player>();
		this.spectators = new ArrayList<Player>();
		
		// Set all spawns
		gameSpawns = getEnderWarConfig().getGameSpawns();
		lobbySpawn = getEnderWarConfig().getLobbySpawn();
		spectatorSpawn = getEnderWarConfig().getSpectatorSpawn();
				
		
		// Set state to lobby at the enabling.
		this.state = EnderWarState.LOBBY;
		
		
		// Get the console log
		Logger log = Bukkit.getLogger();
		
		// Check spawn id order
		int id = 1;
		for(int i : gameSpawns.keySet()) {
			if(id != i) break;
			id++;
		}
		
		if(id != gameSpawns.keySet().toArray().length + 1) {
			
			setState(EnderWarState.ERROR);
			
			// Write an alert in the console
			log.log(Level.SEVERE, "[EnderWar] ----------------------------------------------------------------");
			log.log(Level.SEVERE, "[EnderWar] /!\\            [EnderWar] Block server launching !          /!\\");
			log.log(Level.SEVERE, "[EnderWar]         Spawns id are not correct. Spawn " + id + " does not exist   ");
			log.log(Level.SEVERE, "[EnderWar] ----------------------------------------------------------------");
			motd = prefix + ChatColor.DARK_RED + "Spawns id are not correct !";
			
			
		// Not enough game spawns in config file.
		}else if(gameSpawns.size() < getEnderWarConfig().getMaxPlayers()) {
			
			setState(EnderWarState.ERROR);
			
			
			// Write an alert in the console
			log.log(Level.SEVERE, "[EnderWar] ----------------------------------------------------------------");
			log.log(Level.SEVERE, "[EnderWar] /!\\            [EnderWar] Block server launching !          /!\\");
			log.log(Level.SEVERE, "[EnderWar] The number of spawns must be more than the number of max players");
			log.log(Level.SEVERE, "[EnderWar] ----------------------------------------------------------------");
			motd = prefix + ChatColor.DARK_RED + "The number of spawns must be more than the number of max players";
			
		// Min players > Max Player in config.
		}else if(getEnderWarConfig().getMinPlayers() > getEnderWarConfig().getMaxPlayers()){
			
			setState(EnderWarState.ERROR);
			
			// Write an alert in the console
			log.log(Level.SEVERE, "[EnderWar] ---------------------------------------------------------------------");
			log.log(Level.SEVERE, "[EnderWar] /!\\              [EnderWar] Block server launching !             /!\\");
			log.log(Level.SEVERE, "[EnderWar] The number of max players must be more than the number of min players");
			log.log(Level.SEVERE, "[EnderWar] ---------------------------------------------------------------------");
			motd = prefix + ChatColor.DARK_RED + "The number of max players must be more than the number of min players";
			
		}else {
			motd = prefix + ChatColor.AQUA + "Welcome to " + ChatColor.GREEN + ChatColor.BOLD + "EnderWar " + ChatColor.RESET + ChatColor.AQUA + "!";
		}
		
		
		
		// Register all events
		Bukkit.getPluginManager().registerEvents(new PluginManager(this), this); // Main events
		Bukkit.getPluginManager().registerEvents(new OtherEvents(this), this); // Canceled events
		
		// /enderwar command executor
		getCommand("enderwar").setExecutor(new EnderWarCommand(this));
		
		
		// Setup the world
		initWorld(Bukkit.getWorld(getConfig().getString("Locations.worldname")));
		
		
		super.onEnable();
	}
	
	
	/* Disabling of the plugin */
	@Override
	public void onDisable() {
		// Kick all players
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.kickPlayer(this.prefix + ChatColor.GREEN + "Reloading for prepare the next game ...");
		}
		clearPlayersAndSpectators();
		super.onDisable();
	}
	
	
	
	/* Spawns getter and setter */
	public HashMap<Integer, Location> getGameSpawns(){
		return this.gameSpawns;
	}
	
	// Lobby spawn getter
	public Location getLobbySpawn() {
		return this.lobbySpawn;
	}
	
	// Spectator spawn getter
	public Location getSpectatorSpawn() {
		return this.spectatorSpawn;
	}
	
	
	
	/* Config manager getter */
	public ConfigUtils getEnderWarConfig() {
		return this.configUtils;
	}
	
	
	/* State getter and setter */
	
	// Set the current game state
	public void setState(EnderWarState state) {
		this.state = state;
	}

	// Get the current game state
	public EnderWarState getState() {
		return this.state;
	}
	
	
	@SuppressWarnings("deprecation")
	private void initWorld(World world) {
		world.setGameRuleValue("doDaylightCycle", "false");
		world.setTime(6000);
		world.setDifficulty(Difficulty.PEACEFUL);
	}
	
	
	
	/* Online Players getter, adders and removers */
	
	// Get the players of the game
	public ArrayList<Player> getPlayers(){
		return this.players;
	}
	
	// Get the spectators of the game
	public ArrayList<Player> getSpectators(){
		return this.spectators;
	}
	
	// Add a player to the game
	public void addPlayer(Player p) {
		if(!this.players.contains(p)) {
			this.players.add(p);// Add to player list
			p.setGameMode(GameMode.ADVENTURE); // Gamemode set to adventure
			p.setPlayerListName(ChatColor.GRAY + " [" + ChatColor.GREEN +"Player"+ ChatColor.GRAY  +"] " + ChatColor.GREEN + p.getName() + ChatColor.RESET); // Change TAB Name
			p.setDisplayName(ChatColor.GRAY + "[" + ChatColor.GREEN +"Player"+ ChatColor.GRAY  +"] " + ChatColor.GREEN + p.getName() + ChatColor.RESET); // Change ChatName
		}
	}
	
	// Add a spectator to the game
	public void addSpectator(Player p) {
		if(!this.spectators.contains(p)) {
			this.spectators.add(p); // Add to spectator list
			p.setGameMode(GameMode.SPECTATOR); // Gamemode set to spectator
			p.setPlayerListName(ChatColor.GRAY + " [" + ChatColor.DARK_GRAY +"Spectator"+ ChatColor.GRAY  +"] " + ChatColor.DARK_GRAY + p.getName() + ChatColor.RESET); // Change TAB Name
			p.setDisplayName(ChatColor.GRAY + "[" + ChatColor.DARK_GRAY +"Spectator"+ ChatColor.GRAY  +"] " + ChatColor.DARK_GRAY + p.getName() + ChatColor.RESET); // Change ChatName
		} 
	}
	
	// Add a player to the game
	public void removePlayer(Player p) {
		if(this.players.contains(p)) {
			this.players.remove(p);
			p.setPlayerListName(p.getName()); // Reset TAB Name
			p.setDisplayName(p.getName()); // Reset ChatName
		}
	}
		
	// Add a spectator to the game
	public void removeSpectator(Player p) {
		if(this.spectators.contains(p)) {
			this.spectators.remove(p);
			p.setPlayerListName(p.getName()); // Reset TAB Name
			p.setDisplayName(p.getName()); // Reset ChatName
		}
	}
	
	// Clear all players and spectators
	public void clearPlayersAndSpectators() {
		this.players.clear();
		this.spectators.clear();
		System.out.println(prefix + "All players removed form the game");
	}
	
}
