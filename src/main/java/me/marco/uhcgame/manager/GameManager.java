package me.marco.uhcgame.manager;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.commands.PartyCommand;
import me.marco.uhcgame.tasks.FreezeTimer;
import me.marco.uhcgame.tasks.DeathmatchStartCountdown;
import me.marco.uhcgame.tasks.GameStartCountdown;
import me.marco.uhcgame.tasks.GraceEndCountdown;
import me.marco.uhcgame.util.Util;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class GameManager {
    private final UHCGame plugin;
    private final FileConfiguration config;
    private GameState state = GameState.WAITING;
    private Mode mode;
    private final World world;
    private final WorldBorder border;
    private GraceEndCountdown graceEndCountdown;
    private DeathmatchStartCountdown deathmatchStartCountdown;

    private final HashSet<UUID> players = new HashSet<UUID>();
    private final HashMap<UUID, Integer> playerStats = new HashMap<UUID, Integer>();
    private final HashMap<UUID, ItemStack[]> deaths = new HashMap<UUID, ItemStack[]>();

    public GameManager(UHCGame plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
        switch (config.getInt("mode")) {
            case 1:
                mode = Mode.DUOS;
                break;
            case 2:
                mode = Mode.TRIOS;
                break;
            default:
                mode = Mode.SOLOS;
                break;
        }
        world = UHCGame.getInstance().getServer().getWorld(config.getString("game-world"));
        border = world.getWorldBorder();
        setupBorder();
    }

    public void setGameState(GameState state) {
        if (this.state == state) return;
        this.state = state;

        switch (state) {
            case WAITING:
                break;
            case STARTING:
                // Loop through all the parties and kick offline players.
                for (Party party : parties) {
                    List<UUID> playersToRemove = new ArrayList<>();
                    for (UUID uuid : party.getMembers()) {
                        Player member = Bukkit.getPlayer(uuid);
                        if (member != null) continue;
                        playersToRemove.add(member.getUniqueId());
                        party.messageAll(PartyCommand.color + member.getName() + " was removed from the party as they are not online.");
                    }
                    playersToRemove.forEach(party::removeMember);
                }

                GameStartCountdown gameStartCountdown = new GameStartCountdown();
                gameStartCountdown.runTaskTimer(plugin, 0, 20);
                break;
            case GRACE:
                switch (mode) {
                    case DUOS:
                        // Make a list of players not in a party
                        List<UUID> unsorted = new ArrayList<>();
                        for (UUID uuid : players) {
                            Player player = Bukkit.getPlayer(uuid);
                            if (player != null && getParty(player.getUniqueId()) == null) unsorted.add(player.getUniqueId());
                        }
                        Collections.shuffle(unsorted);

                        for (Party party : parties) {
                            // Iterate through every other member
                            List<UUID> members = party.getMembers();
                            for (int i = 0; i < members.size(); i += 2) {
                                List<UUID> teamPlayers = new ArrayList<>();
                                UUID member = members.get(i);
                                teamPlayers.add(member);
                                UUID teammate = party.getDuosTeammate(member);
                                if (teammate != null) {
                                    // Give them their teammate
                                    teamPlayers.add(teammate);
                                } else if (unsorted.size() != 0) {
                                    // Give them a random teammate
                                    teamPlayers.add(unsorted.remove(0));
                                } // else
                                    // They're on their own
                                teams.add(new Team(teamPlayers));
                            }
                        }
                        int iterations = unsorted.size();
                        for (int i = 0; i < iterations; i += 2) {
                            System.out.println(i);
                            List<UUID> teamPlayers = new ArrayList<>();
                            teamPlayers.add(unsorted.remove(0));
                            if (unsorted.size() != 0) teamPlayers.add(unsorted.remove(0));
                            teams.add(new Team(teamPlayers));
                        }
                        break;
                    case TRIOS:
                        Bukkit.broadcastMessage(ChatColor.RED + "Trios is not currently supported.");
                        setGameState(GameState.WAITING);
                        break;
                }

                Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
                for (org.bukkit.scoreboard.Team team : scoreboard.getTeams()) {
                    team.unregister();
                }

                int id = 0;
                for (Team team : teams) {
                    org.bukkit.scoreboard.Team scoreboardTeam = scoreboard.registerNewTeam(String.valueOf(id));
                    for (UUID uuid : team.getMembers()) {
                        Player member = Bukkit.getPlayer(uuid);
                        if (member == null) continue;
                        scoreboardTeam.addPlayer(member);
                    }
                    id++;
                }

                setupBorder();
                StringBuilder playersAsString = new StringBuilder();
                Location location = world.getSpawnLocation();
                for (UUID uuid : players) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) continue;
                    playersAsString.append(" ").append(player.getName());
                    player.teleport(location);
                }

                // TODO: Create exception for trios
                switch (mode) {
                    case DUOS:
                        plugin.getServer().dispatchCommand(
                                plugin.getServer().getConsoleSender(),
                                "spreadplayers 0 0 150 " + 0.8 * getBorderSize() + " true " + playersAsString.substring(1)
                        );
                        break;
                    case TRIOS:
                        // not breaking for now
                    default:
                        plugin.getServer().dispatchCommand(
                                plugin.getServer().getConsoleSender(),
                                "spreadplayers 0 0 150 " + 0.8 * getBorderSize() + " false " + playersAsString.substring(1)
                        );
                        break;
                }


                graceEndCountdown = new GraceEndCountdown();
                graceEndCountdown.runTaskTimer(plugin, 0, 20);

                players.forEach(p -> {
                    Player player = Bukkit.getPlayer(p);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setHealth(player.getMaxHealth());
                    player.setFoodLevel(20);
                    player.setLevel(0);

                    for (PotionEffect effect : player.getActivePotionEffects()){
                        player.removePotionEffect(effect.getType());
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20 * 60 * config.getInt("grace-period"), 2));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20 * 60 * config.getInt("grace-period"), 3));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 60 * config.getInt("grace-period"), 0));

                    Inventory inv = player.getInventory();
                    inv.clear();
                    // plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "minecraft:clear " + player.getName());
                    inv.addItem(new ItemStack(Material.WOOD_SWORD, 1));
                    inv.addItem(new ItemStack(Material.WOOD_PICKAXE, 1));
                    inv.addItem(new ItemStack(Material.COOKED_BEEF, 5));
                    inv.addItem(new ItemStack(Material.STRING, 4));
                    if (mode != Mode.SOLOS) {
                        ItemStack teammateTracker = new ItemStack(Material.COMPASS, 1);
                        ItemMeta meta = teammateTracker.getItemMeta();
                        meta.setDisplayName(ChatColor.GOLD + "Teammate Tracker");
                        teammateTracker.setItemMeta(meta);
                        inv.addItem(teammateTracker);
                    }
                });
                new FreezeTimer(10).runTaskTimer(plugin, 0, 20);
                break;
            case PLAYING:
                if (graceEndCountdown != null) graceEndCountdown.cancel();

                deathmatchStartCountdown  = new DeathmatchStartCountdown();
                deathmatchStartCountdown.runTaskTimer(plugin, 0, 20);

                Bukkit.broadcastMessage(ChatColor.RED + "Grace period has ended and PvP has been enabled!");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 5, 1);
                }
                startBorderShrink();
                break;
            case DEATHMATCH:
                if (deathmatchStartCountdown != null) deathmatchStartCountdown.cancel();

                ConfigManager configManager = plugin.getConfigManager();
                FileConfiguration data = configManager.fetch("deathmatch");
                List<Map<?, ?>> spawns = data.getMapList("spawns");
                Collections.shuffle(spawns);

                World deathmatchWorld = Bukkit.getWorld(plugin.getConfig().getString("deathmatch-world"));

                Bukkit.getOnlinePlayers().forEach(p -> p.teleport(deathmatchWorld.getSpawnLocation()));

                // TODO: create exception for duos/trios.
                int i = 0;
                switch (mode) {
                    case SOLOS:
                        for (UUID uuid: players) {
                            Player player = Bukkit.getPlayer(uuid);
                            Map<String, String> locmap = (Map<String, String>) spawns.get(i);

                            double x = Double.parseDouble(locmap.get("x"));
                            double y = Double.parseDouble(locmap.get("y"));
                            double z = Double.parseDouble(locmap.get("z"));
                            double yaw = Double.parseDouble(locmap.get("yaw"));
                            double pitch = Double.parseDouble(locmap.get("pitch"));

                            player.teleport(new Location(deathmatchWorld, x, y, z, (float)yaw, (float)pitch));
                            i++;
                        }
                        break;
                    case DUOS:
                        for (Team team : teams) {
                            Map<String, String> locmap = (Map<String, String>) spawns.get(i);
                            double x = Double.parseDouble(locmap.get("x"));
                            double y = Double.parseDouble(locmap.get("y"));
                            double z = Double.parseDouble(locmap.get("z"));
                            double yaw = Double.parseDouble(locmap.get("yaw"));
                            double pitch = Double.parseDouble(locmap.get("pitch"));
                            Location loc = new Location(deathmatchWorld, x, y, z, (float)yaw, (float)pitch);
                            for (UUID uuid : team.getMembers()) {
                                Player player = Bukkit.getPlayer(uuid);
                                if (player == null) continue;
                                player.teleport(loc);
                            }
                            i++;
                        }
                        break;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!isSpectating(player)) continue;
                    player.teleport(deathmatchWorld.getSpawnLocation());
                }

                new FreezeTimer(15).runTaskTimer(plugin, 0, 20);
                break;
            case OVER:
                switch (mode) {
                    case SOLOS:
                        Player winner = getWinner();
                        Bukkit.broadcastMessage(ChatColor.GOLD + winner.getName() + " has won the game!");
                        break;
                    case DUOS:
                        Bukkit.broadcastMessage(ChatColor.GOLD + getWinners() + " have won the game!");
                }
                break;
        }

        plugin.updateGlobalScoreboard();
        plugin.updatePlayerScoreBoards();
    }

    public GameState getState() {
        return state;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        plugin.updateGlobalScoreboard();
    }

    public Mode getMode() {
        return mode;
    }

    public void addPlayer(UUID uuid) {
        players.add(uuid);
        if (playerStats.get(uuid) != null) return;
        playerStats.put(uuid, 0);
        plugin.updateGlobalScoreboard();
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
        if (state == GameState.WAITING || state == GameState.STARTING) return;
        if (players.size() == 1 || getTeamsRemaining() == 1) setGameState(GameState.OVER);
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) player.setGameMode(GameMode.SPECTATOR);
        plugin.updateGlobalScoreboard();
    }

    public boolean hasDied(UUID uuid) {
        return deaths.containsKey(uuid);
    }

    public ItemStack[] getDeathInventory(UUID uuid) {
        return deaths.remove(uuid);
    }

    public void registerDeath(UUID uuid, ItemStack[] inventory) {
        deaths.put(uuid, inventory);
    }

    public boolean removePlayer(String targetName) {
        Player player = Bukkit.getPlayer(targetName);
        UUID uuid;
        if (player == null) {
            OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(targetName);
            if (oPlayer == null) {
                return false;
            }
            uuid = oPlayer.getUniqueId();
        } else {
            uuid = player.getUniqueId();
        }

        if (isPlaying(uuid)) {
            removePlayer(uuid);
            return true;
        } else {
            return false;
        }
    }

    public void increaseStat(UUID uuid) {
        playerStats.put(uuid, playerStats.get(uuid) + 1);
    }

    public boolean isPlaying(UUID uuid) {
        return players.contains(uuid);
    }

    public boolean isSpectating(Player player) {
        return player.isOnline() && isSpectating(player.getUniqueId());
    }

    public boolean isSpectating(UUID uuid) {
        return Bukkit.getPlayer(uuid) != null && !players.contains(uuid);
    }

    public int getKills(UUID uuid) {
        if (isPlaying(uuid)) {
            return playerStats.get(uuid);
        } else {
            return 0;
        }
    }

    public HashSet<UUID> getPlayers() {
        return players;
    }

    public int getPlayersRemaining() {
        return players.size();
    }

    public Player getWinner() {
        if (players.size() > 1) return null;
        return Bukkit.getPlayer(players.iterator().next());
    }

    public int getGraceRemaining() {
        return graceEndCountdown.getSecondsRemaining();
    }

    public int getDeathmatchCountdownRemaining() {
        return deathmatchStartCountdown.getSecondsRemaining();
    }

    private void setupBorder() {
        border.setCenter(new Location(world, 0, 70, 0));
        border.setDamageAmount(0.2);
        border.setDamageBuffer(0);
        border.setSize(config.getInt("starting-border") * 2);
    }

    private void startBorderShrink() {
        border.setSize(100, config.getInt("starting-border") * config.getLong("border-shrink-rate"));
    }

    public int getBorderSize() {
        return (int) border.getSize() / 2;
    }

    public void setDeathmatchTimer(int seconds) {
        if (deathmatchStartCountdown == null) return;
        deathmatchStartCountdown.setTimeLeft(seconds);
    }

    public void cleanup() {
        border.reset();
        // wipe map
    }

    // Duos

    private final List<Party> parties = new ArrayList<>();
    private final List<Team> teams = new ArrayList<>();

    public int getTeamKills(Player player) {
        Team team = getTeam(player.getUniqueId());
        if (team == null) return getKills(player.getUniqueId());
        int kills = 0;
        for (UUID member : team.getMembers()) {
            kills += getKills(member);
        }
        return kills;
    }

    public int getTeamsRemaining() {
        int remaining = 0;
        for (Team team : teams) {
            for (UUID member : team.getMembers()) {
                if (!hasDied(member)) {
                    remaining++;
                    break;
                }
            }
        }
        return remaining;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public String getWinners() {
        Team winningTeam = getTeam(players.iterator().next());
        StringBuilder winners = new StringBuilder();
        for (UUID member : winningTeam.getMembers()) {
            winners.append(Util.getPlayerName(member)).append(", ");
        }
        return winners.substring(0, winners.length() - 2);
    }

    public boolean createParty(Player leader) {
        if (getParty(leader.getUniqueId()) != null) return false;
        parties.add(new Party(leader.getUniqueId()));
        return true;
    }

    public void removeParty(Party party) {
        parties.remove(party);
    }

    // TODO: Implement with admin command to remove other parties
    public void removeParty(UUID member) {
        Optional<Party> party = parties.stream().filter(p -> p.hasMember(member)).findFirst();
        if (!party.isPresent()) return;
        parties.remove(party.get());
    }

    public Party getParty(UUID player) {
        Optional<Party> party = parties.stream().filter(p -> p.hasMember(player)).findFirst();
        return party.orElse(null);
    }

    public Team getTeam(UUID member) {
        Optional<Team> team = teams.stream().filter(t -> t.hasMember(member)).findFirst();
        return team.orElse(null);
    }

    public UUID getTeammate(UUID player) {
        Party party = getParty(player);
        Team team = getTeam(player);
        switch (mode) {
            case DUOS:
                if ((state == GameState.WAITING || state == GameState.STARTING) && party != null) return party.getDuosTeammate(player);
                if (team != null) return team.getTeammate(player);
                break;
            case TRIOS:
                // TODO: Trios
                return null;
        }
        return null;
    }

    public boolean sameTeam(UUID player1, UUID player2) {
        return getTeam(player1) == getTeam(player2);
    }
}
