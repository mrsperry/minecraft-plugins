package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.configs.GeneralConfig;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Messenger {
    public static void sendJoinMessage(Player player) {
        String message = GeneralConfig.getJoinMessage();
        if (!message.equals("")) {
            player.sendMessage(ChatColor.GRAY + message);
        }
    }

    public static void sendStartMessage(World world) {
        ArrayList<String> messages = GeneralConfig.getStartMessages();
        if (messages.size() > 0) {
            for (Player player : world.getPlayers()) {
                player.sendMessage(ChatColor.GRAY + messages.get(Main.getRandom().nextInt(messages.size())));
                world.playSound(player.getLocation(), GeneralConfig.getStartSound(), GeneralConfig.getStartVolume(), 0);
            }
        }
    }

    public static void sendStopMessage(World world) {
        ArrayList<String> messages = GeneralConfig.getStopMessages();
        if (messages.size() > 0) {
            for (Player player : world.getPlayers()) {
                player.sendMessage(ChatColor.GRAY + messages.get(Main.getRandom().nextInt(messages.size())));
                world.playSound(player.getLocation(), GeneralConfig.getStopSound(), GeneralConfig.getStopVolume(), 0);
            }
        }
    }
}
