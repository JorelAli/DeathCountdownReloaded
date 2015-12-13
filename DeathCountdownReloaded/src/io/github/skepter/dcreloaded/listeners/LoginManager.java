package io.github.skepter.dcreloaded.listeners;

import io.github.skepter.dcreloaded.Main;
import io.github.skepter.dcreloaded.api.DCPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LoginManager implements Listener {
	Main plugin;

	public LoginManager(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		DCPlayer dcplayer = new DCPlayer(player);
		if ((!player.hasPlayedBefore()) || (!dcplayer.isInDatabase())) {
			player.sendMessage(this.plugin.prefix + "Adding you to the data file...");
			dcplayer.addToDatabase(this.plugin.getConfig().getInt("startTime"));
			player.sendMessage(this.plugin.prefix + "Done!");
		}
		if(dcplayer.isInBlacklistedWorld())
			return;
		dcplayer.start();
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		DCPlayer player = new DCPlayer(event.getPlayer());
		if(player.isInBlacklistedWorld())
			return;
		player.stop();
	}
}
