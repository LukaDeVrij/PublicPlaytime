package me.lifelessnerd.publicplaytime.commands;

import me.lifelessnerd.publicplaytime.PlaytimeDatabase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetPlaytime implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if (!(sender instanceof Player)){
            return false;
        }
        Player player = (Player) sender;
        if(args.length < 1){
            player.sendMessage("No arguments given.");
            return false;
        }
        String argument = args[0];
        try {
            int value = Integer.parseInt(PlaytimeDatabase.get().getString(argument));
        }catch(Exception exception){
            player.sendMessage("Player was not found in database.");
            return false;
        }

        player.sendMessage("Player " + argument + " has playtime " + PlaytimeDatabase.get().getString(argument));

        return true;
    }
}
