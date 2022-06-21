package me.lifelessnerd.publicplaytime;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

public class PlaytimeHandler implements Listener {


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){

        Player player = playerJoinEvent.getPlayer();

        try{
            int currentValue = Integer.parseInt(PlaytimeDatabase.get().getString(player.getName()));
        }catch(Exception exception){
            Bukkit.getLogger().warning("Joined player was not in database: adding entry");
            FileConfiguration fileConfig = PlaytimeDatabase.get();
            fileConfig.set(player.getName(), 0);
            Bukkit.getLogger().warning("Player was added, with value 0");
            PlaytimeDatabase.save();
        }

    }

    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent){

        Player player = playerQuitEvent.getPlayer();
        long timeStat =  player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        FileConfiguration fileConfiguration = PlaytimeDatabase.get();
        fileConfiguration.set(player.getName(), timeStat);
        PlaytimeDatabase.save();
        Bukkit.getLogger().warning( "Debug: player " + player + " was saved with value " + timeStat);

    }

}
