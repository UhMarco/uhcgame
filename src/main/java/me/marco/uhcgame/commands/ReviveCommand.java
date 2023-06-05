package me.marco.uhcgame.commands;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.manager.GameState;
import me.marco.uhcgame.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class ReviveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /revive <player>");
            return false;
        }

        GameManager gameManager = UHCGame.getInstance().getGameManager();
        GameState[] states = {GameState.WAITING, GameState.STARTING, GameState.OVER, GameState.RESTARTING};
        if (Arrays.asList(states).contains(gameManager.getState())) {
            sender.sendMessage(ChatColor.RED + "You cannot do this right now.");
            return false;
        }

        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Could not find a player named " + playerName + " on the server.");
            return false;
        }

        UUID uuid = player.getUniqueId();

        if (gameManager.isPlaying(uuid)) {
            sender.sendMessage(ChatColor.RED + player.getName() + " is already in the game.");
            return true;
        }

        gameManager.addPlayer(uuid);
        if (gameManager.hasDied(uuid)) {
            player.getInventory().clear();
            player.getInventory().setContents(gameManager.getDeathInventory(uuid));
            player.updateInventory();
        }
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(40);
        player.setFoodLevel(20);
        String worldName = gameManager.getState() == GameState.DEATHMATCH ? "deathmatch-world" : "game-world";
        player.teleport(Bukkit.getWorld(UHCGame.getInstance().getConfig().getString(worldName)).getSpawnLocation());
        Messenger.messageOperators(sender.getName() + " revived " + player.getName());
        return true;
    }
}
