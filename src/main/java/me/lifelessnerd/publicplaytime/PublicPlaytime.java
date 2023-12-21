package me.lifelessnerd.publicplaytime;

import me.lifelessnerd.publicplaytime.commands.CommandManager;
import me.lifelessnerd.publicplaytime.eventhandlers.PlaytimeHandler;
import me.lifelessnerd.publicplaytime.filehandlers.PlaytimeDatabase;
import me.lifelessnerd.publicplaytime.filehandlers.PlaytimeDatabaseBackup;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Logger;

public final class PublicPlaytime extends JavaPlugin {

    public Logger log;

    @Override
    public void onEnable() {


        //Setup config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Boilerplate
        log = getLogger();
        log.info("PublicPlaytime plugin started!");
        getServer().getPluginManager().registerEvents(new PlaytimeHandler(this), this);

        getCommand("playtime").setExecutor(new CommandManager(this));


        //Database initializer
        PlaytimeDatabase.setup();
        ArrayList<String> headerComment = new ArrayList<String>();
        headerComment.add("# DATABASE FILE FOR PLAYTIMES\n");
        headerComment.add("# Editing will have no effect, since playtime stats are pulled from the Spigot API embedded in your server\n");
        headerComment.add("# Formatting is as follows:\n");
        headerComment.add("# PlayerName: value in ticks\n");
        headerComment.add("# Example: 7200\n");

        PlaytimeDatabase.get().options().setHeader(headerComment);
        PlaytimeDatabase.get().options().copyDefaults(true);
        PlaytimeDatabase.save();

        //Backup initializer
        PlaytimeDatabaseBackup.setup();
        ArrayList<String> headerCommentBackup = new ArrayList<String>();
        headerCommentBackup.add("Backed up values will be stored here once backup has been run. Only edit if you know what you are doing.");
        headerCommentBackup.add("Please know that when editing this manually, for the plugin to see any effect you must restart/reload the server!");
        PlaytimeDatabaseBackup.get().options().setHeader(headerCommentBackup);
        PlaytimeDatabaseBackup.get().options().copyDefaults(true);
        PlaytimeDatabaseBackup.save();

        //Config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        // If server is stopped, make sure every playtime is updated (onQuit is never called in this case)
        for (Player player : Bukkit.getOnlinePlayers()){
            long timeStat =  player.getStatistic(Statistic.PLAY_ONE_MINUTE);
            FileConfiguration fileConfiguration = PlaytimeDatabase.get();
            fileConfiguration.set(player.getName(), timeStat);
            PlaytimeDatabase.save();
            getLogger().info( "Player " + player.getName() + " was saved with value " + timeStat);
        }


    }
}
