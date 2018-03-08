package io.github.mrsperry.rifts.utils;

import io.github.mrsperry.rifts.Rifts;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class MobUtils {
    public static boolean listContainsMonster(ArrayList<Monster> monsters, Entity entity) {
        if (entity instanceof Monster) {
            return monsters.contains(entity);
        }
        return false;
    }

    public static EntityType getRandomMob(List<EntityType> monsters) {
        return monsters.get(Rifts.getRandom().nextInt(monsters.size()));
    }

    public static List<PotionEffectType> getRandomEffects(List<PotionEffectType> possible, int max) {
        int amount = Rifts.getRandom().nextInt(max+1);
        possible = new ArrayList<>(possible);
        List<PotionEffectType> effects = new ArrayList<>();
        int count = 0;

        while(count < amount && possible.size() > 0) {
            int index = Rifts.getRandom().nextInt(possible.size());
            effects.add(possible.get(index));
            possible.remove(index);
            count++;
        }
        return effects;
    }
}
