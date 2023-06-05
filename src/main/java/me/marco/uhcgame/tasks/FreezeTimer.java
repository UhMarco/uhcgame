package me.marco.uhcgame.tasks;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameState;
import me.marco.uhcgame.util.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class FreezeTimer extends BukkitRunnable implements Listener {

    private int timeLeft;

    public FreezeTimer(int timeLeft) {
        this.timeLeft = timeLeft;
        UHCGame.getInstance().getServer().getPluginManager().registerEvents(this, UHCGame.getInstance());
    }

    @Override
    public void run() {
        timeLeft--;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (timeLeft != 0) {
                if (UHCGame.getInstance().getGameManager().getState() == GameState.DEATHMATCH) player.playSound(player.getLocation(), Sound.WOOD_CLICK, 2, 0);
                else if (timeLeft <= 5) player.playSound(player.getLocation(), Sound.WOOD_CLICK, 2, 0);
                new ActionBar(ChatColor.RED + "You can move in " + timeLeft + " second" + (timeLeft == 1 ? "!" : "s!")).sendToPlayer(player);
            } else {
                if (UHCGame.getInstance().getGameManager().getState() == GameState.DEATHMATCH) {
                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 5, 1);
                    new ActionBar(ChatColor.RED + "Deathmatch has begun!").sendToPlayer(player);
                } else {
                    new ActionBar(ChatColor.RED + "The game has begun!").sendToPlayer(player);
                }
            }
        }
        if (timeLeft == 0) {
            cancel();
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (timeLeft <= 0) return;
        if (!UHCGame.getInstance().getGameManager().getPlayers().contains(event.getPlayer().getUniqueId())) return;
        event.setTo(event.getFrom());
    }
}
