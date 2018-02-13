package io.github.mrsperry.rifts.configs;

import io.github.mrsperry.rifts.configs.BasicConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;

public class DungeonConfig extends BasicConfig {
    private String dungeonID;
    private String bossID;
    private HashSet<EntityType> monsters;
    private HashSet<PotionEffectType> monsterPotionEffects;
    private HashSet<PotionEffectType> playerPotionEffects;
    private String startingLocation;
    private String portalLocation;
    private String chestLocation;
    private HashSet<Particle> particles; // maybe not particle type?

    public DungeonConfig(String path) {
        super(path);
    }

    public boolean loadValues() {
        this.dungeonID = this.getString("dungeon.dungeon-id", "INVALID");
        this.bossID = this.getString("dungeon.boss-id", "INVALID");
        this.monsters = new HashSet<>();

        for(String monsterType : this.getStringList("dungeon.monsters")) {
            try {
                this.monsters.add(EntityType.valueOf(monsterType));
            } catch (Exception e) {
                Bukkit.getLogger().warning("Error loading monsters in " + this.dungeonID + " config: " + e.getMessage());
            }
        }

        this.monsterPotionEffects = new HashSet<>();
        for(String monsterPE : this.getStringList("dungeon.monster-potion-effects")) {
            try {
                this.monsterPotionEffects.add(PotionEffectType.getByName(monsterPE));
            } catch (Exception e) {
                Bukkit.getLogger().warning("Error loading monster's potion effects in " + this.dungeonID + " config: " + e.getMessage());
            }
        }

        this.playerPotionEffects = new HashSet<>();
        for(String playerPE : this.getStringList("dungeon.player-potion-effects")) {
            try {
                this.playerPotionEffects.add(PotionEffectType.getByName(playerPE));
            } catch (Exception e) {
                Bukkit.getLogger().warning("Error loading players's potion effects in " + this.dungeonID + " config: " + e.getMessage());
            }
        }

        this.startingLocation = this.getString("dungeon.starting-location", "INVALID");
        this.portalLocation = this.getString("dungeon.portal-location", "INVALID");
        this.chestLocation = this.getString("dungeon.chest-location", "INVALID");

        this.particles = new HashSet<>();
        for(String particleType : this.getStringList("dungeon.particles")) {
            try {
                this.particles.add(Particle.valueOf(particleType));
            } catch (Exception e) {
                Bukkit.getLogger().warning("Error loading particle effects in " + this.dungeonID + " config: " + e.getMessage());
            }
        }

        return  !(this.dungeonID.equals("INVALID")        ||
                  this.bossID.equals("INVALID")           ||
                  this.startingLocation.equals("INVALID") ||
                  this.portalLocation.equals("INVALID")   ||
                  this.chestLocation.equals("INVALID"));
    }

    @Override
    public boolean reload() {
        boolean result = super.reload();

        if (result) {
            return this.loadValues();
        }

        return false;
    }

    public String getDungeonID() {
        return this.dungeonID;
    }

    public String getBossID() {
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

    public String getStartingLocation() {
        return this.startingLocation;
    }

    public String getPortalLocation() {
        return this.portalLocation;
    }

    public String getChestLocation() {
        return this.chestLocation;
    }

    public HashSet<Particle> getParticles() {
        return this.particles;
    }
}
