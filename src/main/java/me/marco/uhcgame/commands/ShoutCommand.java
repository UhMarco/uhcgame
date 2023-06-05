package me.marco.uhcgame.commands;

import me.marco.uhcgame.UHCGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ShoutCommand implements CommandExecutor {
    private HashMap<Player, Long> lastMessage;
    private static final long messageCooldown = 30 * 1000;

    public ShoutCommand() {
        lastMessage = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!UHCGame.getInstance().getConfig().getBoolean("uhc-chat")) return false;
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players are able to do this command.");
            return false;
        }
        if (strings.length == 0) return false;
        Player player = (Player) sender;
        if (!player.isOp() && lastMessage.get(player) != null && System.currentTimeMillis() - lastMessage.get(player) < messageCooldown) {
            sender.sendMessage(ChatColor.RED + "You can only shout every 30 seconds.");
            return false;
        }
        lastMessage.put(player, System.currentTimeMillis());
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage(ChatColor.GOLD + "[SHOUT] " + (player.isOp() ? ChatColor.RED : ChatColor.GRAY) + player.getName() + ChatColor.GRAY + ": " + ChatColor.RESET + String.join(" ", strings));
        }
        return true;
    }
}
