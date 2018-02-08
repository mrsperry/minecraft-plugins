package io.github.mrsperry.rifts.rifts;

import org.bukkit.Location;

public interface IRift {
    boolean isValidLocation(Location location);
    void death();
}
