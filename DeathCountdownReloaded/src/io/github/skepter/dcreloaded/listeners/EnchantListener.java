package io.github.skepter.dcreloaded.listeners;

import io.github.skepter.dcreloaded.DeathCountdown;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class EnchantListener implements Listener {
	DeathCountdown plugin;

	public EnchantListener(DeathCountdown plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEnchant(EnchantItemEvent event) {
		Player player = event.getEnchanter();
		for (String str : this.plugin.getConfig().getStringList("blacklistedWorlds")) {
			if (player.getWorld().getName().equals(str)) {
				return;
			}
		}
		int pricetopay = event.getExpLevelCost();
		pricetopay *= this.plugin.getConfig().getInt("enchantmentMultiplier");
		int time = this.plugin.getTime(player);
		this.plugin.setTime(player, time - pricetopay);
	}
}
