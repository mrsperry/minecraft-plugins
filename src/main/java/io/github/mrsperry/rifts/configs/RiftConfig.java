package io.github.mrsperry.rifts.configs;

import io.github.mrsperry.rifts.rifts.RiftSize;
import io.github.mrsperry.rifts.rifts.RiftSize.CustomRiftSize;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class RiftConfig extends BasicConfig {
    private String riftID;
    private CustomRiftSize riftSize;
    private List<EntityType> monsters;
    private List<PotionEffectType> potionEffects;
    private int maxPotionsApplied;
    private boolean showCoreEffect;
    private boolean showSecondaryEffect;
    private boolean showAmbientEffect;

    public RiftConfig(String path) {
        super(path);
    }

    public boolean loadValues() {
        this.riftID = this.getString("rift.rift-id", "INVALID");
        this.riftSize = RiftSize.getInstance().get(this.getString("rift.rift-size", "small").toLowerCase());
        this.maxPotionsApplied = this.getInt("rift.max-potions-applied", 1);
        this.showCoreEffect = this.getBoolean("rift.core-effect", true);
        this.showAmbientEffect = this.getBoolean("rift.ambient-effect", true);
        this.showSecondaryEffect = this.getBoolean("rift.secondary-effect", true);

        this.monsters = new ArrayList<>();
        for(String monsterType : this.getStringList("rift.monsters")) {
            try {
                EntityType type = EntityType.valueOf(monsterType.toUpperCase());
                this.monsters.add(type);
            } catch (Exception e) {
                Bukkit.getLogger().warning("Error loading monster in " + this.riftID + " config! Unknown monster " + monsterType);
            }
        }

        this.potionEffects = new ArrayList<>();
        for(String potionType : this.getStringList("rift.potion-effects")) {
            PotionEffectType type = PotionEffectType.getByName(potionType);

            if(type != null) {
                this.potionEffects.add(type);
            } else {
                Bukkit.getLogger().warning("Error loading potion effect in " + this.riftID + " config! Unknown potion effect " + potionType);
            }
        }

        Bukkit.broadcastMessage(this.riftID + " : " + this.riftSize.toString());
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

    public List<EntityType> getMonsters() {
        return monsters;
    }

    public List<PotionEffectType> getPotionEffects() {
        return potionEffects;
    }

    public int getMaxPotionsApplied() {
        return maxPotionsApplied;
    }

    public boolean showCoreEffect() {
        return showCoreEffect;
    }

    public boolean showAmbientEffect() {
        return showAmbientEffect;
    }

    public boolean showSecondaryEffect() { return showSecondaryEffect; }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
