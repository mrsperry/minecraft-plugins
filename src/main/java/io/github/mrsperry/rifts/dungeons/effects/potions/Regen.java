package io.github.mrsperry.rifts.dungeons.effects.potions;

import io.github.mrsperry.rifts.dungeons.effects.BossEffect;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Regen extends BossEffect {
    public Regen(Monster boss, ArrayList<Player> players, int effectRadius) {
        super(boss, players, effectRadius);
    }

    public void playEffect() {
        // TODO: regen effect/particles
    }
}
