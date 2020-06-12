package fr.djomobil.eventsmanager;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import fr.djomobil.enderwar.EnderWar;
import fr.djomobil.enderwar.EnderWarFinish;
import fr.djomobil.enderwar.EnderWarLauncher;
import fr.djomobil.enderwar.EnderWarState;
import fr.djomobil.utils.EnderWarPearl;
import fr.djomobil.utils.PearlTask;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PluginManager implements Listener {

	private EnderWar main;
	private boolean pearlUsable = true;
	
	public PluginManager(EnderWar enderWar) {
		this.main = enderWar;
	}
	
	 @EventHandler
	 public void onJoin(PlayerJoinEvent e) {
		 
		 // The player who joined the server
		 Player p = e.getPlayer();
		 
		 //Cancel logging if State = ERROR
		 if(main.getState() == EnderWarState.ERROR) {
			 
			 p.kickPlayer(main.prefix + ChatColor.DARK_RED  + ChatColor.BOLD + "[ERROR]" + ChatColor.RESET + ChatColor.DARK_RED +  " : Can't connect ! Plugin configuration error. Look consol.");
		 }
		 
		 
		 
		 // Setup the player
		 p.getInventory().clear();
		 p.setLevel(0);
		 p.setExp(0.0f);
		 p.setHealth(20.0);
 		 p.setFoodLevel(20);
  		 p.setPlayerListHeaderFooter(ChatColor.GREEN + "Welcome to " + ChatColor.DARK_GREEN + "EnderWar" + ChatColor.YELLOW + " v1.0 " + ChatColor.GREEN + "! Have a nice game.", ChatColor.GREEN + "A plugin by " + ChatColor.YELLOW + "Djomobil_Offi");
  		 
  		 p.teleport(main.getLobbySpawn()); // Teleport to lobby spawn
		 
		 
		 // GAMESTATE : LOBBY or PRE-GAME | The game hasn't start
		 if(main.getState() == EnderWarState.LOBBY || main.getState() == EnderWarState.PREGAME) {
			 
			 // Changing the join message
			 e.setJoinMessage(main.prefix + ChatColor.AQUA + "Welcome " + ChatColor.RED + p.getName() + ChatColor.AQUA + " to " + ChatColor.GREEN + ChatColor.BOLD + "EnderWar" + ChatColor.RESET + ChatColor.AQUA + ". Have a nice game !");
			 
			 // The current number of player is under the max size in the config
			 if(main.getPlayers().size() < main.getEnderWarConfig().getMaxPlayers()) {
				 main.addPlayer(p); // Player added to the game
			 }else {
				 main.addSpectator(p); // Player added to spectator
				 p.sendMessage(main.prefix + ChatColor.GREEN + "The number of maximum player is already reached. You have been mooved to specator."); // Message for the player
			 }
			 
		 // The game is already started
		 }else {
			 main.addSpectator(p); // Player added to spectator
			 p.sendMessage(main.prefix + ChatColor.GREEN + "The game started. You have been mooved to specator."); // Message for the player
		 }
		 
		 // Send action bar message
		 for(Player players : Bukkit.getOnlinePlayers()) {
			 players.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§f§l" + main.getPlayers().size() + " / " + main.getEnderWarConfig().getMaxPlayers()));
		 }
		 
		 //The game can start
		 if(main.getPlayers().size() >= main.getEnderWarConfig().getMinPlayers()){
			 main.setState(EnderWarState.PREGAME);
			 EnderWarLauncher launcher = new EnderWarLauncher(main);
			 launcher.runTaskTimer(main, 20, 20);
		 }
	 }
	 
	 
	 
	 @EventHandler
	 public void onQuit(PlayerQuitEvent e) {
		 
		 // The player who quit the server
		 Player p = e.getPlayer();
		 
		 // Changing the quit message
		 if(main.getState() == EnderWarState.LOBBY || main.getState() == EnderWarState.PREGAME) {
			 e.setQuitMessage(main.prefix + ChatColor.AQUA + "Bye " + ChatColor.RED + p.getName() + ChatColor.AQUA + ". See you soon...");
		 } else {
			 e.setQuitMessage(main.prefix + ChatColor.RED + p.getName() + ChatColor.AQUA + " left the game. ");
		 }
		
	     main.removePlayer(p); // Remove the player if he was player
	     main.removeSpectator(p); // Remove the player if he was spectator
	     
	     if(main.getPlayers().size() == 1) {
			 
			 Bukkit.broadcastMessage(main.prefix + ChatColor.RED + ChatColor.BOLD + main.getPlayers().get(0).getName() + ChatColor.RESET + ChatColor.GREEN + " has won the game !");
			 main.getPlayers().get(0).sendTitle(ChatColor.GREEN + "You Won", ChatColor.GREEN + "Congratulation !", 20, 30, 20);
			 
			 // Launch the finish task
			 main.setState(EnderWarState.FINISH);
			 EnderWarFinish finish = new EnderWarFinish(main);
			 finish.runTaskTimer(main, 20, 20);
		 }
	 }
	 
	 
	 
	 @EventHandler
	 public void onPlayerMove(PlayerMoveEvent e) {
		 Player p = e.getPlayer();
		 
		 // The current y location is under the die layer in config
		 if(p.getLocation().getBlockY() < main.getEnderWarConfig().getDieLayer()) {
			 // The game has started
			 if(main.getState() == EnderWarState.GAME) {
				 
				 // The player has loose
				 p.getInventory().clear();
				 main.removePlayer(p);
				 main.addSpectator(p);
				 p.teleport(main.getSpectatorSpawn());
				 p.sendTitle(ChatColor.DARK_RED + "You die !", ChatColor.RED + "You have been move to spectator", 20, 30, 20);
				 Bukkit.broadcastMessage(main.prefix + ChatColor.YELLOW + p.getName() + ChatColor.GREEN + " die. Player remaining : " + ChatColor.AQUA + main.getPlayers().size());
				 
				 
				 if(main.getPlayers().size() == 1) {
					 
					 Bukkit.broadcastMessage(main.prefix + ChatColor.RED + ChatColor.BOLD + main.getPlayers().get(0).getName() + ChatColor.RESET + ChatColor.GREEN + " has won the game !");
					 main.getPlayers().get(0).sendTitle(ChatColor.GREEN + "You Won", ChatColor.GREEN + "Congratulation !", 20, 30, 20);
					 
					 // Launch the finish task
					 main.setState(EnderWarState.FINISH);
					 EnderWarFinish finish = new EnderWarFinish(main);
					 finish.runTaskTimer(main, 20, 20);
				 }
				 
			 } else {
				 // Teleport the player if he jump into the void
				 p.teleport(main.getLobbySpawn());
			 }
		 }
		 
	 }
	 
	 
	@EventHandler
	 public void onInteract(PlayerInteractEvent e) {
		 Player p = e.getPlayer();
		 
		 ItemStack it = p.getInventory().getItemInMainHand();
		 
		 if(e.getHand() == EquipmentSlot.HAND) {
		 
			 // Item used is the Magic pearl (Action teleportation)
			 if((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && it.getType() == Material.ENDER_PEARL) {
				 
				 // Check if teleport can be used or if it is on queue
				 if(isPearlUsable()) {
					 
					 // Keep the pearl in inventory
					 p.getInventory().addItem(new EnderWarPearl());
					 
					 // Set the teleport in queue
					 setPearlUsable(false);
					 // Launch the queue
					 PearlTask pearlTask = new PearlTask(p, this);
					 pearlTask.runTaskTimer(main, 20, 20);
					 
				}else{
					e.setCancelled(true);
					p.sendMessage(main.prefix + ChatColor.RED + "You have to wait the end of the timer before use the " + ChatColor.DARK_GREEN + "Magic Pearl");
				}
			 }
		 }
	 }
	
	
	
	
	/* pearlUsable getter and setter */ 
	
	public boolean isPearlUsable() {
		return this.pearlUsable;
	}
	
	public void setPearlUsable(boolean isPearlUsable) {
		this.pearlUsable = isPearlUsable;
	}
	 
	
}
