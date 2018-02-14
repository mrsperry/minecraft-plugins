package io.github.mrsperry.rifts.dungeons.effects.potions;

import io.github.mrsperry.rifts.dungeons.effects.BossEffect;
import org.bukkit.Location;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Poison extends BossEffect {
    public Poison(Monster boss, ArrayList<Player> players, int effectRadius) {
        super(boss, players, effectRadius);
    }

    public void playEffect() {
        for (Player player : this.players) {
            Location location = player.getLocation();
            if (this.checkDistance(location)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10, 2));
            }
        }
    }
}
