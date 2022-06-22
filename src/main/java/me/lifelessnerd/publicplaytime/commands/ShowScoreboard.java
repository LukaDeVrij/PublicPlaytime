package me.lifelessnerd.publicplaytime.commands;

import me.lifelessnerd.publicplaytime.PlaytimeRanking;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ShowScoreboard implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)){
            return false;
        }
        if (args.length < 1){
            sender.sendMessage("Please provide an argument!");
            return false;
        }
        Player player = (Player) sender;

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        TextComponent component = Component.text("Time Played");
        Objective playtimeRankings = scoreboard.registerNewObjective("Time Played", "dummy", component);


        if (args[0].equalsIgnoreCase("show")){

            playtimeRankings.setDisplaySlot(DisplaySlot.SIDEBAR);

            //Get data either directly from Statistics.PLAY_ONE_MINUTE or out of the file
            //Put all those things in hashmap
            //Rank them on value (possibly on duration but on seconds otherwise, then just reconvert them back to show)

            for (int i = 0; i < 10; i++) {
                Score score = playtimeRankings.getScore("");
            }
            //This should ideally be run every tick from now on, until hide is called


            return true;
        }
        else if(args[0].equalsIgnoreCase("hide")){

            playtimeRankings.unregister();
            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {





        return null;
    }
}
