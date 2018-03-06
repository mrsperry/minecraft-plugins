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

    public static void sendCreateMessage(World world) {
        String message = GeneralConfig.getCreateMessage();
        if (!message.equals("")) {
            for (Player player : world.getPlayers()) {
                player.sendMessage(ChatColor.GRAY + message);
            }
        }
    }

    public static void sendDeathMessage(World world) {
        String message = GeneralConfig.getDeathMessage();
        if (!message.equals("")) {
            for (Player player : world.getPlayers()) {
                player.sendMessage(ChatColor.GRAY + message);
            }
        }
    }
}
