package io.github.mrsperry.worldhandler.portals;

import io.github.mrsperry.worldhandler.Main;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Pattern;

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
     * Constructs a portal from a schematic string
     * @param location The location of the portal origin
     * @param name The name of the schematic to use
     */
    public static void constructPortal(final Location location, final String name) {
        final Logger logger = Main.getInstance().getLogger();

        final String serialized = PortalHandler.getSchematic(name);
        if (serialized == null) {
            logger.severe("Could not find schematic for construction: " + name);
            return;
        }

        final World world = location.getWorld();
        if (world == null) {
            logger.severe("Could not find world for construction: " + location.toString());
            return;
        }

        // Split up each block's data
        final String[] sections = serialized.split(Pattern.quote("|~|"));

        for (final String section : sections) {
            // Separate offset and block data
            final String[] data = section.split(Pattern.quote("|:|"));

            Location offset;
            try {
                // Parse the offset
                final String[] offsetCoords = data[0].split(Pattern.quote(","));
                offset = new Location(world,
                    Integer.parseInt(offsetCoords[0]),
                    Integer.parseInt(offsetCoords[1]),
                    Integer.parseInt(offsetCoords[2]));
            } catch (Exception ex) {
                logger.severe("Could not parse offset coordinates while constructing: " + section);
                return;
            }

            try {
                final Block block = world.getBlockAt(location.clone().subtract(offset));
                block.setBlockData(Bukkit.createBlockData(data[1]));
            } catch (Exception ex) {
                logger.severe("Could not set block data while constructing: " + section);
            }
        }
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
        final World world = this.activator.getWorld();
        if (world == null) {
            sender.sendMessage(ChatColor.RED + "The activator location must belong to a world");
            return;
        }

        if (this.destination.getWorld() != world) {
            sender.sendMessage(ChatColor.RED + "The destination location must belong to the same world as the activator");
            return;
        }

        if (this.pos1.getWorld() != world || this.pos2.getWorld() != world) {
            sender.sendMessage(ChatColor.RED + "Both position locations must belong to the same world as the activator");
            return;
        }

        // Get the min and max coordinates
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

        // Gets the data string of a location
        Function<Location, String> getData = (location) -> {
            final Location distance = this.activator.clone().subtract(location);
            return distance.getBlockX() + ","
                + distance.getBlockY() + ","
                + distance.getBlockZ() + "|:|"
                + world.getBlockAt(location).getBlockData().getAsString() + "|~|";
        };

        final StringBuilder result = new StringBuilder();
        // Set the activator
        result.append(getData.apply(this.activator));
        // Set the destination
        result.append(getData.apply(this.destination));
        // Set all blocks
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    Material type = block.getType();
                    if (type == Material.AIR || type == Material.CAVE_AIR || type == Material.VOID_AIR) {
                        continue;
                    }

                    result.append(getData.apply(block.getLocation()));
                }
            }
        }

        FileOutputStream writer = null;
        try {
            writer = new FileOutputStream(file);
            writer.write(result.toString().substring(0, result.length() - 3).getBytes());
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
