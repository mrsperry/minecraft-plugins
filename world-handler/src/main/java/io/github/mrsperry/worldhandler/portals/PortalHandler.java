package io.github.mrsperry.worldhandler.portals;

import io.github.mrsperry.worldhandler.Main;

import javafx.util.Pair;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;

public class PortalHandler {
    /** Map of all schematic files */
    final static HashMap<String, String> schematics = new HashMap<>();

    /**
     * Loads all portal schematics from file
     */
    public static void reloadPortals() {
        final Main instance = Main.getInstance();
        final Logger logger = instance.getLogger();

        final File portals = new File(instance.getDataFolder().getAbsolutePath() + "\\portals");
        if (!portals.isDirectory()) {
            if (!portals.mkdir()) {
                logger.severe("Could not create portals folder");
            }
        }

        try {
            for (final String name : Objects.requireNonNull(portals.list())) {
                if (PortalHandler.registerPortal(name)) {
                    logger.info("Loaded portal schematic: " + name);
                } else {
                    logger.severe("Could not load portal schematic: " + name);
                }
            }
        } catch (final NullPointerException ex) {
            logger.severe("An error occurred while loading portal schematics");
            ex.printStackTrace();
        }
    }

    /**
     * Reads a portal schematic from file and loads it
     * @param name The name of the schematic
     * @return If the schematic was registered
     */
    public static boolean registerPortal(final String name) {
        final String data = PortalHandler.readPortalFile(name);
        if (data != null) {
            PortalHandler.schematics.put(name, data);
            return true;
        }

        return false;
    }

    /**
     * Reads a collection of portal schematics from file and loads them
     * @param names The names of the schematics
     * @return If all schematics were registered
     */
    public static boolean registerPortals(final Collection<String> names) {
        boolean success = true;
        for (final String name : names) {
            if (!PortalHandler.registerPortal(name)) {
                success = false;
            }
        }

        return success;
    }

    /**
     * Parses a portal schematic file
     * @param fileName The name of the schematic
     * @return The serialized block data of the portal
     */
    private static String readPortalFile(final String fileName) {
        final Main instance = Main.getInstance();
        final Logger logger = instance.getLogger();
        final File file = new File(instance.getDataFolder().getAbsolutePath() + "\\portals\\" + fileName);

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            return new String(stream.readAllBytes());
        } catch (final FileNotFoundException ex) {
            instance.getLogger().severe("Could not find portal schematic file: " + fileName);
            return null;
        } catch (final IOException ex) {
            logger.severe("An exception occurred while reading schematic");
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException ex1) {
                logger.severe("An exception occured while closing file input stream");
                ex1.printStackTrace();
            }
        }
    }

    /**
     * Creates a new portal schematic file
     * @param sender The player who created this portal
     * @param name The name of the schematic
     * @param locations The block locations of the portal
     */
    protected static void createPortalFile(final Player sender, final String name, final Pair<Location, Location> locations) {
        final Location pos1 = locations.getKey();
        final Location pos2 = locations.getValue();

        if (pos1.getWorld() != null && pos2.getWorld() != null) {
            if (pos1.getWorld() != pos2.getWorld()) {
                sender.sendMessage(ChatColor.RED + "Both positions must be in the same world");
                return;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Both positions must belong to a world");
            return;
        }
        final World world = pos1.getWorld();

        int minX, maxX, minY, maxY, minZ, maxZ;

        if (pos1.getBlockX() > pos2.getBlockX()) {
            minX = pos2.getBlockX();
            maxX = pos1.getBlockX();
        } else {
            minX = pos1.getBlockX();
            maxX = pos2.getBlockX();
        }

        if (pos1.getBlockY() > pos2.getBlockY()) {
            minY = pos2.getBlockY();
            maxY = pos1.getBlockY();
        } else {
            minY = pos1.getBlockY();
            maxY = pos2.getBlockY();
        }

        if (pos1.getBlockZ() > pos2.getBlockZ()) {
            minZ = pos2.getBlockZ();
            maxZ = pos1.getBlockZ();
        } else {
            minZ = pos1.getBlockZ();
            maxZ = pos2.getBlockZ();
        }

        final File file = new File(Main.getInstance().getDataFolder().getAbsolutePath() + "\\portals\\" + name + ".portalschematic");

        final StringBuilder result = new StringBuilder();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    result.append(world.getBlockAt(x, y, z).getBlockData().getAsString()).append("DELIMITER");
                }
            }
        }

        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(file);
            writer.write(result.toString().getBytes());
            writer.flush();
        } catch (IOException ex) {
            sender.sendMessage(ChatColor.RED + "Could not write to file; portal schematic was not created");
            ex.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        sender.sendMessage(ChatColor.GREEN + "Successfully created portal schematic: " + name);
    }
}
