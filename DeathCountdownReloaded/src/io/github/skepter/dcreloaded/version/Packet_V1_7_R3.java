package io.github.skepter.dcreloaded.version;

import io.github.skepter.dcreloaded.Main;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Packet_V1_7_R3 implements Packet {

	Plugin plugin;

	public Packet_V1_7_R3(Main dc) {
		plugin = dc;
	}

	@Override
	public boolean sendActionBarMessage(Player player, String message) {
		// Not supported
		return false;
	}
}
