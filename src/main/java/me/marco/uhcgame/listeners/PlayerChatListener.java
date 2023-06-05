package me.marco.uhcgame.listeners;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.manager.GameState;
import me.marco.uhcgame.manager.Mode;
import me.marco.uhcgame.manager.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerChatListener extends EventListener {
    private HashMap<Player, Long> lastMessage;
    private static final long messageCooldown = 3 * 1000;

    public PlayerChatListener() {
        lastMessage = new HashMap<>();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!UHCGame.getInstance().getConfig().getBoolean("uhc-chat")) return;

        Player player = event.getPlayer();

        event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        event.setFormat((player.isOp() ? ChatColor.RED : ChatColor.GRAY) + player.getName() + ChatColor.GRAY + ": " + ChatColor.RESET  + event.getMessage());

        GameManager gameManager = UHCGame.getInstance().getGameManager();
        if (gameManager.getMode() == Mode.SOLOS || gameManager.getState() == GameState.WAITING || gameManager.getState() == GameState.STARTING) {
            if (!player.isOp() && lastMessage.get(player) != null && System.currentTimeMillis() - lastMessage.get(player) < messageCooldown) {
                player.sendMessage(ChatColor.RED + "You can only speak every " + messageCooldown / 1000 + " seconds.");
                event.setCancelled(true);
                return;
            }
            lastMessage.put(player, System.currentTimeMillis());
        } else {
            Team team = gameManager.getTeam(player.getUniqueId());
            if (team == null || gameManager.isSpectating(player)) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (gameManager.isSpectating(onlinePlayer)) onlinePlayer.sendMessage(ChatColor.GRAY + "[SPECTATOR] " + event.getFormat());
                }
            } else {
                for (UUID uuid : team.getMembers()) {
                    Player member = Bukkit.getPlayer(uuid);
                    if (member == null) continue;
                    member.sendMessage(ChatColor.GREEN + "[TEAM] " + event.getFormat());
                }
            }
            event.setCancelled(true);

        }
    }
}
