package io.github.mrsperry.rifts.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;

import java.util.ArrayList;

public class MobUtils {
    public static boolean listContainsMonster(ArrayList<Monster> monsters, Entity entity) {
        if (entity instanceof Monster) {
            return monsters.contains(entity);
        }
        return false;
    }
}
