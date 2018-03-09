package io.github.mrsperry.rifts.rifts;

import io.github.mrsperry.rifts.utils.SpawnUtils;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.List;

public class RiftEffect implements Runnable {
    private Particle coreEffect, secondaryEffect, ambientEffect;
    private Location center;
    private List<Location> valid;

    public RiftEffect() {
        this.coreEffect = Particle.EXPLOSION_LARGE;
        this.secondaryEffect = Particle.PORTAL;
        this.ambientEffect = Particle.SMOKE_NORMAL;
    }

    public RiftEffect setEffects(Particle core, Particle secondary, Particle ambient) {
        this.coreEffect = core;
        this.secondaryEffect = secondary;
        this.ambientEffect = ambient;
        return this;
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
        this.center.getWorld().spawnParticle(this.coreEffect, this.center.clone().add(0.5,0.5,0.5), 1);

        SpawnUtils.spawn(this.valid, 15, (location, count) -> {
            location.getWorld().spawnParticle(this.ambientEffect, location, 1);
        });
    }
}
