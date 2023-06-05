package me.marco.uhcgame.commands;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players are able to do this command.");
            return false;
        }
        Player player;
        if (args.length == 0) {
            player = (Player) sender;
        } else {
            player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Error: player not found.");
                return false;
            }
        }
        FileConfiguration config = UHCGame.getInstance().getConfigFile();
        Location location = new Location(
                Bukkit.getWorld(config.getString("lobby-world")),
                config.getDouble("lobby-position.x"),
                config.getDouble("lobby-position.y"),
                config.getDouble("lobby-position.z")
        );
        player.teleport(location);
        if (sender == player) {
            Messenger.messageOperators(sender.getName() + " teleported to lobby");
        } else {
            Messenger.messageOperators(sender.getName() + " teleported " + player.getName() + " to lobby");
        }
        return true;
    }
}
