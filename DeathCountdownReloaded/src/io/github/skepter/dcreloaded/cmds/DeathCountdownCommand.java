package io.github.skepter.dcreloaded.cmds;

import io.github.skepter.dcreloaded.Main;
import io.github.skepter.dcreloaded.api.DCPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class DeathCountdownCommand implements CommandExecutor {
	Main plugin;

	public DeathCountdownCommand(Main plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if ((command.getName().equalsIgnoreCase("dc"))
				&& ((sender.hasPermission(this.plugin.command)) || (sender.isOp()) || ((sender instanceof ConsoleCommandSender)) || ((sender instanceof BlockCommandSender)))) {
			ChatColor a = ChatColor.GREEN;
			ChatColor g = ChatColor.GRAY;
			if (args.length == 0) {
				sender.sendMessage(this.plugin.prefix + "Welcome to the DeathCountdown control panel. Here's a list of commands:");
				sender.sendMessage(a + "/dc " + g + "Displays this help page");
				sender.sendMessage(a + "/dc give <player> <time> " + g + "Give time to a player");
				sender.sendMessage(a + "/dc take <player> <time> " + g + "Take time from a player");
				sender.sendMessage(a + "/dc set <player> <time> " + g + "Sets a players time");
				sender.sendMessage(a + "/dc check <player> " + g + "Check a players time");
				sender.sendMessage("");
				sender.sendMessage(a + "/dc setadmin <player> true/false " + g + "Set's a player's state as Admin");
				sender.sendMessage(a + "/dc checkadmin <player> " + g + "Check's if a player is an Admin");
				sender.sendMessage(a + "/dc setrevivable <player> true/false " + g + "Set's a player's state as revivable");
				sender.sendMessage(a + "/dc checkrevivable <player> " + g + "Check's if a player is revivable");
				sender.sendMessage("");
				sender.sendMessage(a + "/dc revive <player> " + g + "Revives a banned player");
				sender.sendMessage("");
				sender.sendMessage(a + "/dc unban <player> <world>" + g + "Unbans a player from a world");
				sender.sendMessage(a + "/dc checkbans <player> " + g + "Checks world bans from a player");
				sender.sendMessage("");
				sender.sendMessage(a + "/dc reload " + g + "Reloads plugin");
				sender.sendMessage(a + "/dc listperms " + g + "Lists the permissions from this plugin");
			} else {

				/* Get the target player */
				Player target = null;
				try {
					target = Bukkit.getPlayerExact(args[1]);
				} catch (Exception e) {
					sender.sendMessage(this.plugin.prefix + "Could not find player!");
					return true;
				}
				DCPlayer dcplayer = new DCPlayer(target);
				int time = dcplayer.getTime();

				switch (args[0].toLowerCase()) {
					case "give":
						int amountToGive = 0;
						try {
							amountToGive = Integer.parseInt(args[2]);
						} catch (NumberFormatException e) {
							sender.sendMessage(this.plugin.prefix + "That is not a number!");
							return true;
						}
						int newTime = time + amountToGive;
						dcplayer.setTime(newTime);
						sender.sendMessage(this.plugin.prefix + "You gave " + a + amountToGive + g + " to " + target.getName());
						target.sendMessage(this.plugin.prefix + sender.getName() + " gave you " + a + amountToGive + g + " time");
						return true;
					case "take":
						int amountToTake = 0;
						try {
							amountToTake = Integer.parseInt(args[2]);
						} catch (NumberFormatException e) {
							sender.sendMessage(this.plugin.prefix + "That is not a number!");
							return true;
						}

						int newTime1 = time - amountToTake;
						dcplayer.setTime(newTime1);
						sender.sendMessage(this.plugin.prefix + "You took " + a + amountToTake + g + " from " + target.getName());
						target.sendMessage(this.plugin.prefix + sender.getName() + " took " + a + amountToTake + g + " time from you");
						return true;
					case "set":
						int amountToSet = 0;
						try {
							amountToSet = Integer.parseInt(args[2]);
						} catch (NumberFormatException e) {
							sender.sendMessage(this.plugin.prefix + "That is not a number!");
							return true;
						}

						int newTime2 = amountToSet;
						dcplayer.setTime(newTime2);
						sender.sendMessage(this.plugin.prefix + target.getName() + "'s time has been set to: " + amountToSet);
						target.sendMessage(this.plugin.prefix + sender.getName() + " set your time to " + a + amountToSet);
						return true;
					case "check":
						sender.sendMessage(this.plugin.prefix + target.getName() + "'s time is: " + a + time);
						return true;
				}
				if (args[0].equalsIgnoreCase("setadmin")) {
					Player target = null;
					try {
						target = Bukkit.getPlayerExact(args[1]);
					} catch (Exception e) {
						sender.sendMessage(this.plugin.prefix + "Could not find player!");
						return true;
					}
					boolean isAdmin = false;
					try {
						isAdmin = Boolean.parseBoolean(args[2]);
					} catch (Exception e) {
						sender.sendMessage(this.plugin.prefix + "Please enter true or false!");
					}
					this.plugin.setAdmin(target, isAdmin);
					if (isAdmin) {
						sender.sendMessage(this.plugin.prefix + target.getName() + " is now Admin status");
					} else {
						sender.sendMessage(this.plugin.prefix + target.getName() + " is no longer Admin status");
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("checkadmin")) {
					Player target = null;
					try {
						target = Bukkit.getPlayerExact(args[1]);
					} catch (Exception e) {
						sender.sendMessage(this.plugin.prefix + "Could not find player!");
						return true;
					}
					boolean isAdmin = this.plugin.getAdmin(target);
					if (isAdmin) {
						sender.sendMessage(this.plugin.prefix + target.getName() + " is an Admin");
					} else {
						sender.sendMessage(this.plugin.prefix + target.getName() + " is not an Admin");
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("setrevivable")) {
					Player target = null;
					try {
						target = Bukkit.getPlayerExact(args[1]);
					} catch (Exception e) {
						sender.sendMessage(this.plugin.prefix + "Could not find player!");
						return true;
					}
					boolean canRevive = false;
					try {
						canRevive = Boolean.parseBoolean(args[2]);
					} catch (Exception e) {
						sender.sendMessage(this.plugin.prefix + "Please enter true or false!");
					}
					this.plugin.setRevive(target, canRevive);
					if (canRevive) {
						sender.sendMessage(this.plugin.prefix + target.getName() + " can now revive");
					} else {
						sender.sendMessage(this.plugin.prefix + target.getName() + " can no longer revive");
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("checkrevivable")) {
					Player target = null;
					try {
						target = Bukkit.getPlayerExact(args[1]);
					} catch (Exception e) {
						sender.sendMessage(this.plugin.prefix + "Could not find player!");
						return true;
					}
					boolean canRevive = this.plugin.getRevive(target);
					if (canRevive) {
						sender.sendMessage(this.plugin.prefix + target.getName() + " can revive");
					} else {
						sender.sendMessage(this.plugin.prefix + target.getName() + " can not revive");
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("revive")) {
					OfflinePlayer player = null;
					try {
						player = Bukkit.getOfflinePlayer(args[1]);
					} catch (Exception e) {
						sender.sendMessage(this.plugin.prefix + "Could not find that player!");
						return true;
					}
					this.plugin.unban(player);
					sender.sendMessage(this.plugin.prefix + args[1] + " revived");
					return true;
				}
				if (args[0].equalsIgnoreCase("reload")) {
					this.plugin.reloadConfig();
					this.plugin.getServer().getScheduler().cancelTasks(this.plugin);
					this.plugin.restartScheduler();
					sender.sendMessage(this.plugin.prefix + "DeathCountdown reloaded");
					return true;
				}
				if (args[0].equalsIgnoreCase("listperms")) {
					sender.sendMessage(a + this.plugin.command + g + " Allows the player to use the /dc command");
					sender.sendMessage(a + this.plugin.sign + g + " Allows the player to create a sign");
					return true;
				}
				if (args[0].equalsIgnoreCase("unban")) {
					Player target = null;
					try {
						target = Bukkit.getPlayerExact(args[1]);
					} catch (Exception e) {
						sender.sendMessage(this.plugin.prefix + "Could not find player!");
						return true;
					}
					World world = null;
					try {
						world = Bukkit.getWorld(args[2]);
					} catch (Exception e) {
						sender.sendMessage(this.plugin.prefix + "Could not find world!");
						return true;
					}
					this.plugin.removeBannedWorld(target, world.getName());
					sender.sendMessage(this.plugin.prefix + "Successfully unbanned " + target.getName() + " from the world " + world.getName());
					return true;
				}
				if (args[0].equalsIgnoreCase("checkbans")) {
					Player target = null;
					try {
						target = Bukkit.getPlayerExact(args[1]);
					} catch (Exception e) {
						sender.sendMessage(this.plugin.prefix + "Could not find player!");
						return true;
					}
					String s = this.plugin.getBannedWorlds(target);
					String s1 = s.replaceAll("-", ", ");
					sender.sendMessage(this.plugin.prefix + target.getName() + " is banned from these worlds: " + s1);
					return true;
				}
				sender.sendMessage(this.plugin.prefix + "Unknown argument");
				return true;
			}
		}
		sender.sendMessage(this.plugin.prefix + "You don't have permission to use /DC");
		return true;
	}
}
