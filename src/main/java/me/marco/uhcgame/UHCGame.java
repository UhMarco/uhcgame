package me.marco.uhcgame;

import dev.jcsoftware.jscoreboards.JPerPlayerScoreboard;
import me.marco.uhcgame.listeners.gamemodes.CutCleanGameMode;
import me.marco.uhcgame.manager.*;
import me.marco.uhcgame.tasks.HardWorldBorder;
import me.marco.uhcgame.tasks.TeammateTracker;
import me.marco.uhcgame.util.Util;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.UUID;

public final class UHCGame extends JavaPlugin implements Listener {

    private GameManager gameManager;
    private ConfigManager configManager;

    private static UHCGame instance;

    private final FileConfiguration config = getConfig();

    public FileConfiguration getConfigFile() {
        return config;
    }

    private String formatTeammateForScoreboard(Player player) {
        UUID uuid = player.getUniqueId();
        UUID teammate = gameManager.getTeammate(uuid);
        if (teammate == null || teammate == uuid) return "None";
        StringBuilder message = new StringBuilder();
        if (gameManager.hasDied(teammate)) message.append("&m");
        message.append(Util.getPlayerName(teammate));
        return message.toString();
    }

    private final JPerPlayerScoreboard globalScoreboard = new JPerPlayerScoreboard(
            (player) -> "&6&l" + config.getString("title"),
            (player) -> {
                int timeLeft;
                String countdown;
                String mode = gameManager.getMode().toString().substring(0, 1).toUpperCase() + gameManager.getMode().toString().substring(1).toLowerCase();
                switch (gameManager.getState()) {
                    case WAITING:
                    case STARTING:
                        return mode.equals("Solos") ? Arrays.asList(
                                "",
                                "&fMode: &7" + mode,
                                "&fPlayers: &7" + gameManager.getPlayersRemaining() + "/" + config.getInt("max-players"),
                                "",
                                gameManager.getState() == GameState.WAITING ? "Waiting..." : "Starting...",
                                "",
                                "&6" + config.getString("footer")
                        ) : Arrays.asList(
                                "",
                                "&fMode: &7" + mode,
                                "&fPlayers: &7" + gameManager.getPlayersRemaining() + "/" + config.getInt("max-players"),
                                "",
                                gameManager.getState() == GameState.WAITING ? "Waiting..." : "Starting...",
                                "",
                                "&fTeammate:",
                                "&7" + (gameManager.getTeammate(player.getUniqueId()) != null ? Util.getPlayerName(gameManager.getTeammate(player.getUniqueId())) : "Random"),
                                "",
                                "&6" + config.getString("footer")
                        );
                    case GRACE:
                        timeLeft = gameManager.getGraceRemaining();
                        countdown = timeLeft > 60
                                ? (int) Math.ceil(timeLeft / 60f) + " minutes"
                                : timeLeft + (timeLeft > 1 ? " seconds" : " second");
                        return mode.equals("Solos") ? Arrays.asList(
                                "",
                                "&fGrace ends:",
                                "&7" + countdown,
                                "",
                                "&fPlayers: &7" + gameManager.getPlayersRemaining(),
                                "",
                                "&fKills: &7" + gameManager.getKills(player.getUniqueId()),
                                "",
                                "&fBorder: &7" + gameManager.getBorderSize(),
                                "",
                                "&6" + config.getString("footer")
                        ) : Arrays.asList(
                                "",
                                "&fGrace ends:",
                                "&7" + countdown,
                                "",
                                "&fTeams: &7" + gameManager.getTeamsRemaining(),
                                "&fPlayers: &7" + gameManager.getPlayersRemaining(),
                                "",
                                "&fTeammate:",
                                "&7" + formatTeammateForScoreboard(player),
                                "",
                                "&fKills: &7" + gameManager.getKills(player.getUniqueId()),
                                "&fTeam kills: &7" + gameManager.getTeamKills(player),
                                "",
                                "&fBorder: &7" + gameManager.getBorderSize(),
                                "",
                                "&6" + config.getString("footer")
                        );
                    case PLAYING:
                        timeLeft = gameManager.getDeathmatchCountdownRemaining();
                        countdown = timeLeft > 60
                                ? (int) Math.ceil(timeLeft / 60f) + " minutes"
                                : timeLeft + (timeLeft > 1 ? " seconds" : " second");

                        return mode.equals("Solos") ? Arrays.asList(
                                "",
                                "&fDeathmatch in:",
                                "&7" + countdown,
                                "",
                                "&fPlayers: &7" + gameManager.getPlayersRemaining(),
                                "",
                                "&fKills: &7" + gameManager.getKills(player.getUniqueId()),
                                "",
                                "&fBorder: &7" + gameManager.getBorderSize(),
                                "",
                                "&6" + config.getString("footer")
                        ) : Arrays.asList(
                                "",
                                "&fDeathmatch in:",
                                "&7" + countdown,
                                "",
                                "&fTeams: &7" + gameManager.getTeamsRemaining(),
                                "&fPlayers: &7" + gameManager.getPlayersRemaining(),
                                "",
                                "&fTeammate:",
                                "&7" + formatTeammateForScoreboard(player),
                                "",
                                "&fKills: &7" + gameManager.getKills(player.getUniqueId()),
                                "&fTeam kills: &7" + gameManager.getTeamKills(player),
                                "",
                                "&fBorder: &7" + gameManager.getBorderSize(),
                                "",
                                "&6" + config.getString("footer")
                        );
                    case DEATHMATCH:
                        return mode.equals("Solos") ? Arrays.asList(
                                "",
                                "&fPlayers: &7" + gameManager.getPlayersRemaining(),
                                "",
                                "&fKills: &7" + gameManager.getKills(player.getUniqueId()),
                                "",
                                "&fBorder: &7N/A",
                                "",
                                "&6" + config.getString("footer")
                        ) : Arrays.asList(
                                "",
                                "&fTeams: &7" + gameManager.getTeamsRemaining(),
                                "&fPlayers: &7" + gameManager.getPlayersRemaining(),
                                "",
                                "&fTeammate:",
                                "&7" + formatTeammateForScoreboard(player),
                                "",
                                "&fKills: &7" + gameManager.getKills(player.getUniqueId()),
                                "&fTeam kills: &7" + gameManager.getTeamKills(player),
                                "",
                                "&fBorder: &7" + gameManager.getBorderSize(),
                                "",
                                "&6" + config.getString("footer")
                        );
                    case OVER:
                        return mode.equals("Solos") ? Arrays.asList(
                                "",
                                "&fKills: &7" + gameManager.getKills(player.getUniqueId()),
                                "",
                                "&fWinner:",
                                "&7" + gameManager.getWinner().getName(),
                                "",
                                "&fThanks for",
                                "&fplaying!",
                                "",
                                "&6" + config.getString("footer")
                        ) : Arrays.asList(
                                "",
                                "&fTeammate:",
                                "&7" + formatTeammateForScoreboard(player),
                                "",
                                "&fKills: &7" + gameManager.getKills(player.getUniqueId()),
                                "&fTeam kills: &7" + gameManager.getTeamKills(player),
                                "",
                                "&fWinners:",
                                "&7" + gameManager.getWinners(),
                                "",
                                "&fThanks for",
                                "&fplaying!",
                                "",
                                "&6" + config.getString("footer")
                        );
                    default:
                        return Arrays.asList(
                                "",
                                "&cScoreboard",
                                "&cError",
                                "",
                                "&6" + config.getString("footer")
                        );
                }

            }
    );

    @Override
    public void onEnable() {
        Bukkit.createWorld(new WorldCreator(config.getString("game-world")));
        Bukkit.createWorld(new WorldCreator(config.getString("lobby-world")));
        Bukkit.createWorld(new WorldCreator(config.getString("deathmatch-world")));

        instance = this;

        config.options().copyDefaults(true);
        saveConfig();

        gameManager = new GameManager(this);
        configManager = new ConfigManager();
        new CommandManager();
        new EventManager();
        // getServer().getPluginManager().registerEvents(new RecipeManager(), this);

        World world = Bukkit.getWorld(config.getString("game-world"));
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("naturalRegeneration", "false");
        world.setTime(6000);
        getLogger().info("Set world game rules and time (" + world.getName() + ")");

        Bukkit.getOnlinePlayers().forEach(this::addToScoreboard);
        Bukkit.getOnlinePlayers().forEach(this::configurePlayer);
        updatePlayerScoreBoards();
        updateGlobalScoreboard();

        new TeammateTracker().runTaskTimer(this, 0, 10);
        new HardWorldBorder().runTaskTimer(this, 0, 100);

        // Gamemode
        getServer().getPluginManager().registerEvents(new CutCleanGameMode(), this);
    }

    public void addToScoreboard(Player player) {
        globalScoreboard.addPlayer(player);
        globalScoreboard.updateScoreboard();

        Scoreboard playerScoreboard = player.getScoreboard();
        Objective healthCheck = playerScoreboard.getObjective("health");
        Objective objective = healthCheck != null ? healthCheck : playerScoreboard.registerNewObjective("health", "dummy");
        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        Bukkit.getOnlinePlayers().forEach(p -> {
            objective.getScore(p).setScore((int) p.getHealth());
        });
    }

    public void configurePlayer(Player player) {
        player.setMaxHealth(40);
        player.setHealth(40);
        player.setFoodLevel(20);
        updateTabHealth(player, (int) player.getHealth());

        gameManager.addPlayer(player.getUniqueId());
    }

    public void updateTabHealth(Player playerToUpdate, int newHealth) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard playerScoreboard = player.getScoreboard();
            Objective healthCheck = playerScoreboard.getObjective("health");
            Objective objective = healthCheck != null ? healthCheck : playerScoreboard.registerNewObjective("health", "dummy");
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (p != playerToUpdate) {
                    objective.getScore(p).setScore((int) p.getHealth());
                } else {
                    objective.getScore(p).setScore(newHealth);
                }
            });
        }
    }

    public void updatePlayerScoreBoards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard playerScoreboard = player.getScoreboard();

            Team selfCheck = playerScoreboard.getTeam("aSelf");
            Team self = selfCheck != null ? selfCheck : playerScoreboard.registerNewTeam("aSelf");
            self.setPrefix(ChatColor.GREEN + "");
            self.addPlayer(player);

            Team enemiesCheck = playerScoreboard.getTeam("cEnemies");
            Team enemies = enemiesCheck != null ? enemiesCheck : playerScoreboard.registerNewTeam("cEnemies");
            enemies.setPrefix(ChatColor.RED + "");

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer == player) continue;
                enemies.addPlayer(onlinePlayer);
            }

            if (gameManager.getMode() != Mode.SOLOS && gameManager.getTeam(player.getUniqueId()) != null) {
                Team friendliesCheck = playerScoreboard.getTeam("bFriendlies");
                Team friendlies = friendliesCheck != null ? friendliesCheck : playerScoreboard.registerNewTeam("bFriendlies");
                friendlies.setPrefix(ChatColor.GREEN + "");
                for (UUID teammateUUID : gameManager.getTeam(player.getUniqueId()).getTeammates(player.getUniqueId())) {
                    Player teammate = Bukkit.getPlayer(teammateUUID);
                    if (teammate == null) continue;
                    friendlies.addPlayer(teammate);
                    enemies.removePlayer(teammate);
                }
            }
        }
    }

    public void updateGlobalScoreboard() {
        globalScoreboard.updateScoreboard();
        updateTabHealth(null, 0);
    }

    @Override
    public void onDisable() {
        gameManager.cleanup();
    }

    public static UHCGame getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public void createExpOrb(Location location, int amount) {
        ExperienceOrb experienceOrb = (ExperienceOrb) location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
        experienceOrb.setExperience(amount);
    }
}
