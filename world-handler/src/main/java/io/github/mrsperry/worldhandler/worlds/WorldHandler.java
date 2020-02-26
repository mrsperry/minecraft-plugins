package io.github.mrsperry.worldhandler.worlds;

import io.github.mrsperry.worldhandler.Main;

import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

public class WorldHandler {
    private final static HashMap<String, CustomWorld> worlds = new HashMap<>();

    /**
     * Loads all world settings from a config file
     * @param config The config file
     */
    public static void initialize(final FileConfiguration config) {
        final Logger logger = Main.getInstance().getLogger();
        final ConfigurationSection worlds = config.getConfigurationSection("worlds");
        if (worlds == null) {
            logger.info("No world settings were found in the config file");
            return;
        }

        logger.info("Loading world settings from config...");
        for (final String key : worlds.getKeys(false)) {
            final String path = "worlds." + key + ".";
            final World world = Bukkit.getWorld(key);
            if (world == null) {
                logger.severe("Could not find world: " + key);
                continue;
            }

            final CustomWorld customWorld = new CustomWorld(world);
            try {
                customWorld.setDefaultGameMode(GameMode.valueOf(config.getString(path + "gamemode")));
            } catch (final Exception ex) {
                logger.severe("Could not parse game mode for: " + key);
            }
            try {
                customWorld.setDefaultDifficulty(Difficulty.valueOf(config.getString(path + "difficulty")));
            } catch (final Exception ex) {
                logger.severe("Could not parse difficulty for: " + key);
            }
            customWorld.setCanSpawnAnimals(config.getBoolean(path + "animals"));
            customWorld.setCanSpawnMonsters(config.getBoolean(path + "monsters"));
            customWorld.setCanChangeWeather(config.getBoolean(path + "weather"));
            customWorld.setTimeLock(config.getBoolean(path + "timelock"));

            WorldHandler.worlds.put(world.getName(), customWorld);
        }
    }

    public static void save(final FileConfiguration config) {
        for (final String key : WorldHandler.worlds.keySet()) {
            final String path = "worlds." + key + ".";
            final CustomWorld world = WorldHandler.worlds.get(key);

            config.set(path + "gamemode", world.getDefaultGameMode().toString().toLowerCase());
            config.set(path + "difficulty", world.getDefaultDifficulty().toString().toLowerCase());
            config.set(path + "animals", world.canSpawnAnimals());
            config.set(path + "monsters", world.canSpawnMonsters());
            config.set(path + "weather", world.canChangeWeather());
            config.set(path + "timelock", world.getTimeLock());
        }
    }

    /**
     * Creates a new custom world with the specified settings
     * @param name The world's name
     * @return The custom world
     */
    public static CustomWorld createWorld(final String name) {
       return WorldHandler.createWorld(name, World.Environment.NORMAL);
    }

    /**
     * Creates a new custom world with the specified settings
     * @param name The world's name
     * @param environment The world's environment
     * @return The custom world
     */
    public static CustomWorld createWorld(final String name, final World.Environment environment) {
        return WorldHandler.createWorld(name, environment, WorldType.NORMAL);
    }

    /**
     * Creates a new custom world with the specified settings
     * @param name The world's name
     * @param environment The world's environment
     * @param type The world's type
     * @return The custom world
     */
    public static CustomWorld createWorld(final String name, final World.Environment environment, final WorldType type) {
        return WorldHandler.createWorld(name, environment, type, true);
    }

    /**
     * Creates a new custom world with the specified settings
     * @param name The world's name
     * @param environment The world's environment
     * @param type The world's type
     * @param generateStructures If structures should be generated
     * @return The custom world
     */
    public static CustomWorld createWorld(final String name, final World.Environment environment, final WorldType type, final boolean generateStructures) {
        return WorldHandler.createWorld(name, environment, type, generateStructures, Main.getInstance().getRandom().nextLong());
    }

    /**
     * Creates a new custom world with the specified settings
     * @param name The world's name
     * @param environment The world's environment
     * @param type The world's type
     * @param generateStructures If structures should be generated
     * @param seed The world's seed
     * @return The custom world
     */
    public static CustomWorld createWorld(final String name, final World.Environment environment, final WorldType type, final boolean generateStructures, final long seed) {
        return WorldHandler.createWorld(name, environment, type, generateStructures, seed, "null");
    }

    /**
     * Creates a new custom world with the specified settings
     * @param name The world's name
     * @param environment The world's environment
     * @param type The world's type
     * @param generateStructures If structures should be generated
     * @param seed The world's seed
     * @param generator The world's custom generator
     * @return The custom world
     */
    public static CustomWorld createWorld(final String name, final World.Environment environment, final WorldType type, final boolean generateStructures, final long seed, String generator) {
        return WorldHandler.addWorld(new CustomWorld(new WorldCreator(name)
            .environment(environment)
            .type(type)
            .generateStructures(generateStructures)
            .seed(seed)
            .generator(generator)
            .createWorld()));
    }

    /**
     * Creates a new custom world with the specified settings
     * @param name The world's name
     * @param environment The world's environment
     * @param type The world's type
     * @param generateStructures If structures should be generated
     * @param seed The world's seed
     * @param generator The world's custom generator
     * @return The custom world
     */
    public static CustomWorld createWorld(final String name, final World.Environment environment, final WorldType type, final boolean generateStructures, final long seed, ChunkGenerator generator) {
        return WorldHandler.addWorld(new CustomWorld(new WorldCreator(name)
            .environment(environment)
            .type(type)
            .generateStructures(generateStructures)
            .seed(seed)
            .generator(generator)
            .createWorld()));
    }

    /**
     * Adds a custom world to be handled
     * @param world The custom world to add
     * @return The custom world that was added or null if another world already exists with the same name
     */
    public static CustomWorld addWorld(CustomWorld world) {
        final String name = world.getWorld().getName();
        if (WorldHandler.worlds.containsKey(name)) {
            return null;
        }

        WorldHandler.worlds.put(name, world);
        return world;
    }

    /**
     * Adds a normal world to be handled and converted into a custom world
     * @param name The normal world's name
     * @return The custom world that was added or null if the world could not be found or another world exists with the same name
     */
    public static CustomWorld addWorld(String name) {
        final World world = Bukkit.getWorld(name);
        if (world == null || WorldHandler.worlds.containsKey(name)) {
            return null;
        }

        final CustomWorld customWorld = new CustomWorld(world);
        WorldHandler.worlds.put(name, customWorld);

        return customWorld;
    }

    /**
     * Removes a world from the handler list
     * @param world The the custom world to remove
     * @return If the world was removed
     */
    public static boolean removeWorld(CustomWorld world) {
        return WorldHandler.removeWorld(world.getWorld().getName());
    }

    /**
     * Removes a world from the handler list
     * @param name The name of the custom world to remove
     * @return If the world was removed
     */
    public static boolean removeWorld(String name) {
        if (WorldHandler.worlds.containsKey(name)) {
            WorldHandler.worlds.remove(name);
            return true;
        }

        return false;
    }

    /**
     * Gets a custom world
     * @param name The name of the custom world
     * @return The custom world or null if the world could not be found
     */
    public static CustomWorld getWorld(String name) {
        return WorldHandler.worlds.getOrDefault(name, null);
    }

    /**
     * Gets all loaded custom worlds
     * @return The list of all handled custom worlds
     */
    public static HashSet<CustomWorld> getWorlds() {
        return new HashSet<>(WorldHandler.worlds.values());
    }
}
