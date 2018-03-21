package io.github.mrsperry.mobeggs;

import com.google.common.collect.Lists;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin implements Listener {
    // Can't group all mobs that have eggs any other way
    public ArrayList<EntityType> allowed = Lists.newArrayList(
            EntityType.BAT, EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.CHICKEN, EntityType.COW,
            EntityType.CREEPER, EntityType.DONKEY, EntityType.ELDER_GUARDIAN, EntityType.ENDERMAN, EntityType.ENDERMITE,
            EntityType.EVOKER, EntityType.GHAST, EntityType.GUARDIAN, EntityType.HORSE, EntityType.HUSK,
            EntityType.LLAMA, EntityType.MAGMA_CUBE, EntityType.MULE, EntityType.MUSHROOM_COW, EntityType.OCELOT,
            EntityType.PARROT, EntityType.PIG, EntityType.PIG_ZOMBIE, EntityType.POLAR_BEAR, EntityType.RABBIT,
            EntityType.SHEEP, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SKELETON_HORSE, EntityType.SLIME,
            EntityType.SPIDER, EntityType.SQUID, EntityType.STRAY, EntityType.SHULKER, EntityType.VEX,
            EntityType.VILLAGER, EntityType.VINDICATOR, EntityType.WITCH, EntityType.WITHER_SKELETON, EntityType.WOLF,
            EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIE_VILLAGER
    );

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        FileConfiguration config = this.getConfig();
        if (config.isList("blacklist")) {
            for (String type : config.getStringList("blacklist")) {
                try {
                    EntityType entityType = EntityType.valueOf(type.toUpperCase());
                    if (this.allowed.contains(entityType)) {
                        this.allowed.remove(entityType);
                    }
                } catch (Exception ex) {
                    this.getLogger().severe("Invalid entity type: " + type);
                }
            }
        }

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity hit = event.getEntity();
        if (event.getDamager() instanceof Egg) {
            if (hit != null && this.allowed.contains(hit.getType())) {
                // Set NBT for the mob egg
                net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(new ItemStack(Material.MONSTER_EGG));
                NBTTagCompound tag = new NBTTagCompound();
                NBTTagCompound id = new NBTTagCompound();
                id.setString("id", this.getType(hit.getType()));
                tag.set("EntityTag", id);
                nmsStack.setTag(tag);

                event.setCancelled(true);
                hit.getWorld().dropItemNaturally(hit.getLocation().add(0, 1, 0), CraftItemStack.asBukkitCopy(nmsStack));
                hit.getWorld().playSound(hit.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 0);
                hit.remove();
            }
        }
    }

    private String getType(EntityType type) {
        switch (type) {
            case MUSHROOM_COW:
                return "MOOSHROOM";
            case VINDICATOR:
                return "VINDICATION_ILLAGER";
            case PIG_ZOMBIE:
                return "ZOMBIE_PIGMAN";
            case EVOKER:
                return "EVOCATION_ILLAGER";
        }
        return type.toString();
    }
}
