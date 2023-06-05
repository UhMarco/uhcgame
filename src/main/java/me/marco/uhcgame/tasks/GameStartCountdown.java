package me.marco.uhcgame.tasks;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.manager.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStartCountdown extends BukkitRunnable {
    private final GameManager gameManager;
    private int timeLeft = 5;

    public GameStartCountdown() {
        gameManager = UHCGame.getInstance().getGameManager();
    }

    @Override
    public void run() {
        if (timeLeft != 0) Bukkit.broadcastMessage(ChatColor.YELLOW + "Game starting in " + timeLeft);
        else {
            cancel();
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Game starting...");
            gameManager.setGameState(GameState.GRACE);
            return;
        }
        timeLeft--;
    }
}
