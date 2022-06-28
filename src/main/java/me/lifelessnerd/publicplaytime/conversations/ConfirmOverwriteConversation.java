package me.lifelessnerd.publicplaytime.conversations;

import me.lifelessnerd.publicplaytime.filehandlers.PlaytimeDatabase;
import me.lifelessnerd.publicplaytime.filehandlers.PlaytimeDatabaseBackup;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Set;

public class ConfirmOverwriteConversation extends StringPrompt {

    FileConfiguration databaseFile;
    Set<String> playerStrings;
    FileConfiguration backupFile;
    HashMap<String, Integer> fileContent;

    public ConfirmOverwriteConversation(FileConfiguration databaseFile, Set<String> playerStrings, FileConfiguration backupFile, HashMap<String, Integer> fileContent) {
        this.databaseFile = databaseFile;
        this.playerStrings = playerStrings;
        this.backupFile = backupFile;
        this.fileContent = fileContent;
    }

    @Override
    public @NotNull String getPromptText(@NotNull ConversationContext context) {
        return "Answer either YES or NO";
    }

    @Override
    public @Nullable Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {


        if (input.equalsIgnoreCase("yes") | input.equalsIgnoreCase("y")){
            for (String playerString : fileContent.keySet()) {
                int value = fileContent.get(playerString);
                backupFile.set(playerString, value);
            }
            PlaytimeDatabaseBackup.save();
            context.getForWhom().sendRawMessage(ChatColor.translateAlternateColorCodes('&', "&aBackup made."));
        }
        else {
            context.getForWhom().sendRawMessage(ChatColor.translateAlternateColorCodes('&', "&cBackup cancelled."));
        }

        return null;
    }
}
