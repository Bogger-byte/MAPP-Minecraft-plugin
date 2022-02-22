package me.bogger.mapp.plugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Messenger {
    public static void sendTo(CommandSender sender, String message) {
        sender.sendMessage("[mApp] " + message + ChatColor.RESET);
    }

    public static void broadcast(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendTo(player, message));
    }
}
