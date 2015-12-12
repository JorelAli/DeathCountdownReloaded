package io.github.skepter.dcreloaded.listeners;

import io.github.skepter.dcreloaded.Main;
import io.github.skepter.dcreloaded.api.DCPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

public class VotifierListener implements Listener {
	Main plugin;

	public VotifierListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onVote(VotifierEvent event) {
		Vote vote = event.getVote();
		String username = vote.getUsername();
		Player player = Bukkit.getServer().getPlayer(username);
		for (String str : this.plugin.getConfig().getStringList("blacklistedWorlds")) {
			if (player.getWorld().getName().equals(str)) {
				return;
			}
		}
		DCPlayer dcplayer = new DCPlayer(player);
		int time = dcplayer.getTime();
		dcplayer.setTime(time + this.plugin.getConfig().getInt("voteAmount"));
		Bukkit.getServer().broadcastMessage(
				this.plugin.prefix + ChatColor.GREEN + username + ChatColor.GRAY
						+ " Voted for the server and got more time!");
	}
}
