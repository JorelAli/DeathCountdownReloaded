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
/*******************************************************************************
 *******************************************************************************/
package io.github.skepter.dcreloaded.reflection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/** AllAssets' ReflectionUtils */
public class MinecraftReflectionUtils {

	/* Main objects */
	final public Player player;
	final public Object nmsPlayer;
	final public Object getConnection;
	final public Object craftServer;

	/* Misc & other objects */
	final private String packageName;
	final private String obcPackageName;
	final public Object dedicatedServer;

	/* Classes */
	final public Class<?> iChatBaseComponentClass;
	final public Class<?> packetClass;

	/* Object classes (Packets) */
	final public Object emptyChatSerializer;

	final public Object emptyPacketPlayOutChat;
	
	/** Creates a new instance of ReflectionUtils and prepares the classes and
	 * stuff */
	public MinecraftReflectionUtils(final Player player) throws Exception {
		/* Load player classes, player connection, server and world */
		this.player = player;
		nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
		getConnection = ReflectionUtils.getFieldValue(nmsPlayer, "playerConnection");

		obcPackageName = Bukkit.getServer().getClass().getPackage().getName();
		craftServer = getOBCClass("CraftServer").cast(Bukkit.getServer());

		/* Get the server, world server and the package name for reflection.
		 * The package name is retrieved dynamically from the server instead
		 * of using the default package name and then parsing the version number.
		 * It seems easier this way. */
		dedicatedServer = ReflectionUtils.getPrivateFieldValue(craftServer, "console");
		packageName = dedicatedServer.getClass().getPackage().getName();

		/* Create the class instances */
		packetClass = getNMSClass("Packet");
		iChatBaseComponentClass = getNMSClass("IChatBaseComponent");

		/* Create the class instances */
		emptyChatSerializer = getNMSClass("ChatSerializer").newInstance();
		emptyPacketPlayOutChat = getNMSClass("PacketPlayOutChat").newInstance();

	}

	/** Serialises a String (JSON stuff) */
	public Object chatSerialize(final String string) throws Exception {
		return emptyChatSerializer.getClass().getMethod("a", String.class).invoke(emptyChatSerializer, string);
	}
	
	/** Retrieves a net.minecraft.server class by using the dynamic package from
	 * the dedicated server */
	public Class<?> getNMSClass(final String className) throws ClassNotFoundException {
		return (Class.forName(packageName + "." + className));
	}

	/** Retrieves a net.minecraft.server class by using the dynamic package from
	 * the dedicated server */
	public Class<?> getOBCClass(final String className) throws ClassNotFoundException {
		return (Class.forName(obcPackageName + "." + className));
	}

	/** Sends an outgoing packet (From server to client) */
	public void sendOutgoingPacket(final Object packet) throws Exception {
		getConnection.getClass().getMethod("sendPacket", packetClass).invoke(getConnection, packet);
	}
}
