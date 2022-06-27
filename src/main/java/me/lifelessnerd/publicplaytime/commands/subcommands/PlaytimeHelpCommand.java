package me.lifelessnerd.publicplaytime.commands.subcommands;

import me.lifelessnerd.publicplaytime.PublicPlaytime;
import me.lifelessnerd.publicplaytime.commands.Subcommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PlaytimeHelpCommand extends Subcommand {

    ArrayList<Subcommand> subcommands = new ArrayList<>();
    PublicPlaytime plugin;

    public PlaytimeHelpCommand(ArrayList<Subcommand> subcommands, PublicPlaytime plugin){
        this.subcommands = subcommands;
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Brings up help for the playtime command.";
    }

    @Override
    public String getSyntax() {
        return "/playtime help";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        String message = "&b------| PublicPlaytime Help Menu |------";
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        for (int i = 0; i < subcommands.size(); i++){
            message = "&6- /playtime " + subcommands.get(i).getName() + " : &r" + subcommands.get(i).getDescription();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message + "\n&7Usage: " + subcommands.get(i).getSyntax()));
        }
        return true;
    }
}
