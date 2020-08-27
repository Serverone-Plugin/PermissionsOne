package de.serverone.permissionsOne;

import org.bukkit.plugin.java.JavaPlugin;

import de.serverone.permissionsOne.listeners.PermissionListener;
import de.serverone.source.util.ServerOneConfig;

public class PermissionsOne extends JavaPlugin {
    private static PermissionsOne plugin;
    private static PermissionController controller;

    // onEnable
    public void onEnable() {
	plugin = this;
	ServerOneConfig.loadConfig(this, "plugins/ServerOne/PermissionsOne", "settings.yml");
	
	this.getLogger().info("Permissions-One geladen");
	
	controller = new PermissionController(this);
	Loader.load(this);
    }

    // onDisable
    public void onDisable() {
	PermissionListener.onDisable();
	this.getLogger().info("Permissions-One disabled");
    }

    public static PermissionsOne getPlugin() {
	return plugin;
    }

    public static PermissionController getPermissionController() {
	return controller;
    }
}