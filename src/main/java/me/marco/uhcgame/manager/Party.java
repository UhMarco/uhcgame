package me.marco.uhcgame.manager;

import me.marco.uhcgame.util.Util;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Party {
    private UUID leader;
    private final List<UUID> members;
    private final List<UUID> invited;

    public Party(UUID leader) {
        this.leader = leader;
        members = new ArrayList<>();
        invited = new ArrayList<>();
        members.add(leader);
    }

    public boolean hasMember(UUID player) {
        return members.contains(player);
    }

    public UUID getLeader() {
        return leader;
    }

    public void setLeader(UUID player) {
        leader = player;
    }

    public int getMemberIndex(UUID player) {
        return members.indexOf(player);
    }

    public UUID getMemberFromIndex(int index) {
        if (index >= members.size() || index < 0) return null;
        return members.get(index);
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void invite(UUID player) {
        if (invited.contains(player)) return;
        invited.add(player);
    }

    public void uninvite(UUID player) {
        invited.remove(player);
    }

    public boolean isInvited(UUID player) {
        return invited.contains(player);
    }

    public void addMember(UUID player) {
        if (members.contains(player)) return;
        members.add(player);
    }

    public void removeMember(UUID player) {
        members.remove(player);
    }

    public void messageAll(String message) {
        List<Player> players = Util.convertToPlayerList(members);
        for (Player player : players) {
            if (!player.isOnline()) continue;
            player.sendMessage(message);
        }
    }

    public UUID getDuosTeammate(UUID player) {
        int index = getMemberIndex(player);
        int teammateIndex = index + (index % 2 == 0 ? 1 : -1);
        if (teammateIndex < 0 || teammateIndex > members.size()) return null;
        return getMemberFromIndex(teammateIndex);
    }
}
