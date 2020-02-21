package io.github.mrsperry.deathtracker;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener, CommandExecutor {
    private static Main instance;

    private HashMap<UUID, ArrayList<DeathContext>> deaths = new HashMap<>();

    @Override
    public void onEnable() {
        Main.instance = this;

        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(this, this);

        FileConfiguration config = this.getConfig();
        ConfigurationSection section = config.getConfigurationSection("deaths");
        if (section == null) {
            return;
        }

        for (final String id : section.getKeys(false)) {
            final UUID uuid = UUID.fromString(id);
            final ArrayList<DeathContext> list = Lists.newArrayList();

            if (config.isList("deaths." + id)) {
                for (final String entry : config.getStringList("deaths." + id)) {
                    final DeathContext context = DeathContext.deserialize(entry);
                    if (context != null) {
                        list.add(context);
                    }
                }
            }

            this.deaths.put(uuid, list);
        }
    }

    @Override
    public void onDisable() {
        FileConfiguration config = this.getConfig();

        config.set("deaths", this.deaths.keySet());

        for (final UUID id : this.deaths.keySet()) {
            final ArrayList<DeathContext> deaths = this.deaths.get(id);
            final ArrayList<String> serializedDeaths = Lists.newArrayList();

            for (final DeathContext context : deaths) {
                serializedDeaths.add(context.serialize());
            }

            config.set("deaths." + id, serializedDeaths);
        }

        this.saveConfig();
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID id = player.getUniqueId();
        ArrayList<DeathContext> list = this.getDeathList(id);

        String message = event.getDeathMessage();
        if (message != null) {
            list.add(new DeathContext(player.getLocation(), message.substring(message.split(" ")[0].length() + 1)));
            this.deaths.put(id, list);
        }
    }

    private ArrayList<DeathContext> getDeathList(final UUID id) {
        ArrayList<DeathContext> list = Lists.newArrayList();

        if (this.deaths.containsKey(id)) {
            list = this.deaths.get(id);
        }

        return list;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String cmdLine, final String[] args) {
        if (command.getName().equalsIgnoreCase("deathlist")) {
            Player player;
            int page = 0;

            if (args.length == 0) {
                if (sender instanceof Player) {
                    sender.sendMessage(this.deathListToString(((Player) sender).getUniqueId(), 0));
                } else {
                    sender.sendMessage(ChatColor.RED + "Not enough arguments");
                    sender.sendMessage(ChatColor.RED + "Usage: /deathlist <player> [page]");
                }
                return true;
            } else if (args.length == 1) {
                try {
                    page = Integer.parseInt(args[0]) - 1;

                    if (sender instanceof Player) {
                        player = (Player) sender;
                    } else {
                        sender.sendMessage(ChatColor.RED + "Not enough arguments");
                        sender.sendMessage(ChatColor.RED + "Usage: /deathlist <player> [page]");
                        return true;
                    }
                } catch (NumberFormatException ex) {
                    player = Bukkit.getPlayer(args[0]);

                    if (player == null) {
                        sender.sendMessage(ChatColor.RED + "Player '" + args[0] + "' is not online or could not be found");
                        return true;
                    }
                }

                sender.sendMessage(this.deathListToString(player.getUniqueId(), page));
                return true;
            } else if (args.length == 2) {
                player = Bukkit.getPlayer(args[0]);
                try {
                    page = Integer.parseInt(args[1]) - 1;
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Invalid argument: " + args[1]);
                    sender.sendMessage(ChatColor.RED + "Usage: /deathlist <player> [page]");
                    return true;
                }

                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "Player '" + args[0] + "' is not online or could not be found");
                    return true;
                }

                sender.sendMessage(this.deathListToString(player.getUniqueId(), page));
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Too many arguments");
                sender.sendMessage(ChatColor.RED + "Usage: /deathlist [player | page]");
                return true;
            }
        }
        return false;
    }

    private String deathListToString(final UUID id, final int page) {
        final ArrayList<DeathContext> deaths = this.getDeathList(id);
        if (deaths.size() == 0) {
            return ChatColor.GREEN + "No deaths recorded!";
        }

        int totalPages = (int) Math.ceil(deaths.size() / 5D);
        int startIndex = page * 5;

        if (startIndex > deaths.size() || startIndex < 0) {
            return ChatColor.RED + "Invalid page number.";
        }

        StringBuilder format = new StringBuilder(ChatColor.DARK_GRAY + "========== "
            + ChatColor.RED + "Deaths"
            + ChatColor.GRAY + " (" + (page + 1) + "/" + totalPages + ")"
            + ChatColor.DARK_GRAY + " ==========\n");

        for (int index = 0; index < 5; index++) {
            try {
                format.append(deaths.get(startIndex + index).toString());
                if (index != 4) {
                    format.append("\n");
                }
            } catch (IndexOutOfBoundsException ex) {
                format.delete(format.length() - 1, format.length());
                break;
            }
        }

        return format.toString();
    }

    static Main getInstance() {
        return Main.instance;
    }
}
