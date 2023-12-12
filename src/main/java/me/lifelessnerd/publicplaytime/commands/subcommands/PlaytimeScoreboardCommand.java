package me.lifelessnerd.publicplaytime.commands.subcommands;

import me.lifelessnerd.publicplaytime.PlaytimeRanking;
import me.lifelessnerd.publicplaytime.PublicPlaytime;
import me.lifelessnerd.publicplaytime.commands.Subcommand;
import me.lifelessnerd.publicplaytime.tasks.RefreshScoreboard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
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
        String outputMode = "hours";
        try{
            outputMode = args[2];
            if (outputMode.equalsIgnoreCase("ticks") | outputMode.equalsIgnoreCase("minutes") |
                    outputMode.equalsIgnoreCase("seconds") | outputMode.equalsIgnoreCase("hours") |
                    outputMode.equalsIgnoreCase("days")){
            }
            else {
                outputMode = "hours";
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Your output mode was not recognised. Using hours."));
            }
        }catch(Exception exception){
            outputMode = "hours";
        }

//        Player player = (Player) sender;

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getMainScoreboard();
        TextComponent component = Component.text("Time Played");
        Objective objective;
        try {
            objective = scoreboard.registerNewObjective("Time Played", "dummy", component);
        } catch (Exception e){
            objective = scoreboard.getObjective("Time Played");
        }


        if (args[1].equalsIgnoreCase("show")){

            Objective prevObjective = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
            if (prevObjective != null){
                player.getPersistentDataContainer().set(new NamespacedKey(plugin, "prevObjective"), PersistentDataType.STRING, prevObjective.getName());
            } else {
                player.getPersistentDataContainer().set(new NamespacedKey(plugin, "prevObjective"), PersistentDataType.STRING, "none");
            }


            objective = scoreboard.getObjective("Time Played");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);


            int maxAmount = 10;
            try{
                maxAmount = Integer.parseInt(args[3]);
            }catch(Exception exception){
                maxAmount = 10;
            }

            PlaytimeRanking playtimeRanking = new PlaytimeRanking();
            HashSet<String> playerList = playtimeRanking.getPlayerNames();
            HashMap<String, String> rankedList = playtimeRanking.getRanking(playerList, maxAmount, outputMode);

            //Task one time execution to create the scoreboard
            BukkitTask createScoreboard = new RefreshScoreboard(plugin, scoreboard, player, objective, outputMode, maxAmount).runTask(plugin);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aScoreboard shown."));

            //Refresh
            if (plugin.getConfig().getBoolean("scoreboardRefreshAllowed")){

                int period = plugin.getConfig().getInt("scoreboardRefreshInterval");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7The scoreboard will be refreshed every " + period / 20f + " seconds. To cancel, hide the scoreboard using &o/playtime scoreboard hide"));
                logger.warning("The scoreboard will be refreshed every " + period / 20f + " seconds. This may cause performance to decrease. To see options regarding this feature, see config.yml!");
                try{
                    refreshScoreboard.cancel();
                }catch(Exception exception){}
                refreshScoreboard = new RefreshScoreboard(plugin, scoreboard, player, objective, outputMode, maxAmount).runTaskTimer(plugin, 0L, period);
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


            String prevObjectiveName = player.getPersistentDataContainer().get(new NamespacedKey(plugin, "prevObjective"), PersistentDataType.STRING);
            if (!(prevObjectiveName.equals("none"))){
                Objective originalObjective = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(prevObjectiveName);
                originalObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aScoreboard hidden. Your old scoreboard was put back."));
            } else {
                Bukkit.getScoreboardManager().getMainScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                Bukkit.getScoreboardManager().getMainScoreboard().getObjective("Time Played").unregister();
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aScoreboard hidden. There was no old scoreboard to show."));
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
