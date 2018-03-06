package io.github.mrsperry.rifts;

import io.github.mrsperry.rifts.rifts.Rift;
import io.github.mrsperry.rifts.rifts.RiftSize;
import io.github.mrsperry.rifts.rifts.RiftSize.CustomRiftSize;
import io.github.mrsperry.rifts.utils.SpawnUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Timer implements Runnable {
    private int area;
    private int chance;
    private int max;

    public Timer(int area, int chance, int max) {
        this.area = area;
        this.chance = chance;
        this.max = max;
    }

    public void run() {
        Random random = Rifts.getRandom();
        if (RiftManager.getCurrentRiftId() + 1 < this.max) {
            if (Rifts.getRandom().nextInt(100) + 1 <= this.chance) {
                // get a random player
                Collection<? extends Player> players = Bukkit.getOnlinePlayers();
                Player player = (Player) players.toArray()[random.nextInt(players.size())];

                // get a random valid location
                ArrayList<Location> valids = SpawnUtils.getValidLocations(player.getLocation(), this.area, this.area, this.area);
                Location valid = (valids.size() > 0 ? valids.get(Rifts.getRandom().nextInt(valids.size())) : null);



                RiftManager.registerRift(new Rift(valid, RiftSize.getInstance().getRandom()));
            }
        }
    }
}
