package io.github.mrsperry.time;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Runnable, CommandExecutor {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 0L, 300L);
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public void run() {
        try {
            String server = getConfig().getString("server");
            if (server != null) {
                getConfig().set("server", addTime(server));
            } else {
                getConfig().set("server", "0:0:0");
            }
            for (Player player : getServer().getOnlinePlayers()) {
                String playerTime = getConfig().getString(player.getName());
                if (playerTime != null) {
                    getConfig().set(player.getName(), addTime(playerTime));
                } else {
                    getConfig().set(player.getName(), "0:0:0");
                }
            }
        } catch (Exception ex) {
            getLogger().severe("Could not get or set a time value in the config!");
            ex.printStackTrace();
        }
    }

    private String addTime(String time) {
        int hours = Integer.parseInt(time.split(":")[0]);
        int minutes = Integer.parseInt(time.split(":")[1]);
        int seconds = Integer.parseInt(time.split(":")[2]);
        seconds += 15;
        if (seconds >= 60) {
            seconds -= 60;
            minutes++;
        }
        if (minutes >= 60) {
            minutes -= 60;
            hours++;
        }
        return hours + ":" + minutes + ":" + seconds;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLine, String[] args) {
        if (cmd.getName().equalsIgnoreCase("time")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    sender.sendMessage(format("You have", getConfig().getString(sender.getName())));
                    return true;
                } else {
                    getServer().getConsoleSender().sendMessage(format("The server has", getConfig().getString("server")));
                    return true;
                }
            } else if (args.length == 1) {
                try {
                    Player player = Bukkit.getPlayer(args[0]);
                    sender.sendMessage(format(ChatColor.GREEN + player.getName() + ChatColor.GRAY + " has", getConfig().getString(player.getName())));
                    return true;
                } catch (Exception ex) {
                    if (args[0].equalsIgnoreCase("server")) {
                        sender.sendMessage(format("The server has", getConfig().getString("server")));
                    } else {
                        sender.sendMessage(ChatColor.RED + "Could not find player: " + args[0]);
                    }
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Too many arguments. Usage: /time [player name | server]");
                return true;
            }
        }
        return false;
    }

    private String format(String name, String time) {
        return ChatColor.GRAY + name + " been online for "
                + ChatColor.AQUA + time.split(":")[0] + ChatColor.GRAY + " hour(s) and "
                + ChatColor.AQUA + time.split(":")[1] + ChatColor.GRAY + " minute(s)";
    }
}
