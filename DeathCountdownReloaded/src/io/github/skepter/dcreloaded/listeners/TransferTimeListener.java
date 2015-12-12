package io.github.skepter.dcreloaded.listeners;

import io.github.skepter.dcreloaded.Main;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class TransferTimeListener implements Listener {
	Main plugin;

	public TransferTimeListener(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onTransfer(PlayerInteractEntityEvent event) {
		if (!(event.getRightClicked() instanceof Player)) {
			return;
		}
		Player player = event.getPlayer();
		for (String str : this.plugin.getConfig().getStringList("blacklistedWorlds")) {
			if (player.getWorld().getName().equals(str)) {
				return;
			}
		}
		Player target = (Player) event.getRightClicked();
		if ((player.getItemInHand().getEnchantmentLevel(Enchantment.SILK_TOUCH) == 2)
				&& (player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("Time Transfer Device"))) {
			int time = this.plugin.getTime(player);
			int targettime = this.plugin.getTime(target);
			this.plugin.setTime(player, time - this.plugin.getConfig().getInt("transferSpeed"));
			this.plugin.setTime(target, targettime + this.plugin.getConfig().getInt("transferSpeed"));
			player.sendMessage(this.plugin.prefix + "You transferred "
					+ this.plugin.getConfig().getInt("transferSpeed") + " time to " + target.getName());
			target.sendMessage(this.plugin.prefix + "You received " + this.plugin.getConfig().getInt("transferSpeed")
					+ " time from " + player.getName());
			return;
		}
	}
}
