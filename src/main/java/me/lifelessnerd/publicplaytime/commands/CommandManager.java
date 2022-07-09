package me.lifelessnerd.publicplaytime.commands;

import me.lifelessnerd.publicplaytime.PublicPlaytime;
import me.lifelessnerd.publicplaytime.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager implements TabExecutor {

    ArrayList<Subcommand> subcommands = new ArrayList<>();

    public CommandManager(PublicPlaytime plugin){
        subcommands.add(new PlaytimeGetCommand());
        subcommands.add(new PlaytimeRankingCommand());
        subcommands.add(new PlaytimeScoreboardCommand(plugin));
        subcommands.add(new PlaytimeHelpCommand(subcommands, plugin));
        subcommands.add(new PlaytimeAboutCommand(plugin));
        subcommands.add(new PlaytimeBackupCommand(plugin));
        subcommands.add(new PlaytimeBackupLoadCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage("The console cannot perform PublicPlaytime commands.");
            return false;
        }
        Player player = (Player) sender;

        if (args.length < 1){
            player.sendMessage("Please specify what function of PublicPlaytime to use."); //TODO: Add all options in a message
            return false;
        }

        //Check for names of subcommands in arg
        for (int i = 0; i < getSubcommands().size(); i++){
            if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())){
                boolean result = getSubcommands().get(i).perform(player, args);
                return true; // All help dialogs are done in-class with player.sendMessage
            }
        }
        //Check for aliases of subcommands in arg
        for (int i = 0; i < getSubcommands().size(); i++) {
            if (Arrays.asList(getSubcommands().get(i).getAliases()).contains(args[0])) {
//                player.sendMessage("Alias used.");
                boolean result = getSubcommands().get(i).perform(player, args);
                return true; // All help dialogs are done in-class with player.sendMessage
            }
        }


        player.sendMessage(args[0] + " is not a valid sub-command."); //TODO: Add all options in a message same as above
        return false;

    }

    public ArrayList<Subcommand> getSubcommands(){
        return subcommands;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 1){
            List<String> arguments = new ArrayList<>();
            for (int i = 0; i < getSubcommands().size(); i++){
                arguments.add(getSubcommands().get(i).getName());
            }
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
            if (args.length == 3 && args[1].equalsIgnoreCase("show")){
                List<String> arguments = new ArrayList<>();
                arguments.add("days");
                arguments.add("hours");
                arguments.add("minutes");
                arguments.add("seconds");
                arguments.add("ticks");
                return arguments;
            }
            if (args.length == 4 && args[1].equalsIgnoreCase("show")){
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
