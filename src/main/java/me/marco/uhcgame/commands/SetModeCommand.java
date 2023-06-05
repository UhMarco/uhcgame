package me.marco.uhcgame.commands;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.manager.Mode;
import me.marco.uhcgame.util.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetModeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /setmode <mode>");
            return false;
        }
        GameManager gameManager = UHCGame.getInstance().getGameManager();
        switch (args[0].toLowerCase()) {
            case "solos":
                gameManager.setMode(Mode.SOLOS);
                Messenger.messageOperators(sender.getName() + " updated the game mode");
                return true;
            case "duos":
                gameManager.setMode(Mode.DUOS);
                Messenger.messageOperators(sender.getName() + "updated the game mode");
                return true;
            case "trios":
                gameManager.setMode(Mode.TRIOS);
                Messenger.messageOperators(sender.getName() + "updated the game mode");
                return true;
            default:
                sender.sendMessage(ChatColor.RED + "Error: Invalid mode specified.");
                return false;
        }
    }
}
