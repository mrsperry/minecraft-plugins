package io.github.mrsperry.croptweaks;

import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;
import org.bukkit.material.NetherWarts;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class Main extends JavaPlugin implements Listener {
    private final Random random = new Random();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    private boolean setCrop(Crops crop) {
        if (crop.getState() == CropState.RIPE) {
            crop.setState(CropState.SEEDED);
            return true;
        }
        return false;
    }

    private void drop(Location location, Material material, int amount) {
        location.getWorld().dropItemNaturally(location, new ItemStack(material, amount));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getHand() == EquipmentSlot.HAND) {
                Block block = event.getClickedBlock();
                switch (block.getType()) {
                    case NETHER_WARTS:
                        NetherWarts warts = (NetherWarts) block.getState().getData();
                        if (warts.getState() == NetherWartsState.RIPE) {
                            warts.setState(NetherWartsState.SEEDED);
                            drop(block.getLocation(), Material.NETHER_STALK, this.random.nextInt(2) + 1);
                            event.setCancelled(true);
                        } else {
                            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
                            if (item.getType() == Material.BLAZE_POWDER) {
                                if (warts.getState() == NetherWartsState.SEEDED) {
                                    warts.setState(NetherWartsState.STAGE_ONE);
                                } else if (warts.getState() == NetherWartsState.STAGE_ONE) {
                                    warts.setState(NetherWartsState.STAGE_TWO);
                                } else if (warts.getState() == NetherWartsState.STAGE_TWO) {
                                    warts.setState(NetherWartsState.RIPE);
                                }
                                item.setAmount(item.getAmount() - 1);
                                event.getPlayer().getInventory().setItemInMainHand(item);
                            }
                        }
                        block.setData(warts.getData());
                        break;
                    case CROPS:
                        Crops wheat = (Crops) block.getState().getData();
                        if (setCrop(wheat)) {
                            drop(block.getLocation(), Material.WHEAT, 1);
                            block.setData(wheat.getData());
                            event.setCancelled(true);
                        }
                        break;
                    case CARROT:
                        Crops carrot = (Crops) block.getState().getData();
                        if (setCrop(carrot)) {
                            drop(block.getLocation(), Material.CARROT_ITEM, this.random.nextInt(2) + 1);
                            block.setData(carrot.getData());
                            event.setCancelled(true);
                        }
                        break;
                    case POTATO:
                        Crops potato = (Crops) block.getState().getData();
                        if (setCrop(potato)) {
                            drop(block.getLocation(), Material.POTATO_ITEM, this.random.nextInt(2) + 1);
                            block.setData(potato.getData());
                            event.setCancelled(true);
                        }
                        break;
                    case BEETROOT_BLOCK:
                        Crops beetroot = (Crops) block.getState().getData();
                        if (setCrop(beetroot)) {
                            drop(block.getLocation(), Material.BEETROOT, this.random.nextInt(2) + 1);
                            block.setData(beetroot.getData());
                            event.setCancelled(true);
                        }
                        break;
                }
            }
        }
    }
}
