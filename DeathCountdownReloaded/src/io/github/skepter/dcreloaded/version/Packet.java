package io.github.skepter.dcreloaded.version;

import org.bukkit.entity.Player;

public interface Packet {

	public boolean sendActionBarMessage(Player player, String message);
}
