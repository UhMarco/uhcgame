package me.marco.uhcgame.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@UtilityClass
public class Messenger {
    public static final String color = ChatColor.GRAY + "" + ChatColor.ITALIC;

    public void messageOperators(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.isOp()) continue;
            player.sendMessage(color + "[UHC: " + message + color + "]");
        }
    }
}
