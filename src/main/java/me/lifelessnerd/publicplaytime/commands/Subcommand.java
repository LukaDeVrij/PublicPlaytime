package me.lifelessnerd.publicplaytime.commands;

import org.bukkit.entity.Player;

public abstract class Subcommand {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract boolean perform(Player player, String[] args);

}
