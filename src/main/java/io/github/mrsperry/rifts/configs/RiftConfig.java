package io.github.mrsperry.rifts.configs;

import io.github.mrsperry.rifts.meta.RiftSize;
import io.github.mrsperry.rifts.meta.RiftSize.CustomRiftSize;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RiftConfig extends BasicConfig {
    private String riftID;
    private CustomRiftSize riftSize;
    private int maxPotionsApplied;
    private boolean showCoreEffect;
    private boolean showSecondaryEffect;
    private boolean showAmbientEffect;

    private List<EntityType> monsters;
    private List<PotionEffectType> potionEffects;
    private HashMap<Sound, Float> coreSounds;
    private HashMap<Sound, Float> secondarySounds;

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
        for (String monsterType : this.getStringList("rift.monsters")) {
            try {
                this.monsters.add(EntityType.valueOf(monsterType.toUpperCase()));
            } catch (Exception ex) {
                Bukkit.getLogger().warning("Error loading monster in " + this.riftID + " config! Unknown monster: " + monsterType);
            }
        }

        this.potionEffects = new ArrayList<>();
        for (String potionType : this.getStringList("rift.potion-effects")) {
            PotionEffectType type = PotionEffectType.getByName(potionType.toUpperCase());
            if (type != null) {
                this.potionEffects.add(type);
            } else {
                Bukkit.getLogger().warning("Error loading potion effect in " + this.riftID + " config! Unknown potion effect: " + potionType);
            }
        }

        this.coreSounds = new HashMap<>();
        for (String soundType : this.getStringList("rift.sounds.core")) {
            String[] fullSound = soundType.split(":");
            try {
                this.coreSounds.put(Sound.valueOf(fullSound[0].toUpperCase()), Float.parseFloat(fullSound[1]));
            } catch (Exception ex) {
                Bukkit.getLogger().warning("Error loading core sound in " + this.riftID + " config! Unknown sound or invalid volume: " + soundType);
            }
        }

        this.secondarySounds = new HashMap<>();
        for (String soundType : this.getStringList("rift.sounds.secondary")) {
            String[] fullSound = soundType.split(":");
            try {
                this.secondarySounds.put(Sound.valueOf(fullSound[0].toUpperCase()), Float.parseFloat(fullSound[1]));
            } catch (Exception ex) {
                Bukkit.getLogger().warning("Error loading secondary sound in " + this.riftID + " config! Unknown sound or invalid volume: " + soundType);
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
        return this.riftID;
    }

    public CustomRiftSize getRiftSize() {
        return this.riftSize;
    }

    public int getMaxPotionsApplied() {
        return this.maxPotionsApplied;
    }

    public boolean showCoreEffect() {
        return this.showCoreEffect;
    }

    public boolean showAmbientEffect() {
        return this.showAmbientEffect;
    }

    public boolean showSecondaryEffect() {
        return this.showSecondaryEffect;
    }

    public List<EntityType> getMonsters() {
        return this.monsters;
    }

    public List<PotionEffectType> getPotionEffects() {
        return this.potionEffects;
    }

    public HashMap<Sound, Float> getCoreSounds() {
        return this.coreSounds;
    }

    public HashMap<Sound, Float> getSecondarySounds() {
        return this.secondarySounds;
    }
}
