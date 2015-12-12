package io.github.skepter.dcreloaded.cmds;

import io.github.skepter.dcreloaded.DeathCountdown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DCTopCommand implements CommandExecutor {
	DeathCountdown plugin;

	public DCTopCommand(DeathCountdown plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("dctop")) {
			if ((args.length == 0) || (args.length > 1)) {
				sender.sendMessage(this.plugin.prefix + "Use /DCTop <page number>");
				return true;
			}
			HashMap<Integer, List<String>> pages = new HashMap();
			ChatColor a = ChatColor.GREEN;
			ChatColor g = ChatColor.GRAY;

			ArrayList<String> times = this.plugin.getTopTime();
			ArrayList<String> players = this.plugin.getTopPlayers();
			ArrayList<String> textData = new ArrayList();
			for (int i = 0; i < times.size(); i++) {
				textData.add(g + (String) players.get(i) + ": " + a + (String) times.get(i));
			}
			int arg = 0;
			try {
				arg = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage(this.plugin.prefix + args[0] + " is not a number!");
				return true;
			}
			int j = textData.size() / 10;
			int k = textData.size() % 10;
			for (int i = 1; i < j; i++) {
				pages.put(Integer.valueOf(i), textData.subList(i * 10, (i + 1) * 10));
			}
			System.out.println("textData: " + textData.size());

			System.out.println("j: " + j);
			System.out.println("k: " + k);
			if (arg > j) {
				sender.sendMessage(this.plugin.prefix + "That number is too large!");
				return true;
			}
			if (arg == j) {
				sender.sendMessage(this.plugin.prefix + "Showing page " + a + arg + g + "/" + a + j);
				for (String s : textData.subList(textData.size() - k, textData.size())) {
					sender.sendMessage(s);
				}
				return true;
			}
			sender.sendMessage(this.plugin.prefix + "Showing page " + a + arg + g + "/" + a + j);
			System.out.println("Showing page " + arg + "/" + j);
			for (String s : (List) pages.get(Integer.valueOf(arg))) {
				sender.sendMessage(s);
			}
			return true;
		}
		return false;
	}
}
