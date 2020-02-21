package io.github.mrsperry.deathtracker;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeathContext {
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mma");

    private String time;
    private Location location;
    private String cause;

    public DeathContext(final Location location, final String cause) {
        this.time = LocalDateTime.now().format(DeathContext.format);
        this.location = location;
        this.cause = cause;
    }

    public DeathContext(final String time, final Location location, final String cause) {
        this(location, cause);

        this.time = time;
    }

    public String toString() {
        return ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + this.time + ChatColor.DARK_GRAY + "] "
            + ChatColor.GRAY + "("
                + ChatColor.YELLOW + this.location.getBlockX() + ChatColor.GRAY + ", "
                + ChatColor.YELLOW + this.location.getBlockY() + ChatColor.GRAY + ", "
                + ChatColor.YELLOW + this.location.getBlockZ() + ChatColor.GRAY + ") "
            + ChatColor.RED + this.cause;
    }

    public String serialize() {
        return this.time + ";"
            + this.location.getBlockX() + "," + this.location.getBlockY() + "," + this.location.getBlockZ() + ";"
            + this.cause;
    }

    public static DeathContext deserialize(final String context) {
        try {
            final String[] components = context.split(";");
            final String[] coords = components[1].split(",");

            return new DeathContext(components[0],
                new Location(null, Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2])),
                components[2]);
        } catch (Exception ex) {
            Main.getInstance().getLogger().severe("Could not parse death context string: " + context);
            return null;
        }
    }
}
