package io.github.mrsperry.worldhandler.worlds;

import io.github.mrsperry.mcutils.types.EntityTypes;
import io.github.mrsperry.worldhandler.Main;
import io.github.mrsperry.worldhandler.listeners.EntityListener;
import io.github.mrsperry.worldhandler.listeners.WeatherListener;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class CustomWorld {
    /** The normal world object */
    private World world;
    /** The default game mode */
    private GameMode gameMode = GameMode.SURVIVAL;
    /** The default difficulty */
    private Difficulty difficulty = Difficulty.HARD;
    /** If animals can spawn */
    private boolean animals = true;
    /** If monsters can spawn */
    private boolean monsters = true;
    /** If weather events can occur */
    private boolean weather = true;
    /** If the time of day should be locked */
    private boolean timeLock = true;
    private BukkitTask timeTask = null;

    /**
     * Creates a new custom world
     * @param world The normal world object this world represents
     */
    protected CustomWorld(World world) {
        this.world = world;
    }

    public String getInfo() {
        final String worldType = this.world.getWorldType() == null ? "none" : this.world.getWorldType().toString().toLowerCase();
        final String generator = this.world.getGenerator() == null ? "default" : this.world.getGenerator().toString().toLowerCase();

        return ChatColor.DARK_GRAY + "========== " + ChatColor.AQUA + "Info for " + this.world.getName() + ChatColor.DARK_GRAY + " ==========\n"
            + ChatColor.GRAY + "Environment: " + ChatColor.GREEN + this.world.getEnvironment().toString().toLowerCase() + "\n"
            + ChatColor.GRAY + "World Type: " + ChatColor.GREEN + worldType + "\n"
            + ChatColor.GRAY + "Seed: " + ChatColor.GREEN + this.world.getSeed() + "\n"
            + ChatColor.GRAY + "Structures: " + ChatColor.GREEN + this.world.canGenerateStructures() + "\n"
            + ChatColor.GRAY + "Generator: " + ChatColor.GREEN + generator;
    }

    public String getSettings() {
        return ChatColor.DARK_GRAY + "========== " + ChatColor.AQUA + "Settings for " + this.world.getName() + ChatColor.DARK_GRAY + " ==========\n"
            + ChatColor.GRAY + "Game Mode: " + ChatColor.GREEN + this.gameMode.toString().toLowerCase() + "\n"
            + ChatColor.GRAY + "Difficulty: " + ChatColor.GREEN + this.difficulty.toString().toLowerCase() + "\n"
            + ChatColor.GRAY + "Animals: " + (this.animals ? ChatColor.GREEN : ChatColor.RED) + this.animals + "\n"
            + ChatColor.GRAY + "Monsters: " + (this.monsters ? ChatColor.GREEN : ChatColor.RED) + this.monsters + "\n"
            + ChatColor.GRAY + "Weather: " + (this.weather ? ChatColor.GREEN : ChatColor.RED) + this.weather + "\n"
            + ChatColor.GRAY + "Time Lock: " + (this.timeLock ? ChatColor.GREEN : ChatColor.RED) + this.timeLock;
    }

    public World getWorld() {
        return this.world;
    }

    public GameMode getDefaultGameMode() {
        return this.gameMode;
    }

    public void setDefaultGameMode(GameMode gameMode) {
        this.gameMode = gameMode;

        for (Player player : this.world.getPlayers()) {
            player.setGameMode(gameMode);
        }
    }

    public Difficulty getDefaultDifficulty() {
        return difficulty;
    }

    public void setDefaultDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;

        this.world.setDifficulty(difficulty);
    }

    public boolean canSpawnAnimals() {
        return this.animals;
    }

    public void setCanSpawnAnimals(boolean enabled) {
        this.animals = enabled;

        if (!enabled) {
            EntityListener.clearEntities(this, EntityTypes.getNeutralTypes());
        }
    }

    public boolean canSpawnMonsters() {
        return this.monsters;
    }

    public void setCanSpawnMonsters(boolean enabled) {
        this.monsters = enabled;

        if (!enabled) {
            EntityListener.clearEntities(this, EntityTypes.getHostileTypes());
        }
    }

    public boolean canChangeWeather() {
        return this.weather;
    }

    public void setCanChangeWeather(boolean enabled) {
        this.weather = enabled;

        if (!enabled) {
            WeatherListener.clearWeather(this);
        }
    }

    public boolean getTimeLock() {
        return this.timeLock;
    }

    public void setTimeLock(boolean enabled) {
        this.timeLock = enabled;

        final BukkitScheduler scheduler = Bukkit.getScheduler();
        if (enabled && this.timeTask == null) {
            final World world = this.world;
            this.timeTask = scheduler.runTaskTimer(Main.getInstance(), () -> world.setFullTime(world.getTime() - 60), 60L, 60L);
        } else {
            if (this.timeTask != null) {
                this.timeTask.cancel();
                this.timeTask = null;
            }
        }
    }
}
