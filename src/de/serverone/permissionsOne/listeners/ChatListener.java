package de.serverone.permissionsOne.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.serverone.permissionsOne.PermissionsOne;

public class ChatListener implements Listener {
    @EventHandler
    public void onMessageSend(AsyncPlayerChatEvent event) {
	PermissionsOne.getPlugin();
	String prefix = PermissionsOne.getPermissionController().getChatSetup(event.getPlayer()).replaceFirst("<p>",
		event.getPlayer().getName());

	if (prefix != null)
	    event.setFormat(prefix + " " + event.getMessage());
    }
}
