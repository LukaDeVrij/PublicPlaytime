package me.lifelessnerd.publicplaytime.commands.subcommands;

import me.lifelessnerd.publicplaytime.PublicPlaytime;
import me.lifelessnerd.publicplaytime.commands.Subcommand;
import me.lifelessnerd.publicplaytime.conversations.ConfirmLoadConversation;
import me.lifelessnerd.publicplaytime.conversations.ConfirmOverwriteConversation;
import me.lifelessnerd.publicplaytime.filehandlers.PlaytimeDatabase;
import me.lifelessnerd.publicplaytime.filehandlers.PlaytimeDatabaseBackup;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;

public class PlaytimeBackupLoadCommand extends Subcommand {

    PublicPlaytime plugin;

    public PlaytimeBackupLoadCommand(PublicPlaytime plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "load";
    }

    @Override
    public String getDescription() {
        return "Loads the playtimes from the backup file. Overwrites current data.";
    }

    @Override
    public String getSyntax() {
        return "/playtime load";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        if (!(player.hasPermission("publicplaytime.load"))){
            player.sendMessage("You do not have permission to do this!");
            return false;
        }

        //Copy from original file
        FileConfiguration databaseFile = PlaytimeDatabase.get();
        FileConfiguration backupFile = PlaytimeDatabaseBackup.get();
        Set<String> playerStrings = backupFile.getKeys(false);
        HashMap<String, Integer> fileContent = new HashMap<>();

        for (String playerString : playerStrings){
            fileContent.put(playerString, backupFile.getInt(playerString));
        }

        //Conversation to confirm overwrite
        String warn = "&c&lWarning! &rYou are about to overwrite &l&bALL playtimes&r. Do you want to proceed?";
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', warn));

        ConversationFactory cf = new ConversationFactory(plugin);
        Conversation conv = cf.withFirstPrompt(new ConfirmLoadConversation(databaseFile, playerStrings, backupFile, fileContent)).withLocalEcho(true).buildConversation(player);
        conv.begin();

        return false;
    }
}
