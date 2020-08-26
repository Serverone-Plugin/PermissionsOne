package de.serverone.permissionsOne;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import de.robotix_00.serverone.source.util.ServerOneConfig;

public class PermissionController {
    private HashMap<Player, PermissionAttachment> perms = new HashMap<>();

    protected PermissionController(PermissionsOne plugin) {
	this.plugin = plugin;
    }

    PermissionsOne plugin;
    ServerOneConfig config = ServerOneConfig.getConfig(PermissionsOne.getPlugin(), "settings.yml");;

    public void update() {
	for (Player player : Bukkit.getOnlinePlayers()) {
	    this.resetPlayer(player);
	    this.setupPlayer(player);
	    perms.get(player).getPermissible().recalculatePermissions();
	}
    }

    public void update(Player player) {
	this.resetPlayer(player);
	this.setupPlayer(player);
    }

    public void playerLeave(Player player) {
	PermissionAttachment pa = perms.get(player);
	if (pa == null)
	    return;
	player.removeAttachment(pa);
	perms.remove(player);
    }

    /* Doubles */
    public void setPlayersGroup(Player player, String group) {
	setPlayersGroup(player.getUniqueId(), group);
    }

    public String getPlayersGroup(Player player) {
	return getPlayersGroup(player.getUniqueId());
    }

    /* Playersettings */
    public String getPlayersGroup(UUID player) {
	String group = config.getString("users." + player + ".group");

	if (group == null)
	    return "default";
	else
	    return group;
    }

    public void setPlayersGroup(UUID uuid, String group) {
	// if player is online
	if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
	    Player player = Bukkit.getPlayer(uuid);
	    config.set("users." + uuid + ".group", group);
	    config.save();
	    this.update(player);
	    // if player is offline
	} else {
	    config.set("users." + uuid + ".group", group);
	    config.save();
	}
    }

    /* Permissiongetter */
    public List<String> getPermissions(Player player, World world) {
	List<String> permissions = new ArrayList<>();

	List<String> groups = new ArrayList<>();
	groups.add(getPlayersGroup(player));
	for (int i = 0; i < 10; i++) {
	    String tmp = config.getString("groups." + groups.get(i) + ".parent");
	    if (tmp == null || groups.contains(tmp))
		break;
	    else
		groups.add(tmp);
	}
	Collections.reverse(groups);

	for (String nowGroup : groups) {
	    permissions.addAll(config.getList("groups." + nowGroup + ".permissions"));
	    permissions.addAll(config.getList("groups." + nowGroup + ".worlds." + world.getName()));
	    Collections.reverse(permissions);
	}
	permissions.addAll(config.getList("users." + player.getUniqueId() + ".permissions"));

	Collections.reverse(permissions);
	return permissions;
    }

    public List<String> getWorldPermissions(String group, String world) {
	List<String> permissions = new ArrayList<>();

	List<String> groups = new ArrayList<>();
	groups.add(group);
	for (int i = 0; i < 10; i++) {
	    String tmp = config.getString("groups." + groups.get(i) + ".parent");
	    if (tmp == null || groups.contains(tmp))
		break;
	    else
		groups.add(tmp);
	}
	Collections.reverse(groups);

	for (String nowGroup : groups) {
	    permissions.addAll(config.getList("groups." + nowGroup + ".worlds." + world + ".permissions"));
	}

	Collections.reverse(permissions);
	return permissions;
    }

    public void setupPlayer(Player player) {
	if (perms.get(player) == null)
	    perms.put(player, player.addAttachment(plugin));
	
	//adding permissions
	for (String permission : getPermissions(player, player.getWorld())) {
	    if (permission.startsWith("~")) {
		permission = permission.replaceFirst("~", "");
		perms.get(player).setPermission(permission, false);
	    } else {
		perms.get(player).setPermission(permission, true);
	    }
	}
	
	//adding tablist-colors
	player.setPlayerListName(getTabColour(player) + player.getName());
    }

    public void resetPlayer(Player player) {
	PermissionAttachment pa = perms.get(player);
	if (pa == null)
	    return;
	player.removeAttachment(pa);
	perms.remove(player);
    }

    public void changeWorld(Player player, String oldWorld, String newWorld) {
	resetPlayer(player);
	setupPlayer(player);
    }

    public String getChatSetup(Player player) {
	String prefix;
	prefix = config.getString("users." + player.getUniqueId() + ".options.chatsetup");
	if (prefix != null)
	    return prefix;

	prefix = config.getString("groups." + getPlayersGroup(player) + ".chatsetup");
	if (prefix != null)
	    return prefix;
	else
	    return "<<p>>";
    }
    public String getTabColour(Player player) {
	String prefix;
	prefix = config.getString("users." + player.getUniqueId() + ".options.listcolour");
	if (prefix != null)
	    return prefix;

	prefix = config.getString("groups." + getPlayersGroup(player) + ".listcolour");
	if (prefix != null)
	    return prefix;
	else
	    return "";
    }
}
