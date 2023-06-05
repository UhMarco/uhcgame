package me.marco.uhcgame.tasks;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.manager.GameState;
import me.marco.uhcgame.util.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GraceEndCountdown extends BukkitRunnable {
    private final GameManager gameManager;
    private int timeLeft = 60 * UHCGame.getInstance().getConfig().getInt("grace-period");

    public GraceEndCountdown() {
        gameManager = UHCGame.getInstance().getGameManager();
    }

    @Override
    public void run() {
        timeLeft--;
        if (timeLeft <= 15) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (timeLeft == 0) {
                    new ActionBar(ChatColor.RED + "The grace period has ended!").sendToPlayer(player);
                } else {
                    player.playSound(player.getLocation(), Sound.WOOD_CLICK, 2, 0);
                    new ActionBar(ChatColor.RED + "Grace ends in " + timeLeft + " second" + (timeLeft == 1 ? "!" : "s!")).sendToPlayer(player);
                }
            }
        }
        if (timeLeft == 0) {
            cancel();
            if (gameManager.getState() != GameState.GRACE) return;
            gameManager.setGameState(GameState.PLAYING);
        }
        UHCGame.getInstance().updateGlobalScoreboard();
    }

    public int getSecondsRemaining() {
        return timeLeft;
    }
}
