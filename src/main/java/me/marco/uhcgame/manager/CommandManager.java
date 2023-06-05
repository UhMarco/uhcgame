package me.marco.uhcgame.manager;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.commands.*;

public class CommandManager {
    public CommandManager() {
        UHCGame plugin = UHCGame.getInstance();
        plugin.getCommand("uhc").setExecutor(new InfoCommand());
        plugin.getCommand("status").setExecutor(new StatusCommand());
        plugin.getCommand("start").setExecutor(new StartCommand());
        plugin.getCommand("endgrace").setExecutor(new EndGraceCommand());
        plugin.getCommand("setlobby").setExecutor(new SetLobbyCommand());
        plugin.getCommand("disqualify").setExecutor(new DisqualifyCommand());
        plugin.getCommand("world").setExecutor(new WorldCommand());
        plugin.getCommand("lobby").setExecutor(new LobbyCommand());
        plugin.getCommand("revive").setExecutor(new ReviveCommand());
        plugin.getCommand("setstate").setExecutor(new SetStateCommand());
        plugin.getCommand("deathmatch").setExecutor(new DeathmatchCommand());
        plugin.getCommand("setmode").setExecutor(new SetModeCommand());
        plugin.getCommand("party").setExecutor(new PartyCommand());
        plugin.getCommand("shout").setExecutor(new ShoutCommand());
        plugin.getCommand("setdeathmatchtimer").setExecutor(new SetDeathmatchTimerCommand());
    }
}
