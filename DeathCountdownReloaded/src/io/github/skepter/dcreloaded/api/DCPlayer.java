package io.github.skepter.dcreloaded.api;

import io.github.skepter.dcreloaded.Main;
import io.github.skepter.dcreloaded.reflection.TimePacket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DCPlayer {

	Player player;
	Main instance;

	public DCPlayer(Player player) {
		this.player = player;
		this.instance = Main.getInstance();
	}

	/** Stops a player's countdown */
	public void stop() {
		Bukkit.getScheduler().cancelTask(getTaskID());
	}
	
	/** Starts a player's countdown
	 * <br> Will also time out players if their current time is <= 0
	 * <br> Activates the time change event to update their time */
	public void start() {
		if (isAdmin() || isInBlacklistedWorld()) {
			return;
		}
		/* Begins the countdown */
		int taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
			public void run() {
				int time = getTime();
				
				/* Times the player out if they run out of time */
				if (time <= 0) {
					TimeOutEvent event = new TimeOutEvent(player);
					Bukkit.getServer().getPluginManager().callEvent(event);
					return; //Don't return... stop the task?
				}
				
				/* TimeChange event (Updates the player's time) */
				TimeChangeEvent event = new TimeChangeEvent(instance, player, time, time - instance.getConfig().getInt("amount"));
				Bukkit.getServer().getPluginManager().callEvent(event);
			}
		}, 0L, instance.getConfig().getLong("delay"));
		setTaskID(taskID);
	}
	
	public void addToDatabase(int startTime) {
		String query = "INSERT INTO DeathCountdownData(playername, time, canRevive, isAdmin, taskID) VALUES('" + player.getName() + "', '" + startTime + "', 'false', 'false', '0');";
		System.out.println(query);
		try {
			instance.sqlite.execute(query);
		} catch (SQLException e) {
			Bukkit.getLogger().warning("There was an error creating the player's database file");
		}
	}

	public int getTime() {
		ResultSet result = instance.sqlite.executeQuery("SELECT time FROM DeathCountdownData WHERE playername='" + player.getName() + "';");
		String r = instance.sqlite.resultToString(result, "time");
		return Integer.valueOf(r).intValue();
	}

	public boolean isAdmin() {
		ResultSet result = instance.sqlite.executeQuery("SELECT isAdmin FROM DeathCountdownData WHERE playername='" + player.getName() + "';");
		String r = instance.sqlite.resultToString(result, "isAdmin");
		return Boolean.parseBoolean(r);
	}

	public boolean canRevive() {
		ResultSet result = instance.sqlite.executeQuery("SELECT canRevive FROM DeathCountdownData WHERE playername='" + player.getName() + "';");
		String r = instance.sqlite.resultToString(result, "canRevive");
		return Boolean.parseBoolean(r);
	}

	public void setTime(int time) {
		try {
			instance.sqlite.execute("UPDATE DeathCountdownData SET time='" + time + "' WHERE playername='" + player.getName() + "';");
		} catch (SQLException e) {
			Bukkit.getLogger().warning("There was an error updating the time");
		}
	}

	public void setRevive(boolean canRevive) {
		try {
			instance.sqlite.execute("UPDATE DeathCountdownData SET canRevive='" + canRevive + "' WHERE playername='" + player.getName() + "';");
		} catch (SQLException e) {
			Bukkit.getLogger().warning("There was an error updating the revive status");
		}
	}

	public void setAdmin(boolean isAdmin) {
		try {
			instance.sqlite.execute("UPDATE DeathCountdownData SET isAdmin='" + isAdmin + "' WHERE playername='" + player.getName() + "';");
		} catch (SQLException e) {
			Bukkit.getLogger().warning("There was an error updating the admin status");
		}
	}

	public int getTaskID() {
		ResultSet result = instance.sqlite.executeQuery("SELECT taskID FROM DeathCountdownData WHERE playername='" + player.getName() + "';");
		String r = instance.sqlite.resultToString(result, "taskID");
		return Integer.parseInt(r);
	}

	public void setTaskID(int taskID) {
		try {
			instance.sqlite.execute("UPDATE DeathCountdownData SET taskID='" + taskID + "' WHERE playername='" + player.getName() + "';");
		} catch (SQLException e) {
			Bukkit.getLogger().warning("There was an error updating the taskID");
		}
	}

	public boolean isInDatabase() {
		ResultSet result = instance.sqlite.executeQuery("SELECT playername FROM DeathCountdownData;");
		ArrayList<String> r = instance.sqlite.resultToArray(result, "playername");
		if ((r == null) || (!r.toString().contains(player.getName()))) {
			return false;
		}
		return true;
	}
	
	public boolean isInBlacklistedWorld() {
		for (String str : instance.getConfig().getStringList("blacklistedWorlds")) {
			if (player.getWorld().getName().equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	/** Sends the player how much time they have using Reflections */
	public void displayTime() {
		new TimePacket(player, getTime()).send();
	}

}
