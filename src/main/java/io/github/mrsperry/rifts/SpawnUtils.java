package io.github.mrsperry.rifts;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public class SpawnUtils {
    public interface ISpawn {
        void execute(Location location, int count);
    }

    public static void spawn(List<Location> validLocations, int maxCount, ISpawn operation){
        for (int count = 0; count < maxCount; count++) {
            Location location = validLocations.get(Rifts.getRandom().nextInt(validLocations.size()));
            if (location != null) {
                // TODO: spawn correct monster type/amount
                operation.execute(location, count);
            }
        }
    }

    public static Location toLocation(String location, World world) throws Exception {
        String[] locationPart = location.split(",");

        if(locationPart.length != 3) { throw new Exception("Malformed Location String " + location);}

        return new Location(
                world,
                Double.parseDouble(locationPart[0]),
                Double.parseDouble(locationPart[1]),
                Double.parseDouble(locationPart[2])
        );
    }
}
