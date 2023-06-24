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

public class PlaytimeHandler implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){

        Player player = playerJoinEvent.getPlayer();

        try{
            int currentValue = Integer.parseInt(PlaytimeDatabase.get().getString(player.getName()));
        }catch(Exception exception){
            Bukkit.getLogger().info("Joined player was not in database: adding entry");
            FileConfiguration fileConfig = PlaytimeDatabase.get();
            fileConfig.set(player.getName(), 0);
            Bukkit.getLogger().info("Player was added, with value 0");
            PlaytimeDatabase.save();
        }

        //TODO: check if currentValue differs from player.getStatistic; if so, there was a world change and we might
        // wanna warn the console of world changes; and maybe paste the old value in console so that there is some sort
        // of backup?

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent){

        Player player = playerQuitEvent.getPlayer();
        long timeStat =  player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        FileConfiguration fileConfiguration = PlaytimeDatabase.get();
        fileConfiguration.set(player.getName(), timeStat);
        PlaytimeDatabase.save();
        Bukkit.getLogger().info( "Debug: player " + player + " was saved with value " + timeStat);

    }

}
