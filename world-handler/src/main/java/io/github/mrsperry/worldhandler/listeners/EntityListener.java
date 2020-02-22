package io.github.mrsperry.worldhandler.listeners;

import io.github.mrsperry.mcutils.types.EntityTypes;

import io.github.mrsperry.worldhandler.CustomWorld;
import io.github.mrsperry.worldhandler.WorldHandler;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.List;

public class EntityListener implements Listener {
    public static void clearEntities(CustomWorld world, List<EntityType> types) {
        for (LivingEntity entity : world.getWorld().getLivingEntities()) {
            if (types.contains(entity.getType())) {
                entity.remove();
            }
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        CustomWorld world = WorldHandler.getWorld(event.getEntity().getWorld().getName());
        if (world != null) {
            EntityType type = event.getEntityType();

            if (EntityTypes.getHostileTypes().contains(type) && !world.canSpawnMonsters()) {
                event.setCancelled(true);
            } else if (EntityTypes.getNeutralTypes().contains(type) && !world.canSpawnAnimals()) {
                event.setCancelled(true);
            }
        }
    }
}
