/*******************************************************************************
 * Skepter's Licence
 * Copyright © 2015
 *
 * AllAssets, created by Skepter and MCSpartans
 *
 * You are able to:
 * * View AllAssets' source code on GitHub
 * * Experiment with the code as you wish
 * * Download the .jar files supplied on GitHub for your server
 *
 * You are NOT allowed to:
 * * Sell AllAssets - It is COMPLETELY free for ALL users
 * * Claim it as your own. AllAssets is created by Skepter and MCSpartans
 * * Distribute it on any other website
 * * Decompile the code - It's pointless, time consuming and the source code is already on GitHub
 * * Steal the code from GitHub. Just ask and we're more than likely to let you copy some of it
 *
 * You cannot:
 * * Hold us liable for your actions
 ******************************************************************************/
package io.github.skepter.dcreloaded.reflection;

import org.bukkit.entity.Player;

/** AllAssets' packer builder */
public class TimePacket {

	private MinecraftReflectionUtils utils;
	private Object packet;

	public TimePacket(final Player player, final int time) {
		try {
			this.utils = new MinecraftReflectionUtils(player);
			this.packet = utils.emptyPacketPlayOutChat;

			packet = packet.getClass().getConstructor().newInstance();
			try {
				ReflectionUtils.setPrivateField(packet, "a", utils.chatSerialize(String.valueOf(time)));
				ReflectionUtils.setPrivateField(packet, "b", (byte) 2);
			} catch (final Exception e) {
			}
		} catch (final Exception e) {
		}
	}

	public void send() {
		try {
			utils.sendOutgoingPacket(packet);
		} catch (final Exception e) {
		}
	}
}
