package io.github.mrsperry.worldhandler;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;

import java.util.HashMap;
import java.util.HashSet;

public class WorldHandler {
    private static HashMap<String, CustomWorld> worlds = new HashMap<>();

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
    protected static CustomWorld addWorld(CustomWorld world) {
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
    protected static CustomWorld addWorld(String name) {
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
