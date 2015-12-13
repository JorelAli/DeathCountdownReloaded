package io.github.skepter.dcreloaded.cmds;

import io.github.skepter.dcreloaded.Main;
import io.github.skepter.dcreloaded.api.DCPlayer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeCommand implements CommandExecutor {
	Main plugin;

	public TimeCommand(Main plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if ((sender instanceof Player)) {
			Player player = (Player) sender;
			ChatColor a = ChatColor.GREEN;
			ChatColor g = ChatColor.GRAY;
			if ((command.getName().equalsIgnoreCase("dctime")) && (args.length == 0)) {
				player.sendMessage(this.plugin.prefix + g + "Your current time is: " + a + new DCPlayer(player).getTime());
			}
		}
		return true;
	}
}
