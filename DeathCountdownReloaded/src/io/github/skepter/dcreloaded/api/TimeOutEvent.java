package io.github.skepter.dcreloaded.api;

import io.github.skepter.dcreloaded.Main;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;

public final class TimeOutEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	Main plugin;
	private Player player;
	private World world;

	public TimeOutEvent(Main plugin, Player player) {
		String worldname = player.getWorld().getName();

		this.player = player;
		this.plugin = plugin;
		this.world = Bukkit.getWorld(worldname);
		DCPlayer dcplayer = new DCPlayer(player);
		
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
		
		for (String str : plugin.getConfig().getStringList("blacklistedWorlds")) {
			if (player.getWorld().getName().equals(str)) {
				setCancelled(true);
				return;
			}
		}
		
		if (plugin.getConfig().getBoolean("perWorldBanning")) {
			plugin.addBannedWorld(player, player.getWorld().getName());
			for(World world : Bukkit.getWorlds()) {
				if (!plugin.getBannedWorlds(player).contains(world.getName())) {
					if (plugin.getConfig().getBoolean("banBroadcast")) {
						Bukkit.broadcastMessage(plugin.prefix + player.getName() + " timed out from " + worldname);
					}
					if (plugin.getConfig().getBoolean("banSound")) {
						for(Player p : Bukkit.getOnlinePlayers()) {
							p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 100.0F, 2.0F);
						}
					}
					player.teleport(world.getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
					break;
				}
			}
		}
		
		//player.kickPlayer(plugin.prefix + "Sorry " + player.getName() + ", you timed out!");
		/* Nope. banning isn't happening*/
		//player.setBanned(true);
		if (plugin.getConfig().getBoolean("banSound")) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.playSound(p.getLocation(), Sound.WITHER_SPAWN, 100.0F, 2.0F);
			}
		}
		if (plugin.getConfig().getBoolean("banBroadcast")) {
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
