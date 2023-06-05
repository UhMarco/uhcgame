package me.marco.uhcgame.listeners;

import me.marco.uhcgame.UHCGame;
import org.bukkit.event.Listener;

public abstract class EventListener implements Listener {
    public void register() {
        UHCGame plugin = UHCGame.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
