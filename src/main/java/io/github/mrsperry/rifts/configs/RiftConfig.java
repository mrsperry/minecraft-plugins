package io.github.mrsperry.rifts.configs;

import io.github.mrsperry.rifts.rifts.RiftSize;
import io.github.mrsperry.rifts.rifts.RiftSize.CustomRiftSize;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;

public class RiftConfig extends BasicConfig {
    private String riftID;
    private CustomRiftSize riftSize;
    private HashSet<EntityType> monsters;
    private HashSet<PotionEffectType> potionEffects;
    private int maxPotionsApplied;
    private Particle coreParticle;
    private Particle ambientParticle;

    public RiftConfig(String path) {
        super(path);
    }

    public boolean loadValues() {
        this.riftID = this.getString("rift.rift-id", "INVALID");
        this.riftSize = RiftSize.getInstance().get(this.getString("rift.rift-size", "small").toLowerCase());
        this.maxPotionsApplied = this.getInt("rift.max-potions-applied", 1);
        this.coreParticle = Particle.valueOf(this.getString("rift.core-particle", "SMOKE_NORMAL").toUpperCase());
        this.ambientParticle = Particle.valueOf(this.getString("rift.ambient-particle", "PORTAL").toUpperCase());

        this.monsters = new HashSet<>();
        for(String monsterType : this.getStringList("rift.monsters")) {
            try {
                EntityType type = EntityType.valueOf(monsterType.toUpperCase());
                this.monsters.add(type);
            } catch (Exception e) {
                Bukkit.getLogger().warning("Error loading monster in " + this.riftID + " config! Unknown monster " + monsterType);
            }
        }

        this.potionEffects = new HashSet<>();
        for(String potionType : this.getStringList("rift.potion-effects")) {
            PotionEffectType type = PotionEffectType.getByName(potionType);

            if(type != null) {
                this.potionEffects.add(type);
            } else {
                Bukkit.getLogger().warning("Error loading potion effect in " + this.riftID + " config! Unknown potion effect " + potionType);
            }
        }

        return !this.riftID.equals("INVALID") && this.riftSize != null;
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

    public CustomRiftSize getRiftSize() {
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
