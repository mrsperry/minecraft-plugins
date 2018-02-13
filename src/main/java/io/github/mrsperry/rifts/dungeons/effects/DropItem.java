package io.github.mrsperry.rifts.dungeons.effects;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;

public class DropItem extends BossEffect implements Listener {
    public DropItem(Monster boss, ArrayList<Player> players, int effectRadius) {
        super(boss, players, effectRadius);
    }

    public void playEffect() {
        for (Player player : this.players) {
            Location location = player.getLocation();
            if (this.checkDistance(location)) {
                location.getWorld().dropItemNaturally(location, player.getInventory().getItemInMainHand());
                player.getInventory().getItemInMainHand().setType(Material.AIR);
            }
        }
    }
}
