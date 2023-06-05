package me.marco.uhcgame.commands;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.util.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetLobbyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players are able to do this command.");
            return false;
        }
        Location location = ((Player) sender).getLocation();
        FileConfiguration config = UHCGame.getInstance().getConfig();

        config.set("lobby-world", location.getWorld().getName());
        config.set("lobby-position.x", location.getBlockX());
        config.set("lobby-position.y", location.getBlockY());
        config.set("lobby-position.z", location.getBlockZ());
        UHCGame.getInstance().saveConfig();

        Messenger.messageOperators(sender.getName() + " updated the lobby position");
        return true;
    }
}
