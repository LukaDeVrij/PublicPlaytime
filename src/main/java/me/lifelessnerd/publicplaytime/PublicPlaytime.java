package me.lifelessnerd.publicplaytime;

import me.lifelessnerd.publicplaytime.commands.GetPlaytime;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class PublicPlaytime extends JavaPlugin {

    @Override
    public void onEnable() {


        //Setup config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //Boilerplate
        getLogger().log(Level.ALL, "PublicPlaytime plugin started!\"");
        getServer().getPluginManager().registerEvents(new PlaytimeHandler(), this);
        getCommand("playtime").setExecutor(new GetPlaytime());

        //Database initializer
        PlaytimeDatabase.setup();
        PlaytimeDatabase.get().addDefault("Example", 216000000);
        PlaytimeDatabase.get().options().copyDefaults(true);
        PlaytimeDatabase.save();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
