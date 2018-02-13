package io.github.mrsperry.rifts.dungeons.effects;

import io.github.mrsperry.rifts.Rifts;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Lightning extends BossEffect {
    public Lightning(Monster boss, ArrayList<Player> players, int effectRadius) {
        super(boss, players, effectRadius);
    }

    public void playEffect() {
        Location bossLocation = this.boss.getLocation();
        for (Player player : this.players) {
            Location location = player.getLocation();
            // prevent the boss from getting hit by their own lightning
            if (bossLocation.getBlock().equals(location.getBlock())) {
                continue;
            }
            if (this.checkDistance(location)) {
                int blockX = location.getBlockX();
                int blockZ = location.getBlockZ();
                ArrayList<Location> validLocations = new ArrayList<>();
                // allow lightning strikes in a 3x3 area around each player
                for (int x = blockX; x < blockX + 3; x++) {
                    for (int z = blockZ; z < blockZ + 3; blockZ++) {
                        Block block = location.getWorld().getBlockAt(x, location.getBlockY(), z);
                        if (block.isEmpty()) {
                            validLocations.add(block.getLocation());
                        }
                    }
                }
                if (validLocations.size() > 4) {
                    for (int index = 0; index < 4; index++) {
                        this.spawnLightning(validLocations.get(Rifts.getRandom().nextInt(validLocations.size())));
                    }
                } else {
                    for (Location valid : validLocations) {
                        this.spawnLightning(valid);
                    }
                }
            }
        }
    }

    private void spawnLightning(Location location) {
        location.getWorld().spawn(location, LightningStrike.class);
    }
}
