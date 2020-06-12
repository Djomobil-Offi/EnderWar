package fr.djomobil.eventsmanager;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import fr.djomobil.enderwar.EnderWar;
import fr.djomobil.enderwar.EnderWarState;

public class OtherEvents implements Listener {
	
	
	private EnderWar main;

	public OtherEvents() {}

	public OtherEvents(EnderWar enderWar) {
		this.main = enderWar;
	}

	@EventHandler
	public void onDropItem(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(!e.getPlayer().isOp()) e.setCancelled(true);
	}
	
	@EventHandler
	public void onBLockPlaced(BlockPlaceEvent e) {
		if(!e.getPlayer().isOp()) e.setCancelled(true);
	}
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent e) {
		if(e.getEntityType() != EntityType.ENDER_PEARL) {
			//e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemSpawn(ItemSpawnEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onItemChangeHand(PlayerSwapHandItemsEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onServerPing(ServerListPingEvent e) {
		e.setMotd(main.motd);
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player && (main.getState() == EnderWarState.LOBBY || main.getState() == EnderWarState.PREGAME)){
			e.setCancelled(true);
		}
		else{
			e.setDamage(0.0);
		}
	}
}
