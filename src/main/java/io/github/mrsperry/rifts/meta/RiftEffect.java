package io.github.mrsperry.rifts.meta;

import com.google.common.collect.Lists;

import io.github.mrsperry.rifts.Main;
import io.github.mrsperry.rifts.configs.RiftConfig;
import io.github.mrsperry.rifts.utils.SpawnUtils;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class RiftEffect implements Runnable {
    private RiftConfig config;
    private Location center;
    private List<Location> valid;

    public RiftEffect(RiftConfig config) {
        this.config = config;
    }

    public RiftEffect setCenter(Location location) {
        this.center = location;
        return this;
    }

    public RiftEffect setValidLocations(List<Location> valid) {
        this.valid = valid;
        return this;
    }

    @Override
    public void run() {
        // TODO: dont check particle locations, make big circle
        World world = this.center.getWorld();
        if (this.config.showCoreEffect()) {
            for (Location core : getCoreLocations(this.center)) {
                world.playEffect(core, Effect.EXPLOSION_HUGE, 1);
            }
        }
        if (this.config.showSecondaryEffect()) {
            Effect effect = Main.getRandom().nextInt(2) == 0 ? Effect.PORTAL : Effect.MAGIC_CRIT;
            for (Location secondary : getSecondaryLocations(this.center)) {
                world.playEffect(secondary, effect, 1);
            }
        }
        if (this.config.showAmbientEffect()) {
            Effect effect = Main.getRandom().nextInt(2) == 0 ? Effect.COLOURED_DUST : Effect.WITCH_MAGIC;
            SpawnUtils.spawn(this.valid, 15, (location, count) ->
                    location.getWorld().spigot().playEffect(location, effect, 0, 0, 1, 0, 1, 1, 0, 16)
            );
        }
    }

    public static ArrayList<Location> getCoreLocations(Location location) {
        return Lists.newArrayList(
                location.clone().add(0.5, 2.5, 0.5), location.clone().add(0.5, 3.5, 0.5), location.clone().add(1.5, 1.5, 0.5),
                location.clone().add(0.5, 3.5, 1.5), location.clone().add(-1.5, 3.5, 0.5), location.clone().add(0.5, 3.5, -1.5),
                location.clone().add(0.5, 4.5, 0.5)
        );
    }

    public static ArrayList<Location> getSecondaryLocations(Location location) {
        return Lists.newArrayList(
                location.clone().add(0.5, 0.5, 0.5), location.clone().add(0.5, 1.5, 0.5), location.clone().add(1.5, 2.5, 0.5),
                location.clone().add(0.5, 2.5, 1.5), location.clone().add(-1.5, 2.5, 0.5), location.clone().add(0.5, 2.5, -1.5),
                location.clone().add(2.5, 3.5, 0.5), location.clone().add(0.5, 3.5, 2.5), location.clone().add(-2.5, 3.5, 0.5),
                location.clone().add(0.5, 3.5, -2.5), location.clone().add(1.5, 3.5, 1.5), location.clone().add(1.5, 3.5, -1.5),
                location.clone().add(-1.5, 3.5, 1.5), location.clone().add(-2.5, 3.5, -2.5), location.clone().add(1.5, 4.5, 0.5),
                location.clone().add(0.5, 4.5, 1.5), location.clone().add(-1.5, 4.5, 0.5), location.clone().add(0.5, 4.5, -1.5),
                location.clone().add(0.5, 5.5, 0.5), location.clone().add(0.5, 6.5, 0.5)
        );
    }
}
