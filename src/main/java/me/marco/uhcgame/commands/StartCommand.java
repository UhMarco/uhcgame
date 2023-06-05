package me.marco.uhcgame.commands;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameState;
import me.marco.uhcgame.util.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GameState gameState = UHCGame.getInstance().getGameManager().getState();
        if (gameState == GameState.WAITING) {
            UHCGame.getInstance().getGameManager().setGameState(GameState.STARTING);
            Messenger.messageOperators(sender.getName() + " manually started the game");
            sender.sendMessage(ChatColor.RED + "Manually starting the game...");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "The game has already started.");
        return false;
    }
}
