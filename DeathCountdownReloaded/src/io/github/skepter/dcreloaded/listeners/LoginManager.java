package io.github.skepter.dcreloaded.listeners;

import io.github.skepter.dcreloaded.DeathCountdown;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class LoginManager implements Listener {
	DeathCountdown plugin;

	public LoginManager(DeathCountdown plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if ((!player.hasPlayedBefore()) || (!this.plugin.check(player))) {
			player.sendMessage(this.plugin.prefix + "Adding you to the data file...");
			this.plugin.create(player, this.plugin.getConfig().getInt("startTime"));
			if ((this.plugin.getConfig().getBoolean("giveTTDevice"))
					&& (!player.getInventory().contains(this.plugin.TTDevice()))) {
				player.getInventory().addItem(new ItemStack[] { this.plugin.TTDevice() });
			}
			player.sendMessage(this.plugin.prefix + "Done!");
		} else {
			if ((this.plugin.getConfig().getBoolean("giveTTDevice"))
					&& (!player.getInventory().contains(this.plugin.TTDevice()))) {
				player.getInventory().addItem(new ItemStack[] { this.plugin.TTDevice() });
			}
			player.sendMessage(this.plugin.prefix + "Continuing where you left off");
		}
		if (player.getName() == this.plugin.getConfig().getString("ownerName")) {
			event.setJoinMessage(ChatColor.GOLD + this.plugin.getConfig().getString("ownerName") + ChatColor.GREEN
					+ " the Owner has entered the server!");
		}
		for (String str : this.plugin.getConfig().getStringList("blacklistedWorlds")) {
			if (player.getWorld().getName().equals(str)) {
				return;
			}
		}
		this.plugin.start(this.plugin, player);
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		for (String str : this.plugin.getConfig().getStringList("blacklistedWorlds")) {
			if (event.getPlayer().getWorld().getName().equals(str)) {
				return;
			}
		}
		this.plugin.cancelTask(event.getPlayer());
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		for (String str : this.plugin.getConfig().getStringList("blacklistedWorlds")) {
			if (event.getPlayer().getWorld().getName().equals(str)) {
				return;
			}
		}
		if (event.getResult() == PlayerLoginEvent.Result.KICK_BANNED) {
			event.setKickMessage(this.plugin.prefix + "Sorry " + event.getPlayer().getName() + " You have timed out!");
		}
	}
}
