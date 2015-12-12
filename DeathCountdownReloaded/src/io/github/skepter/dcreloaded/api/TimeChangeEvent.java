package io.github.skepter.dcreloaded.api;

import io.github.skepter.dcreloaded.Main;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class TimeChangeEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	Main plugin;
	private Player player;
	private int oldtime;
	private int newtime;

	public TimeChangeEvent(Main plugin, Player player, int oldtime, int newtime) {
		this.plugin = plugin;
		this.player = player;
		this.oldtime = oldtime;
		this.newtime = newtime;
	}

	public Player getPlayer() {
		return this.player;
	}

	public int oldTime() {
		return this.oldtime;
	}

	public int newTime() {
		return this.newtime;
	}

	public void setTime() {
		new DCPlayer(player).setTime(newtime);
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public boolean isCancelled() {
		return false;
	}

	public void setCancelled(boolean arg0) {
	}
}
