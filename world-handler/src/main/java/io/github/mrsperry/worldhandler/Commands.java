package io.github.mrsperry.worldhandler;

import com.google.common.collect.Lists;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String cmdLine, final String[] args) {
        if (command.getName().equalsIgnoreCase("worlds")) {
            if (args.length == 0) {
                // List command
                this.listWorlds(sender);
                return true;
            } else if (args.length == 1) {
                // List command
                if (args[0].equalsIgnoreCase("list")) {
                    this.listWorlds(sender);
                    return true;
                }

                // Help command
                if (args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(ChatColor.GRAY + "Usage: /worlds [help | list | create | info | settings | load | unload]");
                    return true;
                }

                // Info command
                if (args[0].equalsIgnoreCase("info")) {
                    if (sender instanceof Player) {
                        this.listWorldInfo(sender, ((Player) sender).getWorld().getName());
                    } else {
                        sender.sendMessage(ChatColor.RED + "Too few arguments");
                        sender.sendMessage(ChatColor.RED + "Usage: /worlds settings <world name>");
                    }
                    return true;
                }

                // Settings command
                if (args[0].equalsIgnoreCase("settings")) {
                    if (sender instanceof Player) {
                        this.listWorldSettings(sender, ((Player) sender).getWorld().getName());
                    } else {
                        sender.sendMessage(ChatColor.RED + "Too few arguments");
                        sender.sendMessage(ChatColor.RED + "Usage: /worlds settings <world name>");
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("create")) {
                    sender.sendMessage(ChatColor.RED + "Not enough arguments");
                    sender.sendMessage(ChatColor.RED + "Usage: /worlds create <name> [environment] [generate structures] [seed] [world type] [world generator]");
                    return true;
                } else if (args[0].equalsIgnoreCase("load")) {
                    sender.sendMessage(ChatColor.RED + "Not enough arguments");
                    sender.sendMessage(ChatColor.RED + "Usage: /worlds load <name>");
                    return true;
                } else if (args[0].equalsIgnoreCase("unload")) {
                    sender.sendMessage(ChatColor.RED + "Not enough arguments");
                    sender.sendMessage(ChatColor.RED + "Usage: /worlds unload <name>");
                    return true;
                }
            } else if (args.length == 2) {
                // Settings command
                if (args[0].equalsIgnoreCase("info")) {
                    this.listWorldInfo(sender, args[1]);
                    return true;
                }

                // Settings command
                if (args[0].equalsIgnoreCase("settings")) {
                    this.listWorldSettings(sender, args[1]);
                    return true;
                }

                // Create command
                if (args[0].equalsIgnoreCase("create")) {
                    this.createWorld(sender, args[1]);
                    return true;
                }

                // Load command
                if (args[0].equalsIgnoreCase("load")) {
                    final CustomWorld world = WorldHandler.addWorld(args[1]);
                    if (world == null) {
                        sender.sendMessage(ChatColor.RED + "Could not load world: " + args[1]);
                        return true;
                    }

                    sender.sendMessage(ChatColor.GREEN + "Successfully loaded world: " + args[1]);
                    return true;
                }

                // Unload command
                if (args[0].equalsIgnoreCase("unload")) {
                    if (WorldHandler.removeWorld(args[1])) {
                        sender.sendMessage(ChatColor.GREEN + "Successfully unloaded world: " + args[1]);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Could not find world to unload: " + args[1]);
                    }
                    return true;
                }
            } else if (args.length == 3) {
                // Settings command
                if (args[0].equalsIgnoreCase("settings")) {
                    if (sender instanceof Player) {
                        CustomWorld world = WorldHandler.getWorld(((Player) sender).getWorld().getName());
                        if (world == null) {
                            sender.sendMessage(ChatColor.RED + "This world is not loaded and its settings cannot be changed");
                            return true;
                        }

                        if (args[1].equalsIgnoreCase("gamemode")) {
                            try {
                                world.setDefaultGameMode(GameMode.valueOf(args[2].toUpperCase()));
                                sender.sendMessage(ChatColor.GREEN + "Default game mode set to: " + args[2].toLowerCase());
                            } catch (IllegalArgumentException ex) {
                                sender.sendMessage(ChatColor.RED + "Invalid game mode: " + args[2].toLowerCase());
                            }
                        } else if (args[1].equalsIgnoreCase("difficulty")) {
                            try {
                                world.setDefaultDifficulty(Difficulty.valueOf(args[2].toUpperCase()));
                                sender.sendMessage(ChatColor.GREEN + "Default difficulty set to: " + args[2].toLowerCase());
                            } catch (IllegalArgumentException ex) {
                                sender.sendMessage(ChatColor.RED + "Invalid difficulty: " + args[2].toLowerCase());
                            }
                        } else if (args[1].equalsIgnoreCase("animals")) {
                            world.setCanSpawnAnimals(args[2].equalsIgnoreCase("true"));
                            sender.sendMessage(ChatColor.GREEN + "Animal spawns set to: " + args[2].toLowerCase());
                        } else if (args[1].equalsIgnoreCase("monsters")) {
                            world.setCanSpawnMonsters(args[2].equalsIgnoreCase("true"));
                            sender.sendMessage(ChatColor.GREEN + "Monster spawns set to: " + args[2].toLowerCase());
                        } else if (args[1].equalsIgnoreCase("weather")) {
                            world.setCanChangeWeather(args[2].equalsIgnoreCase("true"));
                            sender.sendMessage(ChatColor.GREEN + "Weather events set to: " + args[2].toLowerCase());
                        } else if (args[1].equalsIgnoreCase("timelock")) {
                            world.setTimeLock(args[2].equalsIgnoreCase("true"));
                            sender.sendMessage(ChatColor.GREEN + "Time lock set to: " + world.getTimeLock());
                        } else {
                            sender.sendMessage(ChatColor.RED + "Invalid argument");
                            sender.sendMessage(ChatColor.RED + "Usage: /worlds settings <gamemode | difficulty | animals | monsters | weather | timelock> [value]");
                        }
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "You must be in a world to change its settings");
                    }
                    return true;
                }

                // Create command
                if (args[0].equalsIgnoreCase("create")) {
                    this.createWorld(sender, args[1], args[2]);
                    return true;
                }
            } else if (args.length < 8) {
                if (args[0].equalsIgnoreCase("create")) {
                    ArrayList<String> values = Lists.newArrayList();

                    for (int index = 1; index < 8; index++) {
                        try {
                            values.add(args[index]);
                        } catch (IndexOutOfBoundsException ex) {
                            values.add(null);
                        }
                    }

                    this.createWorld(sender, values.get(0), values.get(1), values.get(2), values.get(3), values.get(4), values.get(5), values.get(6));
                    return true;
                }
            }
        }

        sender.sendMessage(ChatColor.RED + "Invalid argument");
        sender.sendMessage(ChatColor.RED + "Usage: /worlds [help | list | create | info | settings | load | unload]");
        return true;
    }

    /**
     * Lists out all handled and unhandled worlds
     * @param sender The command sender
     */
    private void listWorlds(final CommandSender sender) {
        final HashSet<String> loadedNames = new HashSet<>();
        final HashSet<CustomWorld> loaded = WorldHandler.getWorlds();
        final List<World> all = Bukkit.getWorlds();

        final StringBuilder builder = new StringBuilder()
            .append(ChatColor.DARK_GRAY).append("========== ").append(ChatColor.AQUA).append("Loaded Worlds").append(ChatColor.DARK_GRAY).append(" ==========\n");

        for (final CustomWorld world : loaded) {
            final String name = world.getWorld().getName();

            builder.append(ChatColor.GREEN).append(name).append(ChatColor.GRAY).append(", ");
            loadedNames.add(name);
        }

        for (final World world : all) {
            final String name = world.getName();

            if (!loadedNames.contains(name)) {
                builder.append(ChatColor.RED).append(name).append(ChatColor.GRAY).append(", ");
            }
        }

        // Remove trailing comma
        sender.sendMessage(builder.toString().substring(0, builder.length() - 2));
    }

    /**
     * Lists out world generation information
     * @param sender The command sender
     * @param world The name of the custom world
     */
    private void listWorldInfo(final CommandSender sender, final String world) {
        final CustomWorld customWorld = WorldHandler.getWorld(world);
        if (customWorld == null) {
            sender.sendMessage(ChatColor.RED + "Could not find world: " + world);
        } else {
            sender.sendMessage(customWorld.getInfo());
        }
    }

    /**
     * Lists out world listener events
     * @param sender The command sender
     * @param world The name of the custom world
     */
    private void listWorldSettings(final CommandSender sender, final String world) {
        final CustomWorld customWorld = WorldHandler.getWorld(world);
        if (customWorld == null) {
            sender.sendMessage(ChatColor.RED + "Could not find world: " + world);
        } else {
            sender.sendMessage(customWorld.getSettings());
        }
    }

    /**
     * Generates a new world with the given settings
     * @param sender The command sender
     * @param args The arguments to generate the world with
     */
    private void createWorld(final CommandSender sender, final String... args) {
        sender.sendMessage(ChatColor.GREEN + "Creating world...");

        final CustomWorld world = WorldHandler.createWorld(args[0],
            args[1] == null ? World.Environment.NORMAL : World.Environment.valueOf(args[1].toUpperCase()),
            args[2] == null ? WorldType.NORMAL : WorldType.valueOf(args[2].toUpperCase()),
            args[3] == null || Boolean.parseBoolean(args[3]),
            args[4] == null ? Main.getInstance().getRandom().nextLong() : Long.parseLong(args[4]),
            args[5]);
        if (world == null) {
            sender.sendMessage(ChatColor.RED + "Could not create world as another world already exists with that name");
            return;
        }

        if (sender instanceof Player) {
            ((Player) sender).teleport(world.getWorld().getSpawnLocation());
        }

        sender.sendMessage(ChatColor.GREEN + "Successfully created world: " + args[0]);
    }
}
