package io.github.mrsperry.rifts.dungeons.effects;

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
                Location valid = this.findValidLocation(player.getLocation());
                if (valid != null) {
                    // TODO: play particle effects
                    player.teleport(valid);
                }
            }
        }
    }
}
