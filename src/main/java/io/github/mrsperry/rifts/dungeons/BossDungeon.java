package io.github.mrsperry.rifts.dungeons;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.entity.Player;

public class BossDungeon extends Dungeon implements IDungeon {
    public BossDungeon(DungeonConfig config, Player keyholder) {
        super(config, keyholder);
        this.teleport(keyholder);
    }

    public void teleport(Player keyholder) {
        super.teleport(keyholder, ChatColor.GRAY + "Defeat " + NameUtils.getRandomBossName() + ChatColor.GRAY + " to exit the dungeon!");
    }

    public void spawnMonsters() {

    }
}
