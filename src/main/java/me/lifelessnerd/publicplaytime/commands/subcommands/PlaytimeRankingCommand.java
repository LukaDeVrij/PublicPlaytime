package me.lifelessnerd.publicplaytime.commands.subcommands;

import me.lifelessnerd.publicplaytime.PlaytimeRanking;
import me.lifelessnerd.publicplaytime.commands.Subcommand;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class PlaytimeRankingCommand extends Subcommand {
    @Override
    public String getName() {
        return "ranking";
    }

    @Override
    public String getDescription() {
        return "Returns a ranked leaderboard in chat.";
    }

    @Override
    public String getSyntax() {
        return "/playtime ranking <outputMode> <maxAmount>";
    }

    @Override
    public boolean perform(Player player, String[] args) {
        int maxAmount;


        if (args.length < 2){
            maxAmount = 10;
        }
        else {
            try{
                maxAmount = Integer.parseInt(args[2]);
            }catch(Exception ex){
                maxAmount = 10;
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your max amount argument was not valid. Using 10."));
            }
        }

        String outputMode = "standard";
        try{
            outputMode = args[1];
        }catch(Exception exception){
            outputMode = "standard";
        }

        if (outputMode.equalsIgnoreCase("ticks") | outputMode.equalsIgnoreCase("minutes") |
                outputMode.equalsIgnoreCase("seconds") | outputMode.equalsIgnoreCase("hours") |
                outputMode.equalsIgnoreCase("days")){ // its all good man
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your output mode was not recognised. Using standard."));
            outputMode = "standard";
        }

        PlaytimeRanking ranking = new PlaytimeRanking();
        HashSet<String> playerList = ranking.getPlayerNames();
        HashMap<String, String> rankedList = ranking.getRanking(playerList, maxAmount, outputMode);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlaytime Leaderboard Ranking" + "&7 (max " + maxAmount + ")" +
                " (Output Mode: " + outputMode + ")"));
        for (String playerName : rankedList.keySet()){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&o" + playerName + "&r -&c " + rankedList.get(playerName)));
        }
        if (maxAmount > rankedList.keySet().toArray().length){
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere were only " + rankedList.keySet().toArray().length + " entries to show."));
        }
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100f, 0.1f);

        return true;
    }
}
