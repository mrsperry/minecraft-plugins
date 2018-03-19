package io.github.mrsperry.rifts.commands;

import io.github.mrsperry.rifts.Main;
import io.github.mrsperry.rifts.RiftManager;
import io.github.mrsperry.rifts.configs.RiftConfig;
import io.github.mrsperry.rifts.Timer;
import io.github.mrsperry.rifts.meta.Rift;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class RiftCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmdLine, String[] args) {
        if (command.getName().equalsIgnoreCase("rifts")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("enabled")) {
                    sender.sendMessage(ChatColor.GREEN + "Enabled: " + (Main.areRiftsEnabled() ? "true" : ChatColor.RED + "false"));
                    return true;
                } else if (args[0].equalsIgnoreCase("spawn")) {
                    if (Bukkit.getOnlinePlayers().size() > 0) {
                        Timer.spawnRandomRift();
                        sender.sendMessage(ChatColor.GREEN + "Spawning a new random rift...");
                        return true;
                    } else {
                        sender.sendMessage(ChatColor.RED + "No players online to spawn rift around!");
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("stop")) {
                    HashSet<Rift> rifts = RiftManager.getActiveRifts();
                    if (rifts.size() == 0) {
                        sender.sendMessage(ChatColor.RED + "No rifts are currently active!");
                        return true;
                    }
                    for (Rift rift : rifts) {
                        rift.end();
                    }
                    sender.sendMessage(ChatColor.GREEN + "Ended all active rifts!");
                    return true;
                } else if (args[0].equalsIgnoreCase("ids")) {
                    String result = ChatColor.GREEN + "Rift IDs: [" + ChatColor.GRAY;
                    for (String id : RiftManager.getRiftConfigIDs()) {
                        result = result.concat(id + ", ");
                    }
                    sender.sendMessage(result.substring(0, result.length() - 2) + ChatColor.GREEN + "]");
                    return true;
                } else {
                    this.displayHelp(sender);
                    return true;
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("enabled")) {
                    try {
                        boolean enabled = Boolean.parseBoolean(args[1]);
                        if (Main.areRiftsEnabled() && enabled) {
                            sender.sendMessage(ChatColor.GREEN + "Rifts are already enabled!");
                            return true;
                        } else if (!Main.areRiftsEnabled() && !enabled) {
                            sender.sendMessage(ChatColor.RED + "Rifts are already disabled!");
                            return true;
                        }
                        Main.setRiftsEnabled(enabled);
                        sender.sendMessage(ChatColor.GREEN + "Rifts are now " + (enabled ? "enabled" : ChatColor.RED + "disabled") + ChatColor.GREEN + "!");
                        return true;
                    } catch (Exception ex) {
                        sender.sendMessage("Argument must be \"true\" or \"false\": " + args[1]);
                        return true;
                    }
                } else {
                    this.displayHelp(sender);
                    return true;
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("spawn")) {
                    Player player;
                    try {
                        player = Bukkit.getPlayer(args[1]);
                        if (!player.isOnline()) {
                            throw new Exception();
                        }
                    } catch (Exception ex) {
                        sender.sendMessage(ChatColor.RED + "Player is not online or does not exist: " + args[1]);
                        return true;
                    }
                    RiftConfig config;
                    if (RiftManager.getRiftConfigIDs().contains(args[2])) {
                        config = RiftManager.getRiftConfig(args[2]);
                    } else {
                        sender.sendMessage(ChatColor.RED + "No rift ID found: " + args[2]);
                        return true;
                    }
                    sender.sendMessage(ChatColor.GREEN + "Attempting to spawn rift around \"" + args[1] + "\" with id: " + args[2]);
                    boolean valid = Timer.spawnRift(player, config);
                    if (valid) {
                        sender.sendMessage(ChatColor.GREEN + "Spawning succeeded!");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Spawning failed! Could not find a valid location to spawn the rift!");
                    }
                    return true;
                } else {
                    this.displayHelp(sender);
                }
            } else {
                this.displayHelp(sender);
                return true;
            }
        }
        return false;
    }

    private void displayHelp(CommandSender sender) {
        String result = ChatColor.RED + "Usage:\n";
        result += "/rifts enabled <true | false>\n";
        result += "/rifts spawn <player> <rift ID>\n";
        result += "/rifts stop\n";
        result += "/rifts ids";
        sender.sendMessage(result);
    }
}
