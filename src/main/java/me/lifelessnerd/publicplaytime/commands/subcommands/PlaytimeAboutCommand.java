package me.lifelessnerd.publicplaytime.commands.subcommands;

import me.lifelessnerd.publicplaytime.PublicPlaytime;
import me.lifelessnerd.publicplaytime.commands.Subcommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Properties;

public class PlaytimeAboutCommand extends Subcommand {

    public PublicPlaytime plugin;
    public PlaytimeAboutCommand(PublicPlaytime plugin){
        this.plugin = plugin;
    }


    @Override
    public String getName() {
        return "about";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"info", "version"};
    }

    @Override
    public String getDescription() {
        return "Info/version about the PublicPlaytime plugin.";
    }

    @Override
    public String getSyntax() {
        return "/playtime about";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        String message =
        """       
        &bPublicPlaytime - version 1.2.5
        &6SpigotMC: &ohttps://bit.ly/PublicPlaytimeSpigotMC
        &6Discord Support: &ohttps://discord.gg/8n65jSEkVf
        &6Github: &ohttps://github.com/LifelessNerd/PublicPlaytime
        &6Developer: &ohttps://twitter.com/LukaDeVrij
        &rIf you have any problems, bugs or glitches, reach out to me via any of the links above.
        """;
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

        return true;
    }
}
