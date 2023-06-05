package me.marco.uhcgame.listeners;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class BlockListener extends EventListener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() != Material.SKULL && !(UHCGame.getInstance().getGameManager().getState() == GameState.DEATHMATCH || UHCGame.getInstance().getGameManager().getState() == GameState.OVER)) return;
        event.setCancelled(true);
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (UHCGame.getInstance().getGameManager().getState() == GameState.DEATHMATCH || UHCGame.getInstance().getGameManager().getState() == GameState.OVER) {
            event.setCancelled(true);
            return;
        }

        // Increase apple drop chances
//        Block block = event.getBlock();
//        if (block.getTypeId() != 18) return;
//        if (block.getDrops().stream().anyMatch(i -> i.getType() == Material.APPLE)) return;
//        if (new Random().nextInt(100) > 1) return;
//        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.APPLE, 1));
    }
}
