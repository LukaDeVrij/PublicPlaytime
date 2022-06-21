package me.lifelessnerd.publicplaytime.commands;

import me.lifelessnerd.publicplaytime.PlaytimeDatabase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class GetPlaytime implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if(args.length < 1){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&bPublicPlaytime&r] &cNo arguments given."));
            return false;
        }

        String argument = args[0];
        Player argumentPlayer = null;
        try {
            // Try and get the player out of the online pool of players
            argumentPlayer = Bukkit.getServer().getPlayerExact(argument);
            String name = argumentPlayer.getName();

        }catch(Exception exception){
            // If we're in here that means the player is not online
            // We try to get the data from the database
            try {
                int value = Integer.parseInt(PlaytimeDatabase.get().getString(argument));
                //Keeping this in here to make try catch work
                String playTime = calculateTimeSpan(value, args, sender);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"[&bPublicPlaytime&r] &6Player &o" + argument + "&r&6 has played &c" + playTime + "&6!"));
                return true;
                // If this fails, i.e. player is not in database, we give up
            }catch(Exception exception2){
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&bPublicPlaytime&r] &cPlayer has never visited this server."));
                return true;
            }

        }
        // If we continue, we are dealing with a player that is online
        // And we now have the player at our disposal
        int score = argumentPlayer.getStatistic(Statistic.PLAY_ONE_MINUTE);
        // Will not produce nullPointer, if player is online he will always have a score in this category
        String playTime = calculateTimeSpan(score, args, sender);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"[&bPublicPlaytime&r] &6Player &o" + argument + "&r&6 has played &c" + playTime + "&6!"));

        return true;
    }

    public String calculateTimeSpan(int value, String[] args, CommandSender sender){
        String outputMode;
        try{
            outputMode = args[1];
        }catch(Exception exception){
            outputMode = "standard";
        }

        int seconds = value / 20;
        Duration playTime = Duration.ofSeconds(seconds);
        long DD = playTime.toDays();
        long HH = playTime.toHoursPart();
        long MM = playTime.toMinutesPart();
        long SS = playTime.toSecondsPart();

        String output = "";
        switch(outputMode){
            default:
                output = String.format("%s days, %s hours, %s minutes & %s seconds",DD,HH,MM,SS);
                break;
            case "seconds":
                output = seconds + " seconds";
                break;
            case "minutes":
                output = playTime.toMinutes() + " minutes";
                break;
            case "hours":
                output = playTime.toHours() + " hours";
                break;
            case "days":
                output = playTime.toDays() + " days";
                break;
            case "ticks":
                output = playTime.toSeconds() * 20 + " ticks";
        }
        return output;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 2){
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
