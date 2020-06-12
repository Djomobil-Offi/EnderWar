package fr.djomobil.enderwar;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class EnderWarCommand implements CommandExecutor, TabCompleter {

	
	private EnderWar main;
	
	
	
	private final String inccorect = ChatColor.GREEN + "Invalid. Use " + ChatColor.RED + "/enderwar help";
	
	private final String help = ChatColor.GREEN + "EnderWar command help:\n"
								+ ChatColor.RED + "    /enderwar help " + ChatColor.GREEN + ": the default help command.\n"
								+ ChatColor.RED + "    /enderwar listSpawns " + ChatColor.GREEN + ": command for list all spawns.\n";
	
	public EnderWarCommand(EnderWar enderWar) {
		this.main = enderWar;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		if(sender instanceof Player) {
			
			Player p = (Player) sender;
			
			// /enderwar 
			if(args.length == 0) {
				sender.sendMessage(main.prefix + ChatColor.GREEN + "A plugin by " + ChatColor.RED + "Djomobil_Offi" + ChatColor.GREEN + ".\n"
						+ "Use " + ChatColor.RED + "/enderwar help");
				
				// /enderwar <arg[0]>
			}else if(args.length == 1) {
				
					// The player must be opped for execute this command
					if(p.isOp()) {
					
					switch (args[0]) {
					
					case "help":
						p.sendMessage(main.prefix + help);
						break;
					
					case "listSpawns":
						p.sendMessage(main.prefix + ChatColor.GREEN + "Spawns :\n");
						
						for(int i : main.getGameSpawns().keySet()) {
							Location loc = main.getGameSpawns().get(i);
							p.sendMessage(ChatColor.YELLOW + "  " + i + ": " + ChatColor.RED + "x=" + ChatColor.DARK_RED + loc.getX()
																			 + ChatColor.RED + ", y=" + ChatColor.DARK_RED + loc.getY()
																			 + ChatColor.RED + ", z=" + ChatColor.DARK_RED + loc.getZ());
						}
						
						break;
						// The first argument is incorrect
					default:
						p.sendMessage(main.prefix + inccorect);
						break;
					}
					
				}else {
					p.sendMessage(main.prefix + ChatColor.DARK_RED + "You must be opped for execute this command !");
				}
			
			}else{
				p.sendMessage(main.prefix + ChatColor.DARK_RED + "Too much argument. " + ChatColor.RED + "/enderwar help");
			}
			
		}else {
			sender.sendMessage(main.prefix + ChatColor.DARK_RED + "Command can be execute only in game !");
		}
		
		
		// Command successful
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

		ArrayList<String> completer = new ArrayList<>();
		
		
		if(cmd.getName().equalsIgnoreCase("enderwar")) {
			completer.add("help");
			completer.add("listSpawns");
		}
		
		return completer;
	}

}
