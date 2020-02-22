package io.github.mrsperry.worldhandler;

import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;

public class CustomWorld {
    /** The normal world object */
    private World world;
    /** The default game mode */
    private GameMode gameMode;
    /** The default difficulty */
    private Difficulty difficulty;
    /** If animals can spawn */
    private boolean animals;
    /** If monsters can spawn */
    private boolean monsters;
    /** If weather events can occur */
    private boolean weather;
    /** If the time of day should be locked */
    private boolean timeLock;

    /**
     * Creates a new custom world
     * @param world The normal world object this world represents
     */
    protected CustomWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    public GameMode getDefaultGameMode() {
        return this.gameMode;
    }

    public void setDefaultGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Difficulty getDefaultDifficulty() {
        return difficulty;
    }

    public void setDefaultDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean canSpawnAnimals() {
        return this.animals;
    }

    public void setCanSpawnAnimals(boolean enabled) {
        this.animals = enabled;
    }

    public boolean canSpawnMonsters() {
        return this.monsters;
    }

    public void setCanSpawnMonsters(boolean enabled) {
        this.monsters = enabled;
    }

    public boolean canChangeWeather() {
        return this.weather;
    }

    public void setCanChangeWeather(boolean enabled) {
        this.weather = enabled;
    }

    public boolean getTimeLock() {
        return this.timeLock;
    }

    public void setTimeLock(boolean enabled) {
        this.timeLock = enabled;
    }
}
