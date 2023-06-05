package me.marco.uhcgame.listeners;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.manager.GameState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffectType;


public class EntityHealthListener extends EventListener {
    @EventHandler
    public void onHealthLoss(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        GameManager gameManager = UHCGame.getInstance().getGameManager();
        GameState state = gameManager.getState();
        if (state == GameState.OVER) {
            event.setCancelled(true);
            return;
        } else if (state == GameState.GRACE && player.hasPotionEffect(PotionEffectType.ABSORPTION) && event.getFinalDamage() > 0) {
            player.removePotionEffect(PotionEffectType.ABSORPTION);
        }

        handle(event.getEntity(), -event.getFinalDamage());
    }

    @EventHandler
    public void onHealthGain(EntityRegainHealthEvent event) {
        handle(event.getEntity(), event.getAmount());
    }

    public void handle(Entity entity, double difference) {
        if (!(entity instanceof Player)) return;
        GameState state = UHCGame.getInstance().getGameManager().getState();
        if (state != GameState.PLAYING && state != GameState.DEATHMATCH) return;
        Player player = (Player) entity;
        int health = (int) (player.getHealth() + difference);
        UHCGame.getInstance().updateTabHealth(player, health);
    }
}
