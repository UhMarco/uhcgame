package me.marco.uhcgame.listeners;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.manager.GameState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener extends EventListener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getDisplayName();
        UHCGame plugin = UHCGame.getInstance();
        GameManager gameManager = plugin.getGameManager();

        event.setQuitMessage(ChatColor.YELLOW + playerName + " left");

        if (gameManager.getState() == GameState.WAITING || gameManager.getState() == GameState.STARTING) {
            gameManager.removePlayer(player.getUniqueId());
        } else if (gameManager.isPlaying(player.getUniqueId())) {
            event.setQuitMessage(ChatColor.RED + playerName + " disconnected");
        } else {
            event.setQuitMessage(ChatColor.YELLOW + playerName + " is no longer spectating");
        }
        plugin.updateGlobalScoreboard();
    }
}
