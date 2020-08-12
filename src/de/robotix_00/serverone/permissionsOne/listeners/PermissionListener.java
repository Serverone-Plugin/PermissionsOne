package de.robotix_00.serverone.permissionsOne.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;

import de.robotix_00.serverone.permissionsOne.PermissionController;
import de.robotix_00.serverone.permissionsOne.PermissionsOne;

public class PermissionListener implements Listener {
    static PermissionsOne plugin = PermissionsOne.getPlugin();
    static PermissionController controller = PermissionsOne.getPermissionController();

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {
	controller.setupPlayer(event.getPlayer());
    }

    @EventHandler
    public static void onPlayerQuit(PlayerQuitEvent event) {
	controller.playerLeave(event.getPlayer());
    }

    @EventHandler
    public static void onChangeWorld(PlayerChangedWorldEvent event) {
	controller.changeWorld(event.getPlayer(), event.getFrom().getName(), event.getPlayer().getWorld().getName());
    }

    @EventHandler
    public static void onStart(ServerLoadEvent event) {
	for (Player player : Bukkit.getOnlinePlayers()) {
	    controller.setupPlayer(player);
	}
    }

    public static void onDisable() {
	for (Player player : Bukkit.getOnlinePlayers()) {
	    controller.resetPlayer(player);
	}
    }
}
