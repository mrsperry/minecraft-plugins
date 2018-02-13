package io.github.mrsperry.rifts.dungeons.effects;

import io.github.mrsperry.rifts.Rifts;
import io.github.mrsperry.rifts.utils.SpawnUtils;
import org.bukkit.Location;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public abstract class BossEffect implements IBossEffect {
    public Monster boss;
    public ArrayList<Player> players;
    public int effectRadius;

    public BossEffect(Monster boss, ArrayList<Player> players, int effectRadius) {
        this.boss = boss;
        this.players = players;
        this.effectRadius = effectRadius;
    }

    public void playEffect() {
        // TODO: default effect things
    }

    public boolean checkDistance(Location player) {
        return player.distance(this.boss.getLocation()) <= this.effectRadius + 1;
    }

    public Location findValidLocation(Location center) {
        ArrayList<Location> valids = new ArrayList<>();
        for (int x = center.subtract(5, 0, 0).getBlockX(); x <= 10; x++) {
            for (int z = center.subtract(0, 0, 5).getBlockZ(); z <= 10; z++) {
                Location location = new Location(center.getWorld(), x, center.getBlockY(), z);
                if (SpawnUtils.isValidLocation(location)) {
                    valids.add(location);
                }
            }
        }

        if (valids.size() > 0) {
            return valids.get(Rifts.getRandom().nextInt(valids.size()));
        } else {
            return null;
        }
    }
}
