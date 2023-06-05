package me.marco.uhcgame.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class Util {
    public Player getPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) return player;
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        if (offlinePlayer != null) return offlinePlayer.getPlayer();
        return null;
    }

    public String getPlayerName(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) return player.getName();
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        if (offlinePlayer != null) return offlinePlayer.getName();
        return null;
    }

    public List<Player> convertToPlayerList(List<UUID> uuids) {
        List<Player> players = new ArrayList<>();
        for (UUID uuid : uuids) {
            Player p = getPlayer(uuid);
            if (p != null) players.add(p);
        }
        return players;
    }

    public List<UUID> converToUuidList(List<Player> players) {
        List<UUID> uuids = new ArrayList<>();
        for (Player player : players) {
            uuids.add(player.getUniqueId());
        }
        return uuids;
    }
}
