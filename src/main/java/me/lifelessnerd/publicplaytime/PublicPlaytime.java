package me.lifelessnerd.publicplaytime;

import me.lifelessnerd.publicplaytime.commands.CommandManager;
import me.lifelessnerd.publicplaytime.eventhandlers.PlaytimeHandler;
import me.lifelessnerd.publicplaytime.filehandlers.PlaytimeDatabase;
import me.lifelessnerd.publicplaytime.filehandlers.PlaytimeDatabaseBackup;
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
        getServer().getPluginManager().registerEvents(new PlaytimeHandler(), this);
//        getCommand("getplaytime").setExecutor(new GetPlaytime());
//        getCommand("getplaytime").setTabCompleter(new GetPlaytime());
//        getCommand("playtimescoreboard").setExecutor(new ShowScoreboard(this));
//        getCommand("playtimescoreboard").setTabCompleter(new ShowScoreboard(this));
//        getCommand("playtimeranking").setExecutor(new ShowRankings());
//        getCommand("playtimeranking").setTabCompleter(new ShowRankings());

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
        headerCommentBackup.add("Backed up values will be stored here once backup as been run. Do not edit unless you know what you are doing!");
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
    }
}
