package me.lifelessnerd.publicplaytime.eventhandlers;

import me.lifelessnerd.publicplaytime.filehandlers.PlaytimeDatabase;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Level;

public class PlaytimeHandler implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){

        Player player = playerJoinEvent.getPlayer();
        int currentValue;
        try{
            currentValue = Integer.parseInt(PlaytimeDatabase.get().getString(player.getName()));
        }catch(Exception exception){
            Bukkit.getLogger().info("Joined player was not in database: adding entry");
            FileConfiguration fileConfig = PlaytimeDatabase.get();
            fileConfig.set(player.getName(), 0);
            currentValue = 0;
            Bukkit.getLogger().info("Player was added, with value 0");
            PlaytimeDatabase.save();
        }

        //checks if currentValue differs from player.getStatistic; if so, there was a world change and we
        // warn the console of world changes; and paste the old value in console so that there is some sort
        // of backup for that player
        int serverPlayTime = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        if (Math.abs(currentValue - serverPlayTime) >= 100){ // difference is more than 5 seconds? Something is going on!
            Bukkit.getLogger().log(Level.WARNING, "There was a substantial difference in playtime between server and plugin! " +
                "Server records show player " + player.getName() + " has playtime of " + serverPlayTime + ", while the plugin" +
                " has the value of " + currentValue + "! Perhaps a world change has occurred. Please run a backup to preserve playtimes." +
                "\nNeed help? Run /playtime help.");
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent){

        Player player = playerQuitEvent.getPlayer();
        long timeStat =  player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        FileConfiguration fileConfiguration = PlaytimeDatabase.get();
        fileConfiguration.set(player.getName(), timeStat);
        PlaytimeDatabase.save();
        Bukkit.getLogger().info( "Player " + player.getName() + " was saved with value " + timeStat);

        // Set any scoreboard that is shown back to main scoreboard instead of new scoreboard; fixes issue #4
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());

    }

}
