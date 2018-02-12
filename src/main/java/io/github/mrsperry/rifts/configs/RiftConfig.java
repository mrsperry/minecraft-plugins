package io.github.mrsperry.rifts.configs;

import io.github.mrsperry.rifts.configs.BasicConfig;
import io.github.mrsperry.rifts.rifts.RiftSize;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;

public class RiftConfig extends BasicConfig {
    private String riftID;
    private RiftSize riftSize;
    private HashSet<EntityType> monsters;
    private HashSet<PotionEffectType> potionEffects;
    private int maxPotionsApplied;
    private Particle coreParticle;
    private Particle ambientParticle;

    public RiftConfig(String path) {
        super(path);
    }

    public boolean loadValues() {
        this.riftID = this.getString("rift.riftID", "INVALID");
        this.riftSize = RiftSize.valueOf(this.getString("rift.riftSize", "SMALL"));
        this.maxPotionsApplied = this.getInt("rift.maxPotionsApplied", 1);
        this.coreParticle = Particle.valueOf(this.getString("rift.coreParticle", "SMOKE_NORMAL"));
        this.ambientParticle = Particle.valueOf(this.getString("rift.ambientParticle", "PORTAL"));

        this.monsters = new HashSet<>();
        for(String monsterType : this.getStringList("rift.monsters")) {
            try {
                this.monsters.add(EntityType.valueOf(monsterType));
            } catch (Exception e) {
                Bukkit.getLogger().warning("Error loading monster in " + this.riftID + " config: " + e.getMessage());
            }
        }

        this.potionEffects = new HashSet<>();
        for(String potionType : this.getStringList("rift.potionEffects")) {
            try {
                this.potionEffects.add(PotionEffectType.getByName(potionType));
            } catch (Exception e) {
                Bukkit.getLogger().warning("Error loading potion effect in " + this.riftID + " config: " + e.getMessage());
            }
        }

        return !this.riftID.equals("INVALID");
    }

    @Override
    public boolean reload() {
        boolean result = super.reload();

        if(result) {
            return this.loadValues();
        }
        return false;
    }

    public String getRiftID() {
        return riftID;
    }

    public RiftSize getRiftSize() {
        return riftSize;
    }

    public HashSet<EntityType> getMonsters() {
        return monsters;
    }

    public HashSet<PotionEffectType> getPotionEffects() {
        return potionEffects;
    }

    public int getMaxPotionsApplied() {
        return maxPotionsApplied;
    }

    public Particle getCoreParticle() {
        return coreParticle;
    }

    public Particle getAmbientParticle() {
        return ambientParticle;
    }
}
