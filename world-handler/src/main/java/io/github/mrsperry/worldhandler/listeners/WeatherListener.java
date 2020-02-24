package io.github.mrsperry.worldhandler.listeners;

import io.github.mrsperry.worldhandler.worlds.CustomWorld;
import io.github.mrsperry.worldhandler.worlds.WorldHandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherListener implements Listener {
    public static void clearWeather(CustomWorld world) {
        world.getWorld().setStorm(false);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        CustomWorld world = WorldHandler.getWorld(event.getWorld().getName());
        if (world != null && !world.canChangeWeather()) {
            event.setCancelled(event.toWeatherState());
        }
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        CustomWorld world = WorldHandler.getWorld(event.getWorld().getName());
        if (world != null && !world.canChangeWeather()) {
            event.setCancelled(event.toThunderState());
        }
    }
}
