package me.marco.uhcgame.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {
    private final List<UUID> members;

    public Team(List<UUID> members) {
        this.members = members;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public UUID getTeammate(UUID player) {
        return  members.get((members.indexOf(player) + 1) % members.size());
    }

    public List<UUID> getTeammates(UUID player) {
        List<UUID> copy = new ArrayList<>(members);
        copy.remove(player);
        return copy;
    }

    public boolean hasMember(UUID member) {
        return members.contains(member);
    }
}
