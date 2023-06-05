package me.marco.uhcgame.commands;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameState;
import me.marco.uhcgame.util.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EndGraceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GameState gameState = UHCGame.getInstance().getGameManager().getState();
        if (gameState == GameState.GRACE) {
            sender.sendMessage(ChatColor.RED + "Ending grace period...");
            Messenger.messageOperators(sender.getName() + " manually ended the grace period");
            UHCGame.getInstance().getGameManager().setGameState(GameState.PLAYING);
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Grace period is not currently active.");
        return false;
    }
}
