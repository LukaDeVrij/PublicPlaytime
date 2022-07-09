package me.lifelessnerd.publicplaytime.commands.subcommands;

import me.lifelessnerd.publicplaytime.PublicPlaytime;
import me.lifelessnerd.publicplaytime.commands.Subcommand;
import me.lifelessnerd.publicplaytime.conversations.ConfirmOverwriteConversation;
import me.lifelessnerd.publicplaytime.filehandlers.PlaytimeDatabase;
import me.lifelessnerd.publicplaytime.filehandlers.PlaytimeDatabaseBackup;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PlaytimeBackupCommand extends Subcommand {

    PublicPlaytime plugin;

    public PlaytimeBackupCommand(PublicPlaytime plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "backup";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"save"};
    }

    @Override
    public String getDescription() {
        return "Writes a backup file of the playtime at the current moment";
    }

    @Override
    public String getSyntax() {
        return "/playtime backup";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        // Permission check
        if(!(player.hasPermission("publicplaytime.backup"))){
            player.sendMessage("You do not have permission to do this!");
            return false;
        }

        // Update scores for online players so that the backup is accurate
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            long timeStat =  onlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE);
            FileConfiguration fileConfiguration = PlaytimeDatabase.get();
            fileConfiguration.set(onlinePlayer.getName(), timeStat);
            PlaytimeDatabase.save();
        }


        //Copy from original file
        FileConfiguration databaseFile = PlaytimeDatabase.get();
        Set<String> playerStrings = databaseFile.getKeys(false);

        HashMap<String, Integer> fileContent = new HashMap<>();

        for (String playerString : playerStrings){
            fileContent.put(playerString, databaseFile.getInt(playerString));
        }
        // Checking backup file and notify player when you're about to overwrite
        FileConfiguration backupFile = PlaytimeDatabaseBackup.get();

        if(backupFile.getKeys(false).size() > 0){

            //Conversation to confirm overwrite
            String warn = "&cWarning! &rYou are about to overwrite the backup file. Do you want to proceed?";
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', warn));

            //Clear backup-database
            for (String path : backupFile.getKeys(false)){
                backupFile.set(path, null);
            }

            ConversationFactory cf = new ConversationFactory(plugin);
            Conversation conv = cf.withFirstPrompt(new ConfirmOverwriteConversation(databaseFile, playerStrings, backupFile, fileContent)).withLocalEcho(true).buildConversation(player);
            conv.begin();

        }else {
            // If you're not about to overwrite, just paste content into backup file
            // Putting copied content in backup file

            for (String playerString : playerStrings) {
                int value = fileContent.get(playerString);
                backupFile.set(playerString, value);
            }
            PlaytimeDatabaseBackup.save();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aBackup made."));
        }

        return false;
    }
}
