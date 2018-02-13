package io.github.mrsperry.rifts.dungeons.effects;

import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Fireballs extends BossEffect {
    public Fireballs(Monster boss, ArrayList<Player> players, int effectRadius) {
        super(boss, players, effectRadius);
    }

    public void playEffect() {
        Location above = this.boss.getEyeLocation().add(0, 1, 0);
        for (Player player : this.players) {
            Location location = player.getLocation();
            if (this.checkDistance(location)) {
                Fireball fireball = player.getWorld().spawn(above, Fireball.class);
                fireball.setVelocity(location.toVector().subtract(above.toVector()).normalize());
            }
        }
    }
}
