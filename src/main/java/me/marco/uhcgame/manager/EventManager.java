package me.marco.uhcgame.manager;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.listeners.*;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class EventManager implements Listener {

    public EventManager() {
        List<EventListener> listeners = new ArrayList<>();
        listeners.add(new PlayerJoinListener());
        listeners.add(new PlayerQuitListener());
        listeners.add(new EntityDamageByEntityListener());
        listeners.add(new EntityHealthListener());
        listeners.add(new PlayerDeathListener());
        listeners.add(new WeatherChangeListener());
        listeners.add(new PlayerInteractListener());
        listeners.add(new BlockListener());
        listeners.add(new PlayerChatListener());
        listeners.forEach(EventListener::register);

        UHCGame plugin = UHCGame.getInstance();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
