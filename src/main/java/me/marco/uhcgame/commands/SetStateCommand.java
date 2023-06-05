package me.marco.uhcgame.commands;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.manager.GameState;
import me.marco.uhcgame.util.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetStateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /setstate <state>");
            return false;
        }
        String state = args[0].toLowerCase();
        GameManager gameManager = UHCGame.getInstance().getGameManager();
        switch(state) {
            case "waiting":
                gameManager.setGameState(GameState.WAITING);
                break;
            case "starting":
                gameManager.setGameState(GameState.STARTING);
                break;
            case "grace":
                gameManager.setGameState(GameState.GRACE);
                break;
            case "playing":
                gameManager.setGameState(GameState.PLAYING);
                break;
            case "deathmatch":
                gameManager.setGameState(GameState.DEATHMATCH);
                break;
            case "over":
                gameManager.setGameState(GameState.OVER);
                break;
            case "restarting":
                gameManager.setGameState(GameState.RESTARTING);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Error: Unrecognised state.");
                return false;
        }
        Messenger.messageOperators(sender.getName() + " updated the game state to " + args[0].toUpperCase());
        return true;
    }
}
