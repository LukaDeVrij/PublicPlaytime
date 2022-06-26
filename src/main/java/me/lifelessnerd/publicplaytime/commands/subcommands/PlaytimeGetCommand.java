package me.lifelessnerd.publicplaytime.commands.subcommands;

import me.lifelessnerd.publicplaytime.commands.Subcommand;
import me.lifelessnerd.publicplaytime.filehandlers.PlaytimeDatabase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;

public class PlaytimeGetCommand extends Subcommand {
    @Override
    public String getName() {
        return "get";
    }

    @Override
    public String getDescription() {
        return "Gets the playtime of the provided player.";
    }

    @Override
    public String getSyntax() {
        return "/playtime get <player> <days/hours/minutes/seconds/ticks/standard>";
    }

    @Override
    public boolean perform(Player sender, String[] args) {


        if(args.length < 1){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&bPublicPlaytime&r] &cNo arguments given."));
            return false;
        }

        String argument = args[1];
        Player argumentPlayer = null;
        try {
            // Try and get the player out of the online pool of players
            argumentPlayer = Bukkit.getServer().getPlayerExact(argument);
            String name = argumentPlayer.getName();
            sender.sendMessage(name);

        }catch(Exception exception){
            // If we're in here that means the player is not online
            // We try to get the data from the database
            sender.sendMessage(exception.toString());
            try {
                int value = Integer.parseInt(PlaytimeDatabase.get().getString(argument));
                //Keeping this in here to make try catch work
                String playTime = calculateTimeSpan(value, args, sender);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6Player &o" + argument + "&r&6 has played &c" + playTime + "&6!"));
                sender.sendMessage("debug");
                return true;
                // If this fails, i.e. player is not in database, we give up
            }catch(Exception exception2){
                sender.sendMessage(exception2.toString());
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlayer has never visited this server."));
                return true;
            }

        }
        // If we continue, we are dealing with a player that is online
        // And we now have the player at our disposal
        int score = argumentPlayer.getStatistic(Statistic.PLAY_ONE_MINUTE);
        // Will not produce nullPointer, if player is online he will always have a score in this category
        String playTime = calculateTimeSpan(score, args, sender);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6Player &o" + argument + "&r&6 has played &c" + playTime + "&6!"));
        return true;



    }

    public String calculateTimeSpan(int value, String[] args, CommandSender sender){
        String outputMode;
        try{
            outputMode = args[2];
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

}
