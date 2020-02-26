package io.github.mrsperry.worldhandler.portals;

import io.github.mrsperry.worldhandler.Main;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.logging.Logger;

public class PortalSchematic {
    private String name;
    private Location activator;
    private Location destination;
    private Location pos1;
    private Location pos2;

    public PortalSchematic() {
        this.name = null;
        this.activator = null;
        this.destination = null;
        this.pos1 = null;
        this.pos2 = null;
    }

    public PortalSchematic(Location activator, Location destination, Location pos1, Location pos2) {
        this.activator = activator;
        this.destination = destination;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    /**
     * Parses a portal schematic file
     * @param fileName The name of the schematic
     * @return The serialized block data of the portal
     */
    public static String readPortalFile(final String fileName) {
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
                logger.severe("An exception occurred while closing file input stream");
                ex1.printStackTrace();
            }
        }
    }

    /**
     * Creates a new portal schematic file
     * @param sender The player who created this portal
     */
    public void createPortalFile(final Player sender) {
        if (this.pos1.getWorld() != null && this.pos2.getWorld() != null) {
            if (this.pos1.getWorld() != this.pos2.getWorld()) {
                sender.sendMessage(ChatColor.RED + "Both positions must be in the same world");
                return;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Both positions must belong to a world");
            return;
        }
        final World world = this.pos1.getWorld();

        int minX, maxX, minY, maxY, minZ, maxZ;

        if (this.pos1.getBlockX() > this.pos2.getBlockX()) {
            minX = this.pos2.getBlockX();
            maxX = this.pos1.getBlockX();
        } else {
            minX = this.pos1.getBlockX();
            maxX = this.pos2.getBlockX();
        }

        if (this.pos1.getBlockY() > this.pos2.getBlockY()) {
            minY = this.pos2.getBlockY();
            maxY = this.pos1.getBlockY();
        } else {
            minY = this.pos1.getBlockY();
            maxY = this.pos2.getBlockY();
        }

        if (this.pos1.getBlockZ() > this.pos2.getBlockZ()) {
            minZ = this.pos2.getBlockZ();
            maxZ = this.pos1.getBlockZ();
        } else {
            minZ = this.pos1.getBlockZ();
            maxZ = this.pos2.getBlockZ();
        }

        final File file = new File(Main.getInstance().getDataFolder().getAbsolutePath() + "\\portals\\" + this.name + ".portalschematic");

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

        sender.sendMessage(ChatColor.GREEN + "Successfully created portal schematic: " + this.name);
    }

    public String getName() {
        return this.name;
    }

    public PortalSchematic setName(String name) {
        this.name = name;
        return this;
    }

    public Location getActivator() {
        return this.activator;
    }

    public PortalSchematic setActivator(Location activator) {
        this.activator = activator;
        return this;
    }

    public Location getDestination() {
        return this.destination;
    }

    public PortalSchematic setDestination(Location destination) {
        this.destination = destination;
        return this;
    }

    public Location getPos1() {
        return this.pos1;
    }

    public PortalSchematic setPos1(Location pos1) {
        this.pos1 = pos1;
        return this;
    }

    public Location getPos2() {
        return this.pos2;
    }

    public PortalSchematic setPos2(Location pos2) {
        this.pos2 = pos2;
        return this;
    }
}
