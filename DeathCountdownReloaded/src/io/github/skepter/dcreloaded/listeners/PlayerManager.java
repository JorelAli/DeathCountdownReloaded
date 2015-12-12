package io.github.skepter.dcreloaded.listeners;

import io.github.skepter.dcreloaded.Main;

import java.util.Iterator;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class PlayerManager implements Listener {
	Main plugin;

	public PlayerManager(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		int time;
		if ((event.getEntity().getKiller() instanceof Player)) {
			Player killer = event.getEntity().getKiller();
			for (String str : this.plugin.getConfig().getStringList("blacklistedWorlds")) {
				if (killer.getWorld().getName().equals(str)) {
					return;
				}
			}
			Player player = event.getEntity();
			time = this.plugin.getTime(player);
			int TimeLoss = time - this.plugin.getConfig().getInt("timeLost");
			this.plugin.setTime(player, TimeLoss);

			int killertime = this.plugin.getTime(event.getEntity().getKiller());
			int TimeGain = killertime + this.plugin.getConfig().getInt("timeLost");
			this.plugin.setTime(killer, TimeGain);
		} else {
			Player player = event.getEntity();
			for (String str : this.plugin.getConfig().getStringList("blacklistedWorlds")) {
				if (player.getWorld().getName().equals(str)) {
					return;
				}
			}
			time = this.plugin.getTime(player);
			int TimeLoss = time - this.plugin.getConfig().getInt("timeLost");
			this.plugin.setTime(player, TimeLoss);
		}
	}

	@EventHandler
	public void onMobDeath(EntityDeathEvent event) {
		if (((event.getEntity() instanceof Monster)) && ((event.getEntity().getKiller() instanceof Player))) {
			Player player = event.getEntity().getKiller();
			for (String str : this.plugin.getConfig().getStringList("blacklistedWorlds")) {
				if (player.getWorld().getName().equals(str)) {
					return;
				}
			}
			int time = this.plugin.getTime(player);
			this.plugin.setTime(player, time + this.plugin.getConfig().getInt("mobReward"));
			return;
		}
	}

	@EventHandler
	public void onXpChange(PlayerExpChangeEvent event) {
		Player player = event.getPlayer();
		for (String str : this.plugin.getConfig().getStringList("blacklistedWorlds")) {
			if (player.getWorld().getName().equals(str)) {
				return;
			}
		}
		if (this.plugin.getConfig().getBoolean("useXpBar")) {
			event.setAmount(0);
			return;
		}
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		//Ugh, an iterator? Why not a for loop for crying out loud!
		Iterator<String> localIterator = this.plugin.getConfig().getStringList("blacklistedWorlds").iterator();
		if (localIterator.hasNext()) {
			String str = (String) localIterator.next();
			if (player.getWorld().getName().equals(str)) {
				this.plugin.cancelTask(player);
				if (this.plugin.getConfig().getBoolean("useXpBar")) {
					player.setExp(this.plugin.getXP(player));
				}
				player.sendMessage(this.plugin.prefix
						+ "You are in a blacklisted world. You will not lose time in here.");
				return;
			}
			this.plugin.start(this.plugin, player);
			if (this.plugin.getConfig().getBoolean("useXpBar")) {
				this.plugin.setXP(player, (int) player.getExp());
			}
			return;
		}
	}
}
