package io.github.skepter.dcreloaded.listeners;

import io.github.skepter.dcreloaded.Main;
import io.github.skepter.dcreloaded.api.DCPlayer;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;

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
			DCPlayer dckiller = new DCPlayer(killer);

			if (dckiller.isInBlacklistedWorld())
				return;

			Player player = event.getEntity();
			DCPlayer dcplayer = new DCPlayer(player);
			time = dcplayer.getTime();
			int timeLoss = time - this.plugin.getConfig().getInt("timeLost");
			dcplayer.setTime(timeLoss);

			int killertime = dckiller.getTime();
			int timeGain = killertime + this.plugin.getConfig().getInt("timeLost");
			dckiller.setTime(timeGain);
		} else {
			Player player = event.getEntity();
			DCPlayer dcplayer = new DCPlayer(player);

			if (dcplayer.isInBlacklistedWorld())
				return;
			time = dcplayer.getTime();
			int timeLoss = time - this.plugin.getConfig().getInt("timeLost");
			dcplayer.setTime(timeLoss);
		}
	}

	@EventHandler
	public void onMobDeath(EntityDeathEvent event) {
		if (((event.getEntity() instanceof Monster)) && ((event.getEntity().getKiller() instanceof Player))) {
			DCPlayer dcplayer = new DCPlayer(event.getEntity().getKiller());
			if (dcplayer.isInBlacklistedWorld())
				return;
			dcplayer.setTime(dcplayer.getTime() + this.plugin.getConfig().getInt("mobReward"));
			return;
		}
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		DCPlayer dcplayer = new DCPlayer(player);
		for (String world : this.plugin.getConfig().getStringList("blacklistedWorlds")) {
			if (player.getWorld().getName().equals(world)) {
				dcplayer.stop();
				player.sendMessage(this.plugin.prefix + "You are in a blacklisted world. You will not lose time in here.");
				return;
			}
			return;
		}
		//Hold it.... this would then ... start ..... wait....
		//Waiiiiiiitttt........ put a check in here for if worlds are synchronised time
		dcplayer.start();
//		if (this.plugin.getConfig().getBoolean("useXpBar")) {
//			dcplayer.setXP((int) player.getExp());
//		}
	}
}
