package io.github.skepter.dcreloaded;

import io.github.skepter.dcreloaded.api.TimeChangeEvent;
import io.github.skepter.dcreloaded.api.TimeOutEvent;
import io.github.skepter.dcreloaded.cmds.DCTopCommand;
import io.github.skepter.dcreloaded.cmds.DeathCountdownCommand;
import io.github.skepter.dcreloaded.cmds.TimeCommand;
import io.github.skepter.dcreloaded.listeners.ChatListener;
import io.github.skepter.dcreloaded.listeners.EnchantListener;
import io.github.skepter.dcreloaded.listeners.LoginManager;
import io.github.skepter.dcreloaded.listeners.PlayerManager;
import io.github.skepter.dcreloaded.listeners.SignListener;
import io.github.skepter.dcreloaded.listeners.SignUseListener;
import io.github.skepter.dcreloaded.listeners.TransferTimeListener;
import io.github.skepter.dcreloaded.listeners.VotifierListener;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	public String command = "DeathCountdown.Command";
	public String sign = "DeathCountdown.Sign";
	public Logger log;
	public SQLite sqlite;
	PluginDescriptionFile description = getDescription();
	public String prefix = "�a[DeathCountdown]�7 ";

	public Main getPlugin() {
		return this;
	}

	public void onEnable() {
		this.log = Bukkit.getLogger();
		//updateAndMetrics();
		this.log.info("[DeathCountdown] Connecting to database...");
		File file = new File(getDataFolder(), "deathcountdown.db");
		this.sqlite = new SQLite(file);
		try {
			this.sqlite.open();
		} catch (SQLException e1) {
			log.info("Could not access database, shutting down");
			//Shut down plugin
		}
		try {
			this.sqlite
					.execute("CREATE TABLE IF NOT EXISTS DeathCountdownData (playername VARCHAR(16), time INTEGER(15), canRevive BOOLEAN, isAdmin BOOLEAN, taskID INTEGER(3), oldXP INTEGER(15));");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		this.log.info("[DeathCountdown] Database connected!");
		getServer().getPluginManager().registerEvents(new LoginManager(this), this);
		getServer().getPluginManager().registerEvents(new PlayerManager(this), this);
		getServer().getPluginManager().registerEvents(new SignListener(this), this);
		getServer().getPluginManager().registerEvents(new SignUseListener(this), this);
		getServer().getPluginManager().registerEvents(new EnchantListener(this), this);
		getServer().getPluginManager().registerEvents(new ChatListener(this), this);
		getServer().getPluginManager().registerEvents(new TransferTimeListener(this), this);
		if (getConfig().getBoolean("votifierSupport")) {
			getServer().getPluginManager().registerEvents(new VotifierListener(this), this);
		}
		getCommand("dc").setExecutor(new DeathCountdownCommand(this));
		getCommand("dctime").setExecutor(new TimeCommand(this));
		getCommand("dctop").setExecutor(new DCTopCommand(this));
		saveDefaultConfig();
		registerRecipe();
		restartScheduler();
	}

	public void onDisable() {
		saveConfig();
		try {
			this.sqlite.close();
		} catch (SQLException e) {
			log.info("There was an error closing the database");
		}
		getServer().getScheduler().cancelTasks(this);
	}
	
	public static Main getInstance() {
		return JavaPlugin.getPlugin(Main.class);
	}

	/*
	private void updateAndMetrics() {
		if (getConfig().getBoolean("updateCheck")) {
			Updater updater = new Updater(this, 67779, getFile(), Updater.UpdateType.NO_DOWNLOAD, true);
			if (updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE) {
				getLogger().info("New version available! " + updater.getLatestName());
			}
		}
		if (getConfig().getBoolean("sendMetrics")) {
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
				this.log.info("[DeathCountdown] Successfully hooked into Metrics");
				this.log.info("[DeathCountdown] Metrics sends small statistics about the server, but if you wish to opt out, you can adjust the settings in the /PluginMetrics/ folder");
			} catch (IOException e) {
				this.log.warning("[DeathCountdown] Unable to submit Metrics statistics");
			}
		}
	}*/

	public void restartScheduler() {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			start(this, player);
		}
	}

	private void registerRecipe() {
		ItemStack item = new ItemStack(Material.WATCH, 1);
		item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 2);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("Time Transfer Device");
		String[] itemlore = { getConfig().getString("clockLore") };

		im.setLore(Arrays.asList(itemlore));
		item.setItemMeta(im);
		ShapedRecipe recipeitem = new ShapedRecipe(item);
		recipeitem.shape(new String[] { " G ", "GRG", " G " });

		recipeitem.setIngredient('G', Material.GOLD_INGOT);
		recipeitem.setIngredient('R', Material.REDSTONE_BLOCK);
		getServer().addRecipe(recipeitem);
	}

	

	public void unban(OfflinePlayer player) {
		this.sqlite.execute("DELETE FROM DeathCountdownData WHERE playername='" + player.getName() + "';");
	}

	@Deprecated
	protected void addFakeData(int amount) {
		SecureRandom r = new SecureRandom();
		for (int i = 0; i < amount; i++) {
			Random random = new Random();
			int time = random.nextInt(5000) + 0;
			String name = new BigInteger(16, r).toString(32);
			this.sqlite.execute("INSERT INTO DeathCountdownData(playername, time) VALUES('" + name + "', '" + time
					+ "');");
		}
	}

	@Deprecated
	protected void addFakeDataHR(int amount) {
		for (int i = 1; i < amount + 1; i++) {
			String name = "Player" + i;
			this.sqlite
					.execute("INSERT INTO DeathCountdownData(playername, time) VALUES('" + name + "', '" + i + "');");
		}
	}

	public void addBannedWorld(Player player, String worldname) {
		String currentbans = null;
		try {
			currentbans = getBannedWorlds(player);
		} catch (Exception e1) {
			currentbans = "";
		}
		String newWorlds = currentbans + worldname + "-";

		this.sqlite.execute("UPDATE DeathCountdownData SET bannedFromWorlds='" + newWorlds + "' WHERE playername='"
				+ player.getName() + "';");
	}

	public String getBannedWorlds(Player player) {
		ResultSet result = this.sqlite
				.executeQuery("SELECT bannedFromWorlds FROM DeathCountdownData WHERE playername='" + player.getName()
						+ "';");
		String r;
		try {
			r = this.sqlite.resultToString(result, "bannedFromWorlds");
		} catch (Exception e1) {
			r = "";
		}
		return r;
	}

	public void removeBannedWorld(Player player, String worldname) {
		String worlds = getBannedWorlds(player);
		String[] w = worlds.split("-");
		String[] arrayOfString1;
		int j = (arrayOfString1 = w).length;
		for (int i = 0; i < j; i++) {
			String s = arrayOfString1[i];
			if (s.equalsIgnoreCase("worldname")) {
				String s1 = worlds.replaceAll("s", "");
				String s2 = s1.replaceAll("--", "-");
				this.sqlite.execute("UPDATE DeathCountdownData SET bannedFromWorlds='" + s2 + "' WHERE playername='"
						+ player.getName() + "';");
				return;
			}
		}
	}

	public void clearBannedWorlds(Player player) {
		this.sqlite.execute("UPDATE DeathCountdownData SET bannedFromWorlds='' WHERE playername='" + player.getName()
				+ "';");
	}

	@Deprecated
	public int getRowAmount() {
		ResultSet result = this.sqlite.executeQuery("SELECT COUNT(*) AS RowCount FROM DeathCountdownData;");
		String r = this.sqlite.resultToString(result, "RowCount");
		return Integer.parseInt(r);
	}

	public ArrayList<String> getTopTime() {
		ResultSet result = this.sqlite.executeQuery("SELECT time FROM DeathCountdownData ORDER BY time DESC;");
		ArrayList<String> r = resultToArray(result, "time");
		return r;
	}

	public ArrayList<String> resultToArray(ResultSet result, String data) {
		ArrayList<String> arr = new ArrayList<String>();
		try {
			while (result.next()) {
				arr.add(result.getString(data));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return arr;
	}

	public ArrayList<String> getTopPlayers() {
		ResultSet result = this.sqlite.executeQuery("SELECT playername FROM DeathCountdownData ORDER BY time DESC;");
		ArrayList<String> r = resultToArray(result, "playername");
		return r;
	}

	public boolean checkInventoryForTTDevice(Player player) {
		if (!player.getInventory().contains(TTDevice())) {
			return false;
		}
		return true;
	}

	public ItemStack TTDevice() {
		ItemStack item = new ItemStack(Material.WATCH, 1);
		item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 2);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("Time Transfer Device");
		String[] itemlore = { getConfig().getString("clockLore") };

		im.setLore(Arrays.asList(itemlore));
		item.setItemMeta(im);
		return item;
	}
}
