package me.bogger.mapp.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface CommandExecutor {
    boolean execute(@NotNull CommandSender sender, String[] args);
}
