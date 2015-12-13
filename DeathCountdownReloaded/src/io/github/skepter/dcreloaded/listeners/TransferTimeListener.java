package io.github.skepter.dcreloaded.listeners;

import io.github.skepter.dcreloaded.Main;
import io.github.skepter.dcreloaded.api.DCPlayer;

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
		DCPlayer dcplayer = new DCPlayer(player);
		if (dcplayer.isInBlacklistedWorld())
			return;
		Player target = (Player) event.getRightClicked();
		DCPlayer dctarget = new DCPlayer(target);

		int time = dcplayer.getTime();
		int targettime = dctarget.getTime();
		dcplayer.setTime(time - this.plugin.getConfig().getInt("transferSpeed"));
		dctarget.setTime(targettime + this.plugin.getConfig().getInt("transferSpeed"));
		player.sendMessage(this.plugin.prefix + "You transferred " + this.plugin.getConfig().getInt("transferSpeed") + " time to " + target.getName());
		target.sendMessage(this.plugin.prefix + "You received " + this.plugin.getConfig().getInt("transferSpeed") + " time from " + player.getName());
		return;

	}
}
