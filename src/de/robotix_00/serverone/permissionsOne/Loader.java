package de.robotix_00.serverone.permissionsOne;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.robotix_00.serverone.permissionsOne.commands.POnecommand;
import de.robotix_00.serverone.permissionsOne.listeners.*;

public class Loader {
	public static void load(JavaPlugin plugin) {
		loadCommands(plugin);
		loadEventHander(plugin);
	}
	
	
	private static void loadCommands(JavaPlugin plugin) {
		plugin.getCommand("pone").setExecutor(new POnecommand());
	}
	private static void loadEventHander(JavaPlugin plugin) {
		PluginManager pluginManager = plugin.getServer().getPluginManager();
		
		pluginManager.registerEvents(new PermissionListener(), plugin);
		pluginManager.registerEvents(new ChatListener(), plugin);
	}
}
