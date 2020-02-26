package io.github.mrsperry.worldhandler.portals;

import javafx.util.Pair;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PortalCommands implements CommandExecutor {
    /** A map of all player's selections */
    private final HashMap<Player, Pair<Location, Location>> selections = new HashMap<>();

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String cmdLine, final String[] args) {
        if (command.getName().equalsIgnoreCase("portal")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You must be in a world to use this command");
                return true;
            }

            final Player player = (Player) sender;
            final Pair<Location, Location> locations = this.selections.getOrDefault(player, new Pair<>(null, null));

            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Too few arguments");
                sender.sendMessage(ChatColor.RED + "Usage: /portal <create | pos1 | pos2>");
                return true;
            } else if (args.length == 1) {
                final Location location = player.getLocation();

                if (args[0].equalsIgnoreCase("create")) {
                    player.sendMessage(ChatColor.RED + "Too few arguments");
                    player.sendMessage(ChatColor.RED + "Usage: /portal create <name>");
                    return true;
                }

                if (args[0].equalsIgnoreCase("pos1")) {
                    this.selections.put(player, new Pair<>(location, locations.getValue()));
                } else if (args[0].equalsIgnoreCase("pos2")) {
                    this.selections.put(player, new Pair<>(locations.getKey(), location));
                } else {
                    player.sendMessage(ChatColor.RED + "Invalid argument");
                    player.sendMessage(ChatColor.RED + "Usage: /portal <create | pos1 | pos2>");
                    return true;
                }

                player.sendMessage(ChatColor.GREEN + "Position " + (args[0].equalsIgnoreCase("pos1") ? "1" : "2") + " set to ("
                    + location.getBlockX() + ", "
                    + location.getBlockY() + ", "
                    + location.getBlockZ() + ")");
                return true;
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (locations.getKey() != null && locations.getValue() != null) {
                        PortalHandler.createPortalFile(player, args[1], locations);
                    } else {
                        player.sendMessage(ChatColor.RED + "You must specify two points before creating a portal");
                        player.sendMessage(ChatColor.RED + "Usage: /portal <create | pos1 | pos2>");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Invalid argument");
                    player.sendMessage(ChatColor.RED + "Usage: /portal <create | pos1 | pos2>");
                }
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "Too many arguments");
                player.sendMessage(ChatColor.RED + "Usage: /portal <create | pos1 | pos2>");
                return true;
            }
        }
        return false;
    }
}
