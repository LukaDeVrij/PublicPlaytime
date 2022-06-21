package me.lifelessnerd.publicplaytime;

import me.lifelessnerd.publicplaytime.commands.GetPlaytime;
import me.lifelessnerd.publicplaytime.commands.ShowScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
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
        getCommand("playtime").setExecutor(new GetPlaytime());
        getCommand("playtime").setTabCompleter(new GetPlaytime());
        getCommand("playtimescoreboard").setExecutor(new ShowScoreboard());
        getCommand("playtimescoreboard").setTabCompleter(new ShowScoreboard());

        //Database initializer
        PlaytimeDatabase.setup();
        PlaytimeDatabase.get().options().copyDefaults(true);
        PlaytimeDatabase.save();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
