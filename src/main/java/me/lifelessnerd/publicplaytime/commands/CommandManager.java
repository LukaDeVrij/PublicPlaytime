package me.lifelessnerd.publicplaytime.commands;

import me.lifelessnerd.publicplaytime.commands.subcommands.PlaytimeGetCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {

    ArrayList<Subcommand> subcommands = new ArrayList<>();

    public CommandManager(){
        subcommands.add(new PlaytimeGetCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)){
            return false;
        }
        Player player = (Player) sender;

        if (args.length < 1){
            player.sendMessage("Please specify what function of PublicPlaytime to use."); //TODO: Add all options in a message
            return false;
        }

        for (int i = 0; i < getSubcommands().size(); i++){

            if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())){
                boolean result = getSubcommands().get(i).perform(player, args);
            }
            else {
                player.sendMessage("Not a valid argument."); //TODO: Add all options in a message same as above
            }

        }


        return true;
    }

    public ArrayList<Subcommand> getSubcommands(){
        return subcommands;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1){
            List<String> arguments = new ArrayList<>();
            arguments.add("get");
            arguments.add("scoreboard");
            arguments.add("ranking");
            return arguments;
        }

        // playtime get <player> <outputMode>
        if (args[0].equalsIgnoreCase("get")){
            if (args.length == 3){
                List<String> arguments = new ArrayList<>();
                arguments.add("standard");
                arguments.add("days");
                arguments.add("hours");
                arguments.add("minutes");
                arguments.add("seconds");
                arguments.add("ticks");
                return arguments;
            }
        }
        // Playtime ranking <amount>
        if (args[0].equalsIgnoreCase("ranking")){
            if (args.length == 2){
                List<String> arguments = new ArrayList<>();
                arguments.add("standard");
                arguments.add("days");
                arguments.add("hours");
                arguments.add("minutes");
                arguments.add("seconds");
                arguments.add("ticks");
                return arguments;
            }
            if (args.length == 3){
                List<String> arguments = new ArrayList<>();
                arguments.add("5");
                arguments.add("10");
                arguments.add("20");
                return arguments;
            }
        }

        // Playtime scoreboard show/hide <outputMode> <amount>
        if (args[0].equalsIgnoreCase("scoreboard")){
            if (args.length == 2){
                List<String> arguments = new ArrayList<>();
                arguments.add("show");
                arguments.add("hide");
                return arguments;
            }
            if (args.length == 3){
                List<String> arguments = new ArrayList<>();
                arguments.add("days");
                arguments.add("hours");
                arguments.add("minutes");
                arguments.add("seconds");
                arguments.add("ticks");
                return arguments;
            }
            if (args.length == 4){
                List<String> arguments = new ArrayList<>();
                arguments.add("5");
                arguments.add("10");
                arguments.add("15");
                return arguments;
            }
        }



        return null;
    }
}
