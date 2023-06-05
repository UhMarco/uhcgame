package me.marco.uhcgame.tasks;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.Team;
import me.marco.uhcgame.util.ActionBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class TeammateTracker extends BukkitRunnable {

    @Override
    public void run() {
        List<Team> teams = UHCGame.getInstance().getGameManager().getTeams();
        if (teams.size() == 0) return;

        for (Team team : teams) {
            for (UUID member : team.getMembers()) {
                Player player = Bukkit.getPlayer(member);
                if (player == null) continue;
                Player teammate = Bukkit.getPlayer(team.getTeammate(member));
                String space = "     ";
                if (teammate == null || teammate == player || !UHCGame.getInstance().getGameManager().isPlaying(teammate.getUniqueId())) {
                    if (player.getItemInHand().getType() != Material.COMPASS) continue;
                    new ActionBar(ChatColor.GOLD + "" + ChatColor.BOLD + "Tracking: " + ChatColor.WHITE + "None" + space + ChatColor.GOLD + "" + ChatColor.BOLD + "Distance: " + ChatColor.WHITE + "N/A").sendToPlayer(player);
                    continue;
                }
                player.setCompassTarget(teammate.getLocation());
                if (player.getItemInHand().getType() != Material.COMPASS) continue;
                int distance = (int) Math.round(player.getLocation().distance(teammate.getLocation()));
                new ActionBar( ChatColor.GOLD + "" + ChatColor.BOLD + "Tracking: " + ChatColor.WHITE + teammate.getName() + space +ChatColor.GOLD + "" + ChatColor.BOLD + "Distance: " + ChatColor.WHITE + distance + "m").sendToPlayer(player);
            }
        }
    }
}
