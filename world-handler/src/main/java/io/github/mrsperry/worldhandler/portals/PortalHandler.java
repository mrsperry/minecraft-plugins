package io.github.mrsperry.worldhandler.portals;

import io.github.mrsperry.worldhandler.Main;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;

public class PortalHandler {
    /** Map of all schematic files */
    private final static HashMap<String, String> schematics = new HashMap<>();

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
        final String data = PortalSchematic.readPortalFile(name);
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

    public static String getSchematic(String name) {
        return PortalHandler.schematics.getOrDefault(name, null);
    }
}
