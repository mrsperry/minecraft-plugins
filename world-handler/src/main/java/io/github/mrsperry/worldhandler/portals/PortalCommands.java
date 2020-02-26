package io.github.mrsperry.worldhandler.portals;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PortalCommands implements CommandExecutor {
    /** A map of all player's portal schematics */
    private final HashMap<Player, PortalSchematic> selections = new HashMap<>();

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String cmdLine, final String[] args) {
        if (command.getName().equalsIgnoreCase("portal")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "You must be in a world to use this command");
                return true;
            }

            final Player player = (Player) sender;
            final PortalSchematic schematic = this.selections.getOrDefault(player, new PortalSchematic());

            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Too few arguments");
                sender.sendMessage(ChatColor.RED + "Usage: /portal <create | set>");
                return true;
            } else if (args.length == 1) {
                // Create command
                if (args[0].equalsIgnoreCase("create")) {
                    player.sendMessage(ChatColor.RED + "Too few arguments");
                    player.sendMessage(ChatColor.RED + "Usage: /portal create <name>");
                    return true;
                }

                // Set command
                if (args[0].equalsIgnoreCase("set")) {
                    player.sendMessage(ChatColor.RED + "Too few arguments");
                    player.sendMessage(ChatColor.RED + "Usage: /portal set <activator | destination | pos1 | pos2>");
                    return true;
                }

                player.sendMessage(ChatColor.RED + "Invalid argument");
                player.sendMessage(ChatColor.RED + "Usage: /portal <create | set>");
                return true;
            } else if (args.length == 2) {
                // Create command
                if (args[0].equalsIgnoreCase("create")) {
                    if (schematic.getActivator() == null) {
                        player.sendMessage(ChatColor.RED + "You must set an activator location before creating a portal");
                        return true;
                    }

                    if (schematic.getDestination() == null) {
                        player.sendMessage(ChatColor.RED + "You must set a destination location before creating a portal");
                        return true;
                    }

                    if (schematic.getPos1() == null || schematic.getPos2() == null) {
                        player.sendMessage(ChatColor.RED + "You must set both position locations before creating a portal");
                        return true;
                    }

                    schematic.setName(args[1]).createPortalFile(player);
                    return true;
                }

                // Set command
                if (args[0].equalsIgnoreCase("set")) {
                    final Location location = player.getLocation();
                    final String locationString = "(" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ")";

                    if (args[1].equalsIgnoreCase("activator")) {
                        this.selections.put(player, schematic.setActivator(location));
                        player.sendMessage(ChatColor.GREEN + "Activator position set to " + locationString);
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("destination")) {
                        this.selections.put(player, schematic.setDestination(location));
                        player.sendMessage(ChatColor.GREEN + "Destination position set to " + locationString);
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("pos1")) {
                        this.selections.put(player, schematic.setPos1(location));
                        player.sendMessage(ChatColor.GREEN + "Position 1 set to " + locationString);
                    } else if (args[1].equalsIgnoreCase("pos2")) {
                        this.selections.put(player, schematic.setPos2(location));
                        player.sendMessage(ChatColor.GREEN + "Position 2 set to " + locationString);
                    } else {
                        player.sendMessage(ChatColor.RED + "Invalid argument");
                        player.sendMessage(ChatColor.RED + "Usage: /portal set <activator | destination | pos1 | pos2>");
                        return true;
                    }
                    return true;
                }

                return true;
            } else {
                player.sendMessage(ChatColor.RED + "Too many arguments");
                player.sendMessage(ChatColor.RED + "Usage: /portal <create | set>");
                return true;
            }
        }
        return false;
    }
}
