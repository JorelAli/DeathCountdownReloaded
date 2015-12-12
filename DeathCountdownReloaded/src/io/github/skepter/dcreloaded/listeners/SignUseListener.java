package io.github.skepter.dcreloaded.listeners;

import io.github.skepter.dcreloaded.DeathCountdown;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SignUseListener implements Listener {
	DeathCountdown plugin;

	public SignUseListener(DeathCountdown plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		for (String str : this.plugin.getConfig().getStringList("blacklistedWorlds")) {
			if (event.getPlayer().getWorld().getName().equals(str)) {
				return;
			}
		}
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if ((event.getClickedBlock().getType() == Material.SIGN_POST)
					|| (event.getClickedBlock().getType() == Material.WALL_SIGN)) {
				Player player = event.getPlayer();
				Sign sign = (Sign) event.getClickedBlock().getState();
				if (sign.getLine(0).equalsIgnoreCase(ChatColor.GREEN + "[BuyTime]")) {
					int price = Integer.parseInt(sign.getLine(1).replaceAll("[\\D]", ""));
					int item = Integer.parseInt(sign.getLine(2).replaceAll("[\\D]", ""));
					int amount = Integer.parseInt(sign.getLine(3).replaceAll("[\\D]", ""));
					ItemStack itemstack = new ItemStack(item, amount);
					player.getInventory().addItem(new ItemStack[] { itemstack });
					player.updateInventory();
					int time = this.plugin.getTime(player);
					int MinusTime = time - price;
					this.plugin.setTime(player, MinusTime);
					player.sendMessage(this.plugin.prefix + "You spent: " + price + " time on " + amount + " "
							+ itemstack.getType().toString().toLowerCase().replace("_", " "));
					return;
				}
				if (sign.getLine(0).equalsIgnoreCase(ChatColor.GREEN + "[SellTime]")) {
					int price = Integer.parseInt(sign.getLine(1).replaceAll("[\\D]", ""));
					int item = Integer.parseInt(sign.getLine(2).replaceAll("[\\D]", ""));
					int amount = Integer.parseInt(sign.getLine(3).replaceAll("[\\D]", ""));
					ItemStack itemstack = new ItemStack(item, amount);
					if (player.getInventory().containsAtLeast(itemstack, amount)) {
						player.getInventory().removeItem(new ItemStack[] { itemstack });
						player.updateInventory();
						int time = this.plugin.getTime(player);
						int AddTime = time + price;
						this.plugin.setTime(player, AddTime);
						player.sendMessage(this.plugin.prefix + "You bought: " + price + " time for " + amount + " "
								+ itemstack.getType().toString().toLowerCase().replace("_", " "));
						return;
					}
					player.sendMessage(this.plugin.prefix + "You don't have enough "
							+ itemstack.getType().toString().toLowerCase().replace("_", " "));
				}
			}
		}
	}
}
