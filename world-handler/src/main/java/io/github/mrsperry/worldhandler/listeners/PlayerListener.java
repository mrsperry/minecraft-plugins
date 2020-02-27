package io.github.mrsperry.worldhandler.listeners;

import io.github.mrsperry.mcutils.ConfigManager;
import io.github.mrsperry.worldhandler.Main;
import io.github.mrsperry.worldhandler.worlds.CustomWorld;
import io.github.mrsperry.worldhandler.worlds.WorldHandler;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerListener implements Listener {
    private ConfigManager manager;
    private YamlConfiguration config;

    public PlayerListener(final ConfigManager manager) {
        this.manager = manager;
        this.config = manager.getConfig("inventories");
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final CustomWorld world = WorldHandler.getWorld(player.getWorld().getName());
        if (world == null) {
            return;
        }

        player.setGameMode(world.getDefaultGameMode());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        final Location newLocation = event.getTo();
        if (newLocation == null) {
            return;
        }

        final World toWorld = newLocation.getWorld();
        final World fromWorld = event.getFrom().getWorld();

        if (fromWorld != null) {
            final CustomWorld world = WorldHandler.getWorld(fromWorld.getName());

            if (world != null && world.getRetainInventory()) {
                this.saveInventory(player);
            }
        }

        if (toWorld != null) {
            final CustomWorld world = WorldHandler.getWorld(toWorld.getName());
            if (world == null) {
                return;
            }

            player.setGameMode(world.getDefaultGameMode());

            if (world.getRetainInventory()) {
                this.loadInventory(player, toWorld);
            }
        }
    }

    private void saveInventory(final Player player) {
        final Inventory inventory = player.getInventory();

        // Serialize their item stacks
        final HashMap<Integer, Map<String, Object>> serializedInventory = new HashMap<>();
        for (int index = 0; index < inventory.getSize(); index++) {
            final ItemStack item = inventory.getItem(index);

            if (item != null && item.getType() != Material.AIR) {
                serializedInventory.put(index, item.serialize());
            }
        }

        this.config.createSection(player.getWorld().getName() + "." + player.getUniqueId().toString(), serializedInventory);
        this.manager.saveConfig("inventories");

        inventory.clear();
    }

    private void loadInventory(final Player player, final World world) {
        final Inventory inventory = player.getInventory();

        final HashMap<Integer, Map<String, Object>> serializedInventory = new HashMap<>();
        try {
            final String path = world.getName() + "." + player.getUniqueId().toString();
            final ConfigurationSection section = this.config.getConfigurationSection(path);

            for (final String key : section.getKeys(false)) {
                serializedInventory.put(Integer.parseInt(key), this.config.getConfigurationSection(path + "." + key).getValues(false));
            }
        } catch (final Exception ex) {
            Main.getInstance().getLogger().severe("Could not read serialized inventory for: " + player.getName());
            return;
        }

        for (final Integer key : serializedInventory.keySet()) {
            inventory.setItem(key, ItemStack.deserialize(serializedInventory.get(key)));
        }
    }
}
