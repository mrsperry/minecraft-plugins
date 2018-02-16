package io.github.mrsperry.rifts.dungeons.effects;

import io.github.mrsperry.rifts.Rifts;
import io.github.mrsperry.rifts.utils.SpawnUtils;

import org.bukkit.Location;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BossTeleport extends BossEffect {
    public BossTeleport(Monster boss, ArrayList<Player> players, int effectRadius) {
        super(boss, players, effectRadius);
    }

    public void playEffect() {
        boolean teleport = false;
        for (Player player : this.players) {
            if (this.checkDistance(player.getLocation())) {
                teleport = true;
            }
        }
        ArrayList<Location> valids = SpawnUtils.getValidLocations(this.boss.getLocation(), 5, 0, 5);
        Location valid = (valids.size() > 0 ? valids.get(Rifts.getRandom().nextInt(valids.size())) : null);
        if (teleport && valid != null) {
            // TODO: play particle effects
            this.boss.teleport(valid);
        }
    }
}
