package me.marco.uhcgame.commands;

import me.marco.uhcgame.UHCGame;
import me.marco.uhcgame.manager.GameManager;
import me.marco.uhcgame.manager.Party;
import me.marco.uhcgame.util.Util;
import me.rayzr522.jsonmessage.JSONMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PartyCommand implements CommandExecutor {
    private final GameManager gameManager = UHCGame.getInstance().getGameManager();
    public static final ChatColor color = ChatColor.DARK_AQUA;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players are able to do this command.");
            return false;
        }

        boolean success = handleCommand(sender, args);
        UHCGame.getInstance().updateGlobalScoreboard();
        return success;
    }

    private boolean handleCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Player target;
        Party party;

        List<String> newArgs = Arrays.asList(args);
        if (newArgs.size() == 0) return list(player, player);

        switch (newArgs.get(0).toLowerCase()) {
            case "list":
            case "info":
            case "i":
            case "show":
                if (args.length == 1) {
                    return list(player, player);
                } else {
                    target = Bukkit.getPlayer(args[1]);
                    if (target == null) {
                        player.sendMessage(ChatColor.RED + "Error: Player not found.");
                        return false;
                    }
                    return list(player, target);
                }
            case "setup":
            case "create":
                player.sendMessage(color + (gameManager.createParty(player) ? "Party created." : "You are already in a party."));
                return true;
            case "join":
                if (gameManager.getParty(player.getUniqueId()) != null) {
                    player.sendMessage(color + "You are already in a party.");
                    return false;
                }
                if (args.length == 1) {
                    player.sendMessage(color + "/party join <player>");
                    return false;
                }
                target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Error: Player not found.");
                    return false;
                }
                party = gameManager.getParty(target.getUniqueId());
                if (party == null) {
                    sender.sendMessage(color + target.getName() + " is not in a party.");
                    return false;
                }
                if (!party.isInvited(player.getUniqueId())) {
                    sender.sendMessage(color + "You have not been invited to this party.");
                    return false;
                }
                party.addMember(player.getUniqueId());
                party.uninvite(player.getUniqueId());
                party.messageAll(color + player.getName() + " joined the party.");
                return true;
            case "add":
            case "invite":
            case "inv":
                party = gameManager.getParty(player.getUniqueId());
                if (party == null) {
                    gameManager.createParty(player);
                    party = gameManager.getParty(player.getUniqueId());
                    player.sendMessage(color + "Party created.");
                }
                if (party.getLeader() != player.getUniqueId()) {
                    player.sendMessage(color + "Only the party leader can do this.");
                    return false;
                }
                if (args.length == 1) {
                    player.sendMessage(color + "/party invite <player>");
                    return false;
                }
                target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Error: Player not found.");
                    return false;
                }
                if (gameManager.getParty(target.getUniqueId()) != null) {
                    player.sendMessage(color + target.getName() + " is already in a party.");
                    return false;
                }
                if (party.isInvited(target.getUniqueId())) {
                    player.sendMessage(color + target.getName() + " has already been invited to your party.");
                    return false;
                }
                party.invite(target.getUniqueId());
                target.sendMessage(color + "You've been invited to " + player.getName() + "'s party.");
                JSONMessage.create("[Click to accept]")
                        .color(ChatColor.GREEN)
                        .tooltip("Join " + player.getName() + "'s party.")
                        .runCommand("/p join " + player.getName())
                        .send(target);
                target.playSound(target.getLocation(), Sound.ORB_PICKUP, 5, 1);
                player.sendMessage(color + "Invited " + target.getName() + " to your party.");
                return true;
            case "leave":
                party = gameManager.getParty(player.getUniqueId());
                if (party == null) return notInAParty(player);
                if (party.getLeader() == player.getUniqueId()) {
                    sender.sendMessage(color + "You cannot leave as the party leader. You must disband the party or transfer leader to a member.");
                    return false;
                }
                party.removeMember(player.getUniqueId());
                party.messageAll(color + player.getName() + " left the party.");
                return true;
            case "remove":
            case "kick":
                party = gameManager.getParty(player.getUniqueId());
                if (party == null) return notInAParty(player);
                if (party.getLeader() != player.getUniqueId()) {
                    player.sendMessage(color + "Only the party leader can do this.");
                    return false;
                }
                target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Error: Player not found.");
                    return false;
                }
                if (player == target) {
                    player.sendMessage(color + "You're hilarious.");
                    return false;
                }
                target.sendMessage(color + "You were kicked from the party.");
                party.removeMember(target.getUniqueId());
                party.messageAll(color + target.getName() + " was kicked from the party.");
                return true;
            case "disband":
                // TODO: Add admin command to disband other parties
                party = gameManager.getParty(player.getUniqueId());
                if (party == null) return notInAParty(player);
                if (party.getLeader() != player.getUniqueId()) {
                    player.sendMessage(color + "Only the party leader can do this.");
                    return false;
                }
                party.messageAll(color + "The party was disbanded.");
                gameManager.removeParty(party);
                return true;
            case "transfer":
            case "leader":
                party = gameManager.getParty(player.getUniqueId());
                if (party == null) return notInAParty(player);
                if (party.getLeader() != player.getUniqueId()) {
                    player.sendMessage(color + "Only the party leader can do this.");
                    return false;
                }
                target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(ChatColor.RED + "Error: Player not found.");
                    return false;
                }
                if (player == target) {
                    player.sendMessage(color + "Uhh okay..");
                    return false;
                }
                party.setLeader(target.getUniqueId());
                party.messageAll(color + target.getName() + " was given leadership of the party.");
                return true;
        }
        player.sendMessage(color + "Party Commands:\n - I'll add these later");
        return false;
    }

    private boolean notInAParty(Player player) {
        player.sendMessage(color + "You are not in a party.");
        return false;
    }

    private boolean list(Player player, Player target) {
        Party party = gameManager.getParty(target.getUniqueId());
        if (party == null) return notInAParty(player);
        String border = ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------");
        String messageTitle = border + "\n" + color + Util.getPlayerName(party.getLeader()) + "'s party:";
        StringBuilder message = new StringBuilder().append(messageTitle);
        for (UUID member : party.getMembers()) {
            String thingToAppend = "\n" + color + "- " + ChatColor.GRAY + Util.getPlayerName(member);
            message.append(thingToAppend);
            if (Bukkit.getPlayer(member) != null) continue;
            message.append(" (Offline)");
        }
        message.append("\n").append(border);
        player.sendMessage(message.toString());
        return true;
    }
}
