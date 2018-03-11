package io.github.mrsperry.rifts.rifts;

import io.github.mrsperry.rifts.RiftManager;
import io.github.mrsperry.rifts.configs.RiftConfig;
import io.github.mrsperry.rifts.Timer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class RiftCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String cmdLine, String[] args) {

        switch (command.getName().toLowerCase()) {
            case "spawnrift":
                return spawnRift(sender, args);
            case "endrift":
                return endrift(sender, args);
            case "riftids":
                return listRifts(sender);
            default:
                return false;
        }
    }

    private boolean listRifts(CommandSender sender) {
        String out = "Rift IDs: [";
        for(String id : RiftManager.getRiftConfigIDs()) {
            out += id + ", ";
        }

        out = out.substring(0, out.length() - 2) + "]";
        sender.sendMessage(ChatColor.GOLD + out);
        return true;
    }

    // /spawnRift |id=R1T| |player=PepsiDog|
    private boolean spawnRift(CommandSender sender, String[] args) {
        Map<String, String> arguments = getArgumentPairs(args, Arrays.asList("id", "player"));
        if(arguments == null) return false;

        Player player = arguments.containsKey("player") ? Bukkit.getPlayer(arguments.get("player")) :
                        sender instanceof Player ? (Player) sender : null;
        RiftConfig config;

        if(arguments.containsKey("id")) {
            if(RiftManager.isConfigID(arguments.get("id"))) {
                config = RiftManager.getRiftConfig(arguments.get("id"));
            } else {
                sender.sendMessage(ChatColor.RED + "Could not find rift config with id " + arguments.get("id"));
                return true;
            }
        } else {
            config = RiftManager.getRandomRiftConfig();
        }

        if(player == null) {
            sender.sendMessage(ChatColor.RED + "No valid player found!");
            return true;
        }

        Timer.spawnRift(player, config.getRiftSize().radius(), config);
        sender.sendMessage(ChatColor.GREEN + "Spawning new rift around player " + player.getName());

        return true;
    }

    // /endrift [# || -a]
    private boolean endrift(CommandSender sender, String[] args) {
        int radius;
        boolean all;
        Location location;

        if(args.length != 1) return false;
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        location = ((Player) sender).getLocation();

        all = args[0].equals("-a");

        if(all) {
            sender.sendMessage(ChatColor.GREEN + "Ending all active rifts...");
            for(Rift rift : RiftManager.getActiveRifts()) {
                rift.end();
                RiftManager.unregisterRift(rift.getID());
            }
        } else {
            radius = Integer.parseInt(args[0], 10);
            sender.sendMessage(ChatColor.GREEN + "Ending all rifts within " + radius + " blocks of you...");
            for(Rift rift : RiftManager.getActiveRifts()) {
                double dist = location.distance(rift.getCenter());
                if(dist <= radius) {
                    rift.end();
                    RiftManager.unregisterRift(rift.getID());
                }
            }
        }

        return true;
    }

    private Map<String, String> getArgumentPairs(String[] args, List<String> validTags) {
        Map<String, String> pairs = new HashMap<>();

        for (String arg : args) {
            String[] temp = arg.split("=");
            if(temp.length != 2 && !validTags.contains(temp[0])) return null;

            pairs.put(temp[0], temp[1]);
        }

        return pairs;
    }
}
