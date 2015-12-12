package io.github.skepter.dcreloaded.listeners;

import io.github.skepter.dcreloaded.Main;
import io.github.skepter.dcreloaded.api.DCPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class EnchantListener implements Listener {
	Main plugin;

	public EnchantListener(Main plugin) {
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
		DCPlayer dcplayer = new DCPlayer(player);
		int time = dcplayer.getTime();
		dcplayer.setTime(time - pricetopay);
	}
}
