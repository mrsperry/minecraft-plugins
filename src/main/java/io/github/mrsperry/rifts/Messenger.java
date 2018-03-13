package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.configs.GeneralConfig;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class Messenger {
    public static void sendJoinMessage(Player player) {
        String message = GeneralConfig.getJoinMessage();
        if (!message.equals("")) {
            player.sendMessage(ChatColor.GRAY + message);
        }
    }

    public static void sendStartMessage(World world) {
        String message = GeneralConfig.getStartMessage();
        if (!message.equals("")) {
            for (Player player : world.getPlayers()) {
                player.sendMessage(ChatColor.GRAY + message);
                world.playSound(player.getLocation(), GeneralConfig.getStartSound(), GeneralConfig.getStartVolume(), 0);
            }
        }
    }

    public static void sendStopMessage(World world) {
        String message = GeneralConfig.getStopMessage();
        if (!message.equals("")) {
            for (Player player : world.getPlayers()) {
                player.sendMessage(ChatColor.GRAY + message);
                world.playSound(player.getLocation(), GeneralConfig.getStopSound(), GeneralConfig.getStopVolume(), 0);
            }
        }
    }
}
