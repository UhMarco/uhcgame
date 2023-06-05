package me.marco.uhcgame.commands;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.manager.GameState;
import me.marco.uhcgame.util.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetDeathmatchTimerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /timer <seconds>");
            return false;
        }
        GameManager gameManager = UHCGame.getInstance().getGameManager();
        if (gameManager.getState() != GameState.PLAYING) {
            sender.sendMessage(ChatColor.RED + "You can't do this right now.");
            return false;
        }
        try {
            gameManager.setDeathmatchTimer(Integer.parseInt(args[0]));
            sender.sendMessage(ChatColor.RED + "Deathmatch timer updated.");
            Messenger.messageOperators(sender.getName() + " set the deathmatch timer to " + args[0] + " seconds");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Error: Invalid number of seconds.");
            return false;
        }
        return false;
    }
}
