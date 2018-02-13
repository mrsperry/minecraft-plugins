package io.github.mrsperry.rifts.dungeons.effects;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Velocity extends BossEffect {
    public Velocity(Monster boss, ArrayList<Player> players, int effectRadius) {
        super(boss, players, effectRadius);
    }

    public void playEffect() {
        for (Player player : this.players) {
            if (this.checkDistance(player.getLocation())) {
                player.setVelocity(new Vector(0, 3, 0));
            }
        }
    }
}
