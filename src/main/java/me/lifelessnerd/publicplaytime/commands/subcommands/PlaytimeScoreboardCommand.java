package me.lifelessnerd.publicplaytime.commands.subcommands;

import me.lifelessnerd.publicplaytime.PlaytimeRanking;
import me.lifelessnerd.publicplaytime.PublicPlaytime;
import me.lifelessnerd.publicplaytime.commands.Subcommand;
import me.lifelessnerd.publicplaytime.tasks.RefreshScoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

public class PlaytimeScoreboardCommand extends Subcommand {

    Logger logger;
    private PublicPlaytime plugin;
    BukkitTask refreshScoreboard;

    public PlaytimeScoreboardCommand(PublicPlaytime plugin){
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "scoreboard";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"sb"};
    }

    @Override
    public String getDescription() {
        return "Show or hide the scoreboard.";
    }

    @Override
    public String getSyntax() {
        return "/playtime scoreboard <show/hide> <hours/seconds/etc> <maxAmount>";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        // /command scoreboard <show/hide> <hours/seconds/etc> <maxAmount>
        logger = plugin.getLogger();
        Player sender = player;
        if (Arrays.asList(args).contains("help")){
            player.sendMessage(getDescription());
            player.sendMessage(getSyntax());
            return true;
        }

        if (args.length < 2){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease provide an argument!"));
            sender.sendMessage(getSyntax());
            return false;
        }
        String outputMode;
        try{
            outputMode = args[2];
            if (outputMode.equalsIgnoreCase("ticks") | outputMode.equalsIgnoreCase("minutes") |
                    outputMode.equalsIgnoreCase("seconds") | outputMode.equalsIgnoreCase("hours") |
                    outputMode.equalsIgnoreCase("days")){
            }
            else {
                outputMode = "hours";
                if (!args[1].equalsIgnoreCase("hide")){ // TODO temp fix really
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your output mode was not recognised. Using hours."));
                }
            }
        }catch(Exception exception){
            outputMode = "hours";
        }


        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        TextComponent component = Component.text("Time Played");
        Objective objective;
        try {
            objective = scoreboard.registerNewObjective("Time Played", "dummy", component);
        } catch (Exception e){
            objective = scoreboard.getObjective("Time Played");
        }

        boolean globalSetting = false;
        if (Arrays.asList(args).contains("global")){
            if (player.hasPermission("publicplaytime.admin")){
                globalSetting = true;
            } else {
                player.sendMessage(Component.text("global").color(NamedTextColor.RED)
                .append(Component.text(" was ignored; you do not have permission!").color(NamedTextColor.GRAY)));
            }
        }


        if (args[1].equalsIgnoreCase("show")){

            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            int maxAmount = 10;
            try{
                maxAmount = Integer.parseInt(args[3]);
            }catch(Exception ignored){}

            PlaytimeRanking playtimeRanking = new PlaytimeRanking();
            HashSet<String> playerList = playtimeRanking.getPlayerNames();
            playtimeRanking.getRanking(playerList, maxAmount, outputMode);

            //Task one time execution to create the scoreboard
            refreshScoreboard = new RefreshScoreboard(plugin, scoreboard, player, objective, outputMode, maxAmount, globalSetting).runTask(plugin);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aScoreboard shown."));

            //Refresh
            if (plugin.getConfig().getBoolean("scoreboardRefreshAllowed")){

                int period = plugin.getConfig().getInt("scoreboardRefreshInterval");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7The scoreboard will be refreshed every " + period / 20f + " seconds. To cancel, hide the scoreboard using &o/playtime scoreboard hide"));
                logger.warning("The scoreboard will be refreshed every " + period / 20f + " seconds. This may cause performance to decrease. To see options regarding this feature, see config.yml!");
                try{
                    refreshScoreboard.cancel();
                }catch(Exception ignored){}
                refreshScoreboard = new RefreshScoreboard(plugin, scoreboard, player, objective, outputMode, maxAmount, globalSetting).runTaskTimer(plugin, 0L, period);
            }
            else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNote that scoreboard refreshing is disabled by your administrator. To update the scoreboard, repeat the command."));
            }

            return true;
        }
        else if(args[1].equalsIgnoreCase("hide")){

            try{
                refreshScoreboard.cancel();
            } catch(Exception ignored) {
            }

            if (globalSetting){
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                }
            } else {
                player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            }

            return true;
        }
        else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease provide an argument!"));
            sender.sendMessage(getSyntax());
            return false;
        }

    }
}
