package me.marco.uhcgame.commands;

import me.marco.uhcgame.UHCGame;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StatusCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.RED + "The current game is in the " + UHCGame.getInstance().getGameManager().getState() + " state.");
        return true;
    }
}
