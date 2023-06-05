package me.marco.uhcgame.tasks;

import dev.jcsoftware.jscoreboards.JPerPlayerScoreboard;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardUpdater extends BukkitRunnable {
    private final JPerPlayerScoreboard scoreboard;
    public ScoreboardUpdater(JPerPlayerScoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }
    @Override
    public void run() {
        scoreboard.updateScoreboard();
    }
}
