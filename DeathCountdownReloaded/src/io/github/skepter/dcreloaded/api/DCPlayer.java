package io.github.skepter.dcreloaded.api;

import io.github.skepter.dcreloaded.Main;

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

	public void stop() {
		Bukkit.getScheduler().cancelTask(getTaskID());
	}
	
	public void start() {
		if (isAdmin()) {
			return;
		}
		/* Begins the countdown */
		int taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
			public void run() {
				int time = getTime();
				
				/* Times the player out if they run out of time */
				if ((time == 0) || (time < 0)) {
					TimeOutEvent event = new TimeOutEvent(instance, player);
					Bukkit.getServer().getPluginManager().callEvent(event);
					return;
				}
				
				/* TimeChange event (Updates the player's time) */
				int TimeLoss = time - instance.getConfig().getInt("amount");
				TimeChangeEvent event = new TimeChangeEvent(instance, player, time, TimeLoss);
				Bukkit.getServer().getPluginManager().callEvent(event);
				
				/* Does some stuff with the xp bar.... should we not just use that packetty thing
				 * which has text above the xpbar instead?? */
				if (instance.getConfig().getBoolean("useXpBar")) {
					for (String str : instance.getConfig().getStringList("blacklistedWorlds")) {
						if (player.getWorld().getName().equals(str)) {
							return;
						}
					}
					if (time > 32767) {
						player.setLevel(32767);
					} else {
						player.setLevel(time);
					}
					return;
				}
			}
		}, 0L, instance.getConfig().getLong("delay"));
		setTaskID(taskID);
	}

	public void create(int startTime) {
		try {
			instance.sqlite.execute("INSERT INTO DeathCountdownData(playername, time, canRevive, isAdmin, taskID, oldXP, bannedFromWorlds) VALUES('"
					+ player.getName() + "', '" + startTime + "', 'false', 'false', '0', '0', '');");
		} catch (SQLException e) {
			Bukkit.getLogger().warning("There was an error creating the player's database file");
		}
	}

	public int getTime() {
		ResultSet result = instance.sqlite.executeQuery("SELECT time FROM DeathCountdownData WHERE playername='" + player.getName() + "';");
		String r = instance.sqlite.resultToString(result, "time");
		return Integer.valueOf(r).intValue();
	}

	public int getXP() {
		ResultSet result = instance.sqlite.executeQuery("SELECT oldXP FROM DeathCountdownData WHERE playername='" + player.getName() + "';");
		String r = instance.sqlite.resultToString(result, "oldXP");
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

	public void setXP(Player player, int xp) {
		try {
			instance.sqlite.execute("UPDATE DeathCountdownData SET oldXP='" + xp + "' WHERE playername='" + player.getName() + "';");
		} catch (SQLException e) {
			Bukkit.getLogger().warning("There was an error updating the xp");
		}
	}

	public boolean check() {
		ResultSet result = instance.sqlite.executeQuery("SELECT playername FROM DeathCountdownData;");
		ArrayList<String> r = instance.sqlite.resultToArray(result, "playername");
		if ((r == null) || (!r.toString().contains(player.getName()))) {
			return false;
		}
		return true;
	}

}
