package me.lifelessnerd.publicplaytime.tasks;

import me.lifelessnerd.publicplaytime.PlaytimeRanking;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.HashSet;

public class RefreshScoreboard extends BukkitRunnable {

    JavaPlugin plugin;
    HashMap<String, String> rankedList;
    Scoreboard scoreboard;
    Player player;
    Objective objective;
    String outputMode;
    int maxAmount;
    boolean global;

    public RefreshScoreboard(JavaPlugin plugin, Scoreboard scoreboard, Player player, Objective objective, String outputMode, int maxAmount, boolean global) {
        this.plugin = plugin;
        this.rankedList = rankedList;
        this.scoreboard = scoreboard;
        this.player = player;
        this.objective = objective;
        this.outputMode = outputMode;
        this.maxAmount = maxAmount;
        this.global = global;
    }

    @Override
    public void run() {

        PlaytimeRanking playtimeRanking = new PlaytimeRanking();
        HashSet<String> playerList = playtimeRanking.getPlayerNames();
        HashMap<String, String> rankedList = playtimeRanking.getRanking(playerList, maxAmount, outputMode);

        int index = 0;
        for (String playerName : rankedList.keySet()) {
            Score score = objective.getScore(playerName);
            try {
                String valueString = rankedList.get(playerName);
                String[] partsOfValue = valueString.split(" ");
                score.setScore(Integer.parseInt(partsOfValue[0]));
            } catch (Exception exception) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&bPublicPlaytime&r] &aPlease choose a different output mode."));
                player.sendMessage(exception.toString());
            }

            index++;
        }

        StringBuilder outputModeString = new StringBuilder(outputMode);
        char char0OfOutput = outputMode.charAt(0);
        char capitalOfOutput = Character.toUpperCase(char0OfOutput);
        outputModeString.setCharAt(0, capitalOfOutput);

        objective.setDisplayName(outputModeString + " Played");

        if(global){
            for (Player player : Bukkit.getOnlinePlayers()  ) {
                player.setScoreboard(scoreboard);
            }
        } else {
            player.setScoreboard(scoreboard);
        }

    }
}
