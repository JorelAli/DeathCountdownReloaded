package io.github.skepter.dcreloaded.listeners;

import io.github.skepter.dcreloaded.DeathCountdown;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

public class SignListener implements Listener {
	DeathCountdown plugin;

	public SignListener(DeathCountdown plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onCreate(SignChangeEvent event) {
		for (String str : this.plugin.getConfig().getStringList("blacklistedWorlds")) {
			if (event.getPlayer().getWorld().getName().equals(str)) {
				return;
			}
		}
		if (event.getLine(0).equalsIgnoreCase("[BuyTime]")) {
			if ((event.getPlayer().hasPermission(this.plugin.sign)) || (event.getPlayer().isOp())) {
				event.setLine(0, ChatColor.GREEN + "[BuyTime]");
				if ((isInteger(event.getLine(1))) && (isInteger(event.getLine(2))) && (isInteger(event.getLine(3)))) {
					event.setLine(1, "Price:" + event.getLine(1));
					ItemStack item = new ItemStack(Material.getMaterial(Integer.parseInt(event.getLine(2))));
					event.setLine(2, event.getLine(2) + ":" + item.getType().toString().toLowerCase().replace("_", ""));
					event.setLine(3, "Amount:" + event.getLine(3));
					event.getPlayer().sendMessage(this.plugin.prefix + "BuyTime sign created");
					return;
				}
				event.getPlayer().sendMessage(this.plugin.prefix + "Incorrect format!");
				return;
			}
			event.setCancelled(true);
			event.getPlayer().sendMessage(this.plugin.prefix + "You do not have powers to create DeathCountdown Signs");
			return;
		}
		if (event.getLine(0).equalsIgnoreCase("[SellTime]")) {
			if ((event.getPlayer().hasPermission(this.plugin.sign)) || (event.getPlayer().isOp())) {
				event.setLine(0, ChatColor.GREEN + "[SellTime]");
				if ((isInteger(event.getLine(1))) && (isInteger(event.getLine(2))) && (isInteger(event.getLine(3)))) {
					event.setLine(1, "Price:" + event.getLine(1));
					ItemStack item = new ItemStack(Material.getMaterial(Integer.parseInt(event.getLine(2))));
					event.setLine(2, event.getLine(2) + ":" + item.getType().toString().toLowerCase().replace("_", ""));
					event.setLine(3, "Amount:" + event.getLine(3));
					event.getPlayer().sendMessage(this.plugin.prefix + "SellTime sign created");
					return;
				}
				event.getPlayer().sendMessage(this.plugin.prefix + "Incorrect format!");
				return;
			}
			event.setCancelled(true);
			event.getPlayer().sendMessage(this.plugin.prefix + "You do not have powers to create DeathCountdown Signs");
			return;
		}
	}

	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
