package io.github.mrsperry.rifts.dungeons.effects;

import io.github.mrsperry.rifts.Rifts;
import io.github.mrsperry.rifts.utils.SpawnUtils;

import org.bukkit.Location;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlayerTeleport extends BossEffect {
    public PlayerTeleport(Monster boss, ArrayList<Player> players, int effectRadius) {
        super(boss, players, effectRadius);
    }

    public void playEffect() {
        for (Player player : this.players) {
            if (this.checkDistance(player.getLocation())) {
                ArrayList<Location> valids = SpawnUtils.getValidLocations(this.boss.getLocation(), 5, 0, 5);
                Location valid = (valids.size() > 0 ? valids.get(Rifts.getRandom().nextInt(valids.size())) : null);
                if (valid != null) {
                    // TODO: play particle effects
                    player.teleport(valid);
                }
            }
        }
    }
}
