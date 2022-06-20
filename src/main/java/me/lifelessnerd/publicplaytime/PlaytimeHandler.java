package me.lifelessnerd.publicplaytime;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlaytimeHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){

        Player player = playerJoinEvent.getPlayer();
        player.sendMessage(player.getPlayer() + " joined at " + System.currentTimeMillis());

    }

    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent){

        Player player = playerQuitEvent.getPlayer();
    }
}
