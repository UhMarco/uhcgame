package me.marco.uhcgame.commands;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeathmatchCommand implements CommandExecutor {
    private final String usage = ChatColor.RED + "Usage: /deathmatch spawn <add:remove:list>";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(usage);
            return false;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can do this command.");
            return true;
        }
        Player player = (Player) sender;
        Location loc = player.getLocation();
        UHCGame plugin = UHCGame.getInstance();
        ConfigManager configManager = plugin.getConfigManager();
        FileConfiguration deathmatchConfig = configManager.fetch("deathmatch");

        if ("spawn".equalsIgnoreCase(args[0])) {
            switch (args[1].toLowerCase()) {
                case "add":
                    List<Map<?, ?>> spawns = deathmatchConfig.getMapList("spawns");
                    Map<String, String> locmap = new HashMap<>();
                    locmap.put("x", loc.getX() + "");
                    locmap.put("y", loc.getY() + "");
                    locmap.put("z", loc.getZ() + "");
                    locmap.put("yaw", loc.getYaw() + "");
                    locmap.put("pitch", loc.getPitch() + "");
                    spawns.add(locmap);
                    deathmatchConfig.set("spawns", spawns);
                    configManager.save("deathmatch", deathmatchConfig);
                    sender.sendMessage(ChatColor.RED + "Added spawnpoint.");
                    break;
                case "remove":
                    sender.sendMessage(ChatColor.RED + "I haven't added this yet.");
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "I haven't added this yet.");
                    break;
            }
        } else {
            sender.sendMessage(usage);
        }
        return true;
    }
}
