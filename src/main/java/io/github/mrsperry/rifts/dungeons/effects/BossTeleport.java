package io.github.mrsperry.rifts.dungeons.effects;

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
        Location valid = this.findValidLocation(this.boss.getLocation());
        if (teleport && valid != null) {
            // TODO: play particle effects
            this.boss.teleport(valid);
        }
    }
}
