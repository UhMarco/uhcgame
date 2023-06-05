package me.marco.uhcgame.listeners;

import me.marco.uhcgame.UHCGame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherChangeListener extends EventListener {
    @EventHandler
    public void onChange(WeatherChangeEvent event) {
        if (UHCGame.getInstance().getConfig().getBoolean("allow-weather")) return;
        event.setCancelled(true);
    }
}
