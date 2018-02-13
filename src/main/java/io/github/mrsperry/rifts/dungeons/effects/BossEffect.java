package io.github.mrsperry.rifts.dungeons.effects;

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

    public Location findValidLocation(Location location) {

    }
}
