package me.marco.uhcgame.tasks;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.manager.GameState;
import me.marco.uhcgame.util.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathmatchStartCountdown extends BukkitRunnable {
    private final GameManager gameManager;
    private int timeLeft = 60 * UHCGame.getInstance().getConfig().getInt("deathmatch-timer");

    public DeathmatchStartCountdown() {
        gameManager = UHCGame.getInstance().getGameManager();
    }

    @Override
    public void run() {
        timeLeft--;
        if (timeLeft == 0) {
            cancel();
            if (gameManager.getState() != GameState.PLAYING) return;
            gameManager.setGameState(GameState.DEATHMATCH);
        }
        UHCGame.getInstance().updateGlobalScoreboard();
        int border = gameManager.getBorderSize();

        Bukkit.getOnlinePlayers().forEach(player -> {
            Location location = player.getLocation();
            if (Math.abs(Math.abs(location.getX()) - border) <= 30 || Math.abs(Math.abs(location.getZ()) - border) <= 30) {
                player.playSound(location, Sound.WOOD_CLICK, 2, 0);
                new ActionBar(ChatColor.RED + "You are close the border!").sendToPlayer(player);
            }
        });

        if (timeLeft <= 15) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.WOOD_CLICK, 2, 0);
                if (timeLeft == 0) {
                    new ActionBar(ChatColor.RED + "Teleporting...").sendToPlayer(player);
                } else {
                    new ActionBar(ChatColor.RED + "Deathmatch starts in " + timeLeft + " second" + (timeLeft == 1 ? "!" : "s!")).sendToPlayer(player);
                }
            }
        }
    }

    public int getSecondsRemaining() {
        return timeLeft;
    }

    public void setTimeLeft(int value) {
        timeLeft = value;
    }
}
