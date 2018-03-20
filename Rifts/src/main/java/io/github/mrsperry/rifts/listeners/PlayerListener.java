package io.github.mrsperry.rifts.listeners;

import io.github.mrsperry.rifts.Messenger;
import io.github.mrsperry.rifts.RiftManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (RiftManager.getCurrentRiftId() > 0) {
            Messenger.sendJoinMessage(event.getPlayer());
        }
    }
}
