package me.marco.uhcgame.commands;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DisqualifyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /disqualify <player>");
            return false;
        }

        GameManager gameManager = UHCGame.getInstance().getGameManager();
        boolean success = gameManager.removePlayer(args[0]);
        if (success) {
            Player player = Bukkit.getPlayer(args[0]);
            if (player != null) {
                gameManager.registerDeath(player.getUniqueId(), player.getInventory().getContents());
                Messenger.messageOperators(sender.getName() + " removed " + player.getName() + " from the game");
            } else {
                OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(args[0]);
                if (oPlayer != null) {
                    gameManager.registerDeath(oPlayer.getUniqueId(), new ItemStack[]{});
                    Messenger.messageOperators(sender.getName() + " removed " + oPlayer.getName() + " from the game");
                } else {
                    Messenger.messageOperators(sender.getName() + " encountered an error trying to revive " + "'" + args[0] + "'");
                    sender.sendMessage(ChatColor.RED + "An error occurred while registering a game death. The scoreboard may not reflect alive teams properly.");
                }
            }
        }
        return success;
    }
}
