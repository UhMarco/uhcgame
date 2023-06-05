package me.marco.uhcgame.listeners;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.manager.GameState;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerJoinListener extends EventListener {

    UHCGame plugin = UHCGame.getInstance();
    GameManager gameManager = UHCGame.getInstance().getGameManager();
    FileConfiguration config = plugin.getConfigFile();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getDisplayName();

        plugin.addToScoreboard(player);

        event.setJoinMessage(ChatColor.YELLOW + playerName + " joined");

        if (gameManager.getState() == GameState.WAITING) {
            gameManager.addPlayer(player.getUniqueId());

            plugin.configurePlayer(event.getPlayer());
            player.setGameMode(GameMode.SURVIVAL);
            for (PotionEffect effect : player.getActivePotionEffects ()){
                player.removePotionEffect(effect.getType());
            }

            if (Bukkit.getOnlinePlayers().size() == config.getInt("starting-players")) {
                gameManager.setGameState(GameState.STARTING);
            }
        }

        if (gameManager.getState() != GameState.WAITING && gameManager.getState() != GameState.STARTING) {
            if (gameManager.isPlaying(player.getUniqueId())) {
                event.setJoinMessage(ChatColor.RED + playerName + " reconnected");
            } else {
                event.setJoinMessage(null);
                World uhcWorld = Bukkit.getWorld(config.getString("game-world"));
                if (player.getWorld() != uhcWorld) player.teleport(uhcWorld.getSpawnLocation());
                player.setGameMode(GameMode.SPECTATOR);
                event.setJoinMessage(ChatColor.YELLOW + playerName + " is now spectating");
            }
        }

        plugin.updatePlayerScoreBoards();
        plugin.updateGlobalScoreboard();
    }

    @EventHandler
    public void onSpawn(PlayerSpawnLocationEvent event) {
        GameState state = UHCGame.getInstance().getGameManager().getState();
        if (state == GameState.WAITING || state == GameState.STARTING) {
            FileConfiguration config = UHCGame.getInstance().getConfigFile();
            Location location = new Location(
                    Bukkit.getWorld(config.getString("lobby-world")),
                    config.getDouble("lobby-position.x"),
                    config.getDouble("lobby-position.y"),
                    config.getDouble("lobby-position.z")
            );
            event.setSpawnLocation(location);
        } else {
            if (!gameManager.isPlaying(event.getPlayer().getUniqueId())) {
                World world = Bukkit.getWorld(config.getString(state == GameState.DEATHMATCH ? "deathmatch-world" : "game-world"));
                if (event.getSpawnLocation().getWorld() != world) event.setSpawnLocation(world.getSpawnLocation());
            }
        }

    }
}
