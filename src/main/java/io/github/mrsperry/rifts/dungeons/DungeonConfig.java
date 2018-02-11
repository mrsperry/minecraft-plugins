package io.github.mrsperry.rifts.dungeons;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;

public class DungeonConfig {
    private int dungeonID;
    private int bossID;
    private HashSet<EntityType> monsters;
    private HashSet<PotionEffectType> monsterPotionEffects;
    private HashSet<PotionEffectType> playerPotionEffects;
    private Location startingLocation;
    private Location portalLocation;
    private Location chestLocation;
    private HashSet<Particle> particles; // maybe not particle type?

    public DungeonConfig() {
        // TODO: set variables
    }

    public int getDungeonID() {
        return dungeonID;
    }

    public int getBossID() {
        return this.bossID;
    }

    public HashSet<EntityType> getMonsters() {
        return this.monsters;
    }

    public HashSet<PotionEffectType> getMonsterPotionEffects() {
        return this.monsterPotionEffects;
    }

    public HashSet<PotionEffectType> getPlayerPotionEffects() {
        return this.playerPotionEffects;
    }

    public Location getStartingLocation() {
        return this.startingLocation;
    }

    public Location getPortalLocation() {
        return this.portalLocation;
    }

    public Location getChestLocation() {
        return this.chestLocation;
    }

    public HashSet<Particle> getParticles() {
        return this.particles;
    }
}
