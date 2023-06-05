package me.marco.uhcgame.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can do this command.");
            return true;
        }
        if (args.length == 0) {
            String[] strings = new String[Bukkit.getWorlds().size()];
            for (int i = 0; i < strings.length; i++) {
                strings[i] = Bukkit.getWorlds().get(i).getName();
            }
            sender.sendMessage(ChatColor.RED + "Usage: /world <world>\nWorlds: " + String.join(", ", strings));
            return false;
        }

        World world = Bukkit.getWorld(args[0]);
        if (world == null) {
            World newWorld = Bukkit.createWorld(new WorldCreator(args[0]));
            if (newWorld == null) {
                sender.sendMessage(ChatColor.RED + "World not found.");
                return false;
            } else {
                world = newWorld;
            }
        }

        Player player = (Player) sender;
        player.teleport(world.getSpawnLocation());
        return true;
    }
}
