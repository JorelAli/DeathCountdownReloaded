package io.github.skepter.dcreloaded.listeners;

import io.github.skepter.dcreloaded.Main;
import io.github.skepter.dcreloaded.api.DCPlayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
	Main plugin;

	public ChatListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		boolean ranksEnabled = this.plugin.getConfig().getBoolean("enableRanks");
		Player player = event.getPlayer();
		for (String str : this.plugin.getConfig().getStringList("blacklistedWorlds")) {
			if (player.getWorld().getName().equals(str)) {
				return;
			}
		}
		String currentname = player.getName();
		int time = new DCPlayer(player).getTime();
		ChatColor r = ChatColor.RESET;
		ChatColor e;
		ChatColor a = ChatColor.GREEN;
		ChatColor c = ChatColor.RED;
		e = ChatColor.YELLOW;
		ChatColor g = ChatColor.GOLD;
		ChatColor d = ChatColor.DARK_GREEN;
		ChatColor s = ChatColor.DARK_RED;
		ChatColor p = ChatColor.AQUA;

		int desperatebegin = this.plugin.getConfig().getInt("desperate");
		int goodbegin = this.plugin.getConfig().getInt("low");
		int richbegin = this.plugin.getConfig().getInt("average");
		int superrichbegin = this.plugin.getConfig().getInt("good");
		int reallyrich = this.plugin.getConfig().getInt("reallyrich");

		int desperateend = goodbegin - 1;
		int goodend = richbegin - 1;
		int richend = superrichbegin - 1;
		int superrichend = reallyrich - 1;
		if (ranksEnabled) {
			if ((player.getName().equals(this.plugin.getConfig().getString("ownerName"))) && (this.plugin.getConfig().getBoolean("ownerTimeLord"))) {
				player.setDisplayName(s + "[" + g + "[" + p + "TimeLord" + g + "]" + s + "] " + p + this.plugin.getConfig().getString("ownerName")
						+ r);
				return;
			}
			if ((time > desperatebegin) && (time < desperateend)) {
				player.setDisplayName(c + "[Desperate] " + r + currentname);
				return;
			}
			if ((time > goodbegin) && (time < goodend)) {
				player.setDisplayName(e + "[Low] " + r + currentname);
				return;
			}
			if ((time > richbegin) && (time < richend)) {
				player.setDisplayName(a + "[Average] " + r + currentname);
				return;
			}
			if ((time > superrichbegin) && (time < superrichend)) {
				player.setDisplayName(d + "[Good] " + r + currentname);
				return;
			}
			if (time <= reallyrich) {
				return;
			}
			//player.setDisplayName(p + "[" + g + "Great" + p + "] " + r + currentname);
		}
		// What does this code even do?
		// } else if ((ranksEnabled = 1) != 0) {
		// List<String> ranks = this.plugin.getConfig().getStringList("Ranks");
		// ChatColor.translateAlternateColorCodes('&', "");
		// if
		// ((player.getName().equals(this.plugin.getConfig().getString("ownerName")))
		// && (this.plugin.getConfig().getBoolean("ownerTimeLord"))) {
		// player.setDisplayName(ChatColor.translateAlternateColorCodes('&',
		// this.plugin.getConfig().getString("timeLordPrefix"))
		// + r + this.plugin.getConfig().getString("ownerName"));
		// return;
		// }
		// for (String rank : ranks) {
		// String prefix = this.plugin.getConfig().getString("Rank." + rank +
		// ".prefix");
		// if ((time > this.plugin.getConfig().getDouble("Rank." + rank +
		// ".lowerTime"))
		// && (time < this.plugin.getConfig().getDouble("Rank." + rank +
		// ".upperTime"))) {
		// player.setDisplayName(ChatColor.translateAlternateColorCodes('&',
		// prefix) + r + currentname);
		// return;
		// }
		// }
		// }
	}
}
