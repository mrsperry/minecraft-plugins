package io.github.mrsperry.rifts.dungeons;

import org.bukkit.entity.Player;

public interface IDungeon {
    void teleport(Player keyholder);
    void spawnMonsters();
}
