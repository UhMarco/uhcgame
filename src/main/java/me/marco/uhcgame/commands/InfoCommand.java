package me.marco.uhcgame.commands;

import me.marco.uhcgame.UHCGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class InfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                "&7&m--------------------\n" +
                        "&6&l              UHC\n" +
                        "&7 Version: " + UHCGame.getInstance().getDescription().getVersion() + "\n" +
                        "&7 By Ashlan\n" +
                        "&7&m--------------------"
        ));
        return true;
    }
}
