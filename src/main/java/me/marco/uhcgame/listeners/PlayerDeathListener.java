package me.marco.uhcgame.listeners;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.manager.GameState;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerDeathListener extends EventListener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        GameManager gameManager = UHCGame.getInstance().getGameManager();
        GameState state = gameManager.getState();
        if (state == GameState.WAITING || state == GameState.STARTING || state == GameState.RESTARTING) return;

        Player player = event.getEntity();
        if (event.getDeathMessage().contains("shot")) {
            Player shooter = player.getKiller();
            double distance = Math.round(player.getLocation().distance(shooter.getLocation()) * 100.0) / 100.0;
            event.setDeathMessage(event.getDeathMessage() + " (" + distance + " blocks)");
        } else if (event.getDeathMessage().contains("suffocated in a wall")) {
            Location location = event.getEntity().getLocation();
            if (Math.abs(location.getX()) > gameManager.getBorderSize() || Math.abs(location.getZ()) > gameManager.getBorderSize()) {
                event.setDeathMessage(event.getEntity().getName() + " died outside the border");
            }
        }
        event.setDeathMessage(ChatColor.RED + event.getDeathMessage());
        player.getWorld().strikeLightningEffect(player.getLocation());

        gameManager.registerDeath(player.getUniqueId(), player.getInventory().getContents());
        gameManager.removePlayer(player.getUniqueId());
        event.getDrops().add(getPlayerHead(player));
        if (player.getKiller() == null) return;
        gameManager.increaseStat(player.getKiller().getUniqueId());
        UHCGame.getInstance().updateGlobalScoreboard();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        UHCGame plugin = UHCGame.getInstance();
        plugin.updateTabHealth(event.getPlayer(), (int) event.getPlayer().getHealth());
        event.setRespawnLocation(event.getPlayer().getLocation());
    }

    private ItemStack getPlayerHead(Player player) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner(player.getName());
        meta.setDisplayName(ChatColor.RED + player.getName() + "'s head");
        skull.setItemMeta(meta);
        return skull;
    }
}
