package me.lifelessnerd.publicplaytime.commands.legacy;

import me.lifelessnerd.publicplaytime.PlaytimeRanking;
import me.lifelessnerd.publicplaytime.PublicPlaytime;
import me.lifelessnerd.publicplaytime.tasks.RefreshScoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Logger;

public class ShowScoreboard implements TabExecutor {

    Logger logger;
    private PublicPlaytime plugin;
    BukkitTask refreshScoreboard;

    public ShowScoreboard(PublicPlaytime plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // /command <show/hide> <standard/hours/etc> <maxAmount>
        logger = plugin.getLogger();

        if (!(sender instanceof Player)){
            return false;
        }
        if (args.length < 1){
            sender.sendMessage("Please provide an argument!");
            return false;
        }
        String outputMode = "hours";
        try{
            outputMode = args[1];
            if (outputMode.equalsIgnoreCase("ticks") | outputMode.equalsIgnoreCase("minutes") |
                    outputMode.equalsIgnoreCase("seconds") | outputMode.equalsIgnoreCase("hours") |
                    outputMode.equalsIgnoreCase("days")){
            }
            else {
                outputMode = "hours";
            }
        }catch(Exception exception){
            outputMode = "hours";
        }

        Player player = (Player) sender;

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        TextComponent component = Component.text("Time Played");
        Objective objective = scoreboard.registerNewObjective("Time Played", "dummy", component);


        if (args[0].equalsIgnoreCase("show")){

            objective = scoreboard.getObjective("Time Played");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);


            int maxAmount = 10;
            try{
                maxAmount = Integer.parseInt(args[2]);
            }catch(Exception exception){
                maxAmount = 10;
            }

            PlaytimeRanking playtimeRanking = new PlaytimeRanking();
            HashSet<String> playerList = playtimeRanking.getPlayerNames();
            HashMap<String, String> rankedList = playtimeRanking.getRanking(playerList, maxAmount, outputMode);

            //Task one time execution to create the scoreboard
            BukkitTask createScoreboard = new RefreshScoreboard(plugin, scoreboard, player, objective, outputMode, maxAmount).runTask(plugin);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&bPublicPlaytime&r] &aScoreboard shown."));

            //Refresh
            if (plugin.getConfig().getBoolean("scoreboardRefreshAllowed")){

                int period = plugin.getConfig().getInt("scoreboardRefreshInterval");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7The scoreboard will be refreshed every " + period / 20f + " seconds. To cancel, hide the scoreboard."));
                logger.warning("The scoreboard will be refreshed every " + period / 20f + " seconds. This may cause performance to decrease. To see options regarding this feature, see config.yml!");
                try{
                    refreshScoreboard.cancel();
                }catch(Exception exception){}
                refreshScoreboard = new RefreshScoreboard(plugin, scoreboard, player, objective, outputMode, maxAmount).runTaskTimer(plugin, 0L, period);
            }
            else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNote that scoreboard refreshing is disabled. To update, repeat the command."));
            }

            return true;
        }
        else if(args[0].equalsIgnoreCase("hide")){

            try{
                refreshScoreboard.cancel();
            } catch(Exception ignored) {
            }
            objective.unregister();
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&bPublicPlaytime&r] &aScoreboard hidden."));

            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1){
            List<String> arguments = new ArrayList<>();
            arguments.add("show");
            arguments.add("hide");
            return arguments;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("show")){
            List<String> arguments = new ArrayList<>();
            arguments.add("days");
            arguments.add("hours");
            arguments.add("minutes");
            arguments.add("seconds");
            arguments.add("ticks");
            return arguments;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("show")){
            List<String> arguments = new ArrayList<>();
            arguments.add("5");
            arguments.add("10");
            arguments.add("15");
            return arguments;
        }




        return null;
    }
}
