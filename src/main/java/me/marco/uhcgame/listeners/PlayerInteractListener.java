package me.marco.uhcgame.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerInteractListener extends EventListener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;
        ItemStack item = event.getPlayer().getItemInHand();
        if (item == null) return;
        if (item.getItemMeta() == null) return;
        if (item.getItemMeta().getDisplayName() == null) return;
        if (item.getType() != Material.SKULL_ITEM || !item.getItemMeta().getDisplayName().contains("'s head")) return;

        Player player = event.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.SPEED)) player.removePotionEffect(PotionEffectType.SPEED);
        if (player.hasPotionEffect(PotionEffectType.REGENERATION)) player.removePotionEffect(PotionEffectType.REGENERATION);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 20, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 4, 2));

        player.sendMessage(ChatColor.GREEN + "You ate a player head and received 4 seconds of Regeneration III and 20 seconds of Speed II!");
        // player.getInventory().remove(item);
        player.setItemInHand(null);
    }
}
