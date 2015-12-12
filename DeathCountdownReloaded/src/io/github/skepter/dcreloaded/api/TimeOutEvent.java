package io.github.skepter.dcreloaded.api;

import io.github.skepter.dcreloaded.Main;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class TimeOutEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	Main plugin;
	private Player player;
	private World world;

	public TimeOutEvent(Main plugin, Player player) {

		this.player = player;
		this.plugin = plugin;
		this.world = player.getWorld();
		
		DCPlayer dcplayer = new DCPlayer(player);
		
		if(dcplayer.isInBlacklistedWorld())
			return;
		
		if (dcplayer.isAdmin()) {
			dcplayer.setTime(plugin.getConfig().getInt("startTime"));
			setCancelled(true);
			return;
		}
		
		if (dcplayer.canRevive()) {
			dcplayer.setRevive(false);
			dcplayer.setTime(plugin.getConfig().getInt("startTime"));
			player.sendMessage(plugin.prefix + "You were revived!");
			setCancelled(true);
			return;
		}
		
		if (plugin.getConfig().getBoolean("playSound")) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 100.0F, 2.0F);
			}
		}
		if (plugin.getConfig().getBoolean("doBroadcast")) {
			Bukkit.broadcastMessage(plugin.prefix + player.getName() + " timed out!");
		}
	}

	public Player getPlayer() {
		return this.player;
	}

	public World getWorld() {
		return this.world;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public boolean isCancelled() {
		return false;
	}

	public void setCancelled(boolean arg0) {
	}
}
