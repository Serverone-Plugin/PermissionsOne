package de.serverone.permissionsOne.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import de.serverone.permissionsOne.PermissionController;
import de.serverone.permissionsOne.PermissionsOne;
import de.serverone.source.util.ServerOneConfig;

public class POnecommand implements CommandExecutor, TabCompleter {
    PermissionsOne plugin = PermissionsOne.getPlugin();
    PermissionController controller = PermissionsOne.getPermissionController();

    ServerOneConfig config = ServerOneConfig.getConfig(plugin, "settings.yml");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlable, String[] args) {
	if (!(sender instanceof ConsoleCommandSender) && !sender.hasPermission("permissionsone.pone")) {
	    sender.sendMessage("§cDu hast die Berechtigung nicht!");
	    return true;
	}
	if (args.length == 0) {
	    pass(sender);
	    return true;
	}

	Player player = null;
	UUID uuid;

	switch (args[0].toLowerCase()) {

	/* test */
	case "test":
	    if (args.length < 3) {
		sender.sendMessage("§cVerwendung: /pone permtest [player] [permission]");
		return true;
	    }
	    player = Bukkit.getPlayer(args[1]);
	    if (player == null) {
		sender.sendMessage("§cDer Spieler §e" + args[1] + "§c schein nicht online zu sein.");
		return true;
	    }
	    if (player.hasPermission(args[2])) {
		sender.sendMessage("§aDer Spieler §e" + args[1] + " §ahat die Berechtigung §e" + args[2]);
		return true;
	    } else {
		sender.sendMessage("§aDer Spieler §e" + args[1] + " §ahat die Berechtigung §e" + args[2] + " §cnicht!");
		return true;
	    }
	case "update":
	    if (args.length == 1) {
		controller.update();
		return true;
	    }
	    if (args.length < 2) {
		sender.sendMessage("§cDer Spieler scheint nicht online zu sein.");
		return true;
	    }
	    player = Bukkit.getPlayer(args[1]);
	    if (player == null) {
		sender.sendMessage("§cDer Spieler §e" + args[1] + " §cist nicht online.");
		return true;
	    }
	    controller.update(player);
	    break;
	case "get":
	    if (args.length < 3) {
		sender.sendMessage("§cDu hast zu wenige Argumente angegeben!");
		pass(sender);
		return true;
	    }
	    uuid = null;
	    player = null;
	    try {
		uuid = UUID.fromString(args[1]);
	    } catch (IllegalArgumentException e) {
		player = Bukkit.getPlayer(args[1]);
		if (player == null) {
		    sender.sendMessage("§cDer Spieler ist nicht online. Verwende seine UUID");
		    return true;
		}
	    }

	    if (uuid == null)
		uuid = player.getUniqueId();

	    switch (args[2].toLowerCase()) {
	    case "group":
		sender.sendMessage("§aDer Spieler §e" + Bukkit.getOfflinePlayer(uuid).getName()
			+ " §agehört zu der Gruppe §e" + controller.getPlayersGroup(uuid));
		break;
	    }
	    break;
	case "set":
	    if (args.length < 4) {
		sender.sendMessage("§cDu hast zu wenige Argumente angegeben!");
		pass(sender);
		return true;
	    }
	    uuid = null;
	    player = null;
	    try {
		uuid = UUID.fromString(args[1]);
	    } catch (IllegalArgumentException e) {
		player = Bukkit.getPlayer(args[1]);
		if (player == null) {
		    sender.sendMessage("§cDer Spieler ist nicht online. Verwende seine UUID");
		    return true;
		}
	    }

	    if (uuid == null)
		uuid = player.getUniqueId();

	    switch (args[2].toLowerCase()) {
	    case "group":
		if (args.length < 4) {
		    sender.sendMessage("§cDu hast zu wenige Argumente angegeben!");
		    pass(sender);
		    return true;
		}
		controller.setPlayersGroup(uuid, args[3]);
		sender.sendMessage("§aDer Spieler §e" + Bukkit.getOfflinePlayer(uuid).getName()
			+ " §awurde der Gruppe §e" + args[3] + " §azugewiesen");
		break;
	    }
	    break;
	default:
	    return false;
	}

	return true;
    }

    private void pass(CommandSender sender) {
	sender.sendMessage(new String[] { "§c/permone test [player] [permission]", "§c/permone get [player] group",
		"§c/permone set [player] group [group]", "§c/permone update", "§c/permone update [player]" });
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String commandlable, String[] args) {
	if (!sender.hasPermission("permissionsone.pone"))
	    return null;
	switch (args.length) {
	case 1:
	    return toCompleteList(args[0], "get", "set", "update", "test");
	case 3:
	    switch (args[0].toLowerCase()) {
	    case "set":
	    case "get":
		return toCompleteList(args[2], "group");
	    }
	case 4:
	    switch (args[0].toLowerCase()) {
	    case "set":
		switch (args[2].toLowerCase()) {
		case "group":
		    return toCompleteList(args[3],
			    new ArrayList<String>(config.getConfigurationSection("groups").getKeys(false)));
		}
	    }
	}

	return null;
    }

    private List<String> toCompleteList(String start, String... strings) {
	List<String> list = new ArrayList<>();

	for (String now : strings) {
	    if (now.equalsIgnoreCase(start) || now.toLowerCase().startsWith(start.toLowerCase()))
		list.add(now);
	}
	return list;
    }

    private List<String> toCompleteList(String start, List<String> strings) {
	List<String> list = new ArrayList<>();

	for (String now : strings) {
	    if (now.equalsIgnoreCase(start) || now.toLowerCase().startsWith(start.toLowerCase()))
		list.add(now);
	}
	return list;
    }
}
