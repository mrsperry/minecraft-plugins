package io.github.mrsperry.rifts.dungeons;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class MonsterDungeon extends Dungeon implements IDungeon {
    public MonsterDungeon(DungeonConfig config, Player keyholder) {
        super(config, keyholder);
        this.teleport(keyholder);
    }

    public void teleport(Player keyholder) {
        super.teleport(keyholder, ChatColor.GRAY + "Defeat all the monsters to exit the dungeon!");
    }

    public void spawnMonsters() {

    }
}
