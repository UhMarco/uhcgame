package me.marco.uhcgame.listeners;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.manager.GameState;
import me.marco.uhcgame.manager.Mode;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener extends EventListener {
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Entity damagingEntity = event.getDamager();

        if (damagingEntity instanceof Player || damagingEntity instanceof Arrow || damagingEntity instanceof FishHook) {
            GameState[] blockedStates = {GameState.WAITING, GameState.STARTING, GameState.GRACE, GameState.OVER};

            for (GameState state : blockedStates) {
                if (state != UHCGame.getInstance().getGameManager().getState()) continue;

                event.setCancelled(true);

                if (state != GameState.GRACE) return;

                if (damagingEntity instanceof Player) {
                    if (event.getEntity() == damagingEntity) return;
                    sendMessage((Player) damagingEntity);
                } else {
                    Projectile projectile = (Projectile) damagingEntity;
                    if (!(projectile.getShooter() instanceof Player) || event.getEntity() == projectile.getShooter()) return;
                    sendMessage((Player) projectile.getShooter());
                }
                return;
            }

            // if not in blocked states
            GameManager gameManager = UHCGame.getInstance().getGameManager();
            if (gameManager.getMode() != Mode.SOLOS && event.getDamager() instanceof Player && gameManager.sameTeam(event.getEntity().getUniqueId(), event.getDamager().getUniqueId())) {
                event.setCancelled(true);
                event.getDamager().sendMessage(ChatColor.RED + "You cannot damage your teammates.");
            }

            if (!(damagingEntity instanceof Projectile)) return;
            if (!((((Projectile) damagingEntity).getShooter()) instanceof Player)) return;
            Player shooter = (Player) ((Projectile) damagingEntity).getShooter();
            Player player = (Player) event.getEntity();
            if (gameManager.getMode() != Mode.SOLOS && gameManager.sameTeam(player.getUniqueId(), shooter.getUniqueId())) {
                event.setCancelled(true);
                shooter.sendMessage(ChatColor.RED + "You cannot damage your teammates.");
                damagingEntity.remove();
                return;
            }
            double health = player.getHealth() - event.getFinalDamage();
            if (health <= 0) return;
            shooter.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + player.getDisplayName() + " &eis at &c" + (int) health + " &eHP"));
        }
    }

    private void sendMessage(Player recipient) {
        recipient.sendMessage(ChatColor.RED + "You cannot damage other players during the grace period.");
    }
}
