package io.github.mrsperry.rifts.configs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;

public class BossConfig extends BasicConfig {
    private String name;
    private String bossID;
    private double maxHealth;
    private double regenRate;
    private PlayerInventory inventory;
    private HashSet<PotionEffectType> potionEffects;
    //private HashSet<Ability> abilities;
    //private String loottable;

    public BossConfig(String path) {
        super(path);
    }

    public boolean loadValues() {
        this.name = this.getString("boss.name", "RANDOM");
        this.bossID = this.getString("boss.boss-id", "INVALID");
        this.maxHealth = this.getDouble("boss.max-health", 100.0);
        this.regenRate = this.getDouble("boss.regen-rate", 1.5);

        this.inventory = (PlayerInventory) Bukkit.createInventory(null, InventoryType.PLAYER);
        this.inventory.setItemInMainHand(
                new ItemStack(Material.valueOf(
                        this.getString("boss.inventory.main-hand", "AIR"))));
        this.inventory.setItemInOffHand(
                new ItemStack(Material.valueOf(
                        this.getString("boss.inventory.off-hand", "AIR"))));
        this.inventory.setHelmet(
                new ItemStack(Material.valueOf(
                        this.getString("boss.inventory.helmet", "AIR"))));
        this.inventory.setChestplate(
                new ItemStack(Material.valueOf(
                        this.getString("boss.inventory.chestplate", "AIR"))));
        this.inventory.setLeggings(
                new ItemStack(Material.valueOf(
                        this.getString("boss.inventory.leggings", "AIR"))));
        this.inventory.setBoots(
                new ItemStack(Material.valueOf(
                        this.getString("boss.inventory.boots", "AIR"))));

        this.potionEffects = new HashSet<>();
        for(String potionEffect : this.getStringList("boss.potion-effects")) {
            try {
                this.potionEffects.add(PotionEffectType.getByName(potionEffect));
            } catch (Exception e) {
                Bukkit.getLogger().warning("Error loading boss's potion effects in " + this.bossID + " config: " + e.getMessage());
            }
        }

        return this.bossID.equals("INVALID");
    }

    @Override
    public boolean reload() {
        boolean result = super.reload();

        if(result) {
            return loadValues();
        }

        return result;
    }
}
