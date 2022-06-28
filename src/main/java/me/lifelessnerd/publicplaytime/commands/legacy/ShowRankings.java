package me.lifelessnerd.publicplaytime.commands.legacy;

import me.lifelessnerd.publicplaytime.PlaytimeRanking;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ShowRankings implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        int maxAmount;

        if (!(sender instanceof Player)){
            return false;
        }

        if (args.length < 2){
            maxAmount = 10;
        }
        else {
            try{
                maxAmount = Integer.parseInt(args[1]);
            }catch(Exception ex){
                maxAmount = 10;
            }
        }

        String outputMode = "standard";
        try{
            outputMode = args[0];
        }catch(Exception exception){
            outputMode = "standard";
        }
        Player player = (Player) sender;

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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 2){
            List<String> arguments = new ArrayList<>();
            arguments.add("5");
            arguments.add("20");
            arguments.add("100");
            return arguments;
        }

        if (args.length == 1){
            List<String> arguments = new ArrayList<>();
            arguments.add("standard");
            arguments.add("days");
            arguments.add("hours");
            arguments.add("minutes");
            arguments.add("seconds");
            arguments.add("ticks");
            return arguments;
        }

        return null;
    }

}
