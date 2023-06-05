package me.marco.uhcgame.tasks;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameState;
import me.marco.uhcgame.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HardWorldBorder extends BukkitRunnable {
    @Override
    public void run() {
        GameState state = UHCGame.getInstance().getGameManager().getState();
        if (state == GameState.WAITING || state == GameState.STARTING) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SURVIVAL) return;
            Location location = player.getLocation();
            int border = UHCGame.getInstance().getGameManager().getBorderSize() + 50;
            if (Math.abs(Math.abs(location.getX())) > border || Math.abs(Math.abs(location.getZ())) > border) {
                player.teleport(player.getWorld().getSpawnLocation());
                Messenger.messageOperators(player.getName() + " was spectating too far outside the border");
            }
        }
    }
}
