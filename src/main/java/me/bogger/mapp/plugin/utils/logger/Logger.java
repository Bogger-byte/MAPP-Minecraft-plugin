package me.bogger.mapp.plugin.utils.logger;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class Logger {

    private static final boolean isDebug = true;

    public static void log(Level level, String message) {
        String messageColor = "";
        switch (level) {
            case OFF:
            case WARNING:
                messageColor = AnsiColor.RED;
                break;
            case SEVERE:
                messageColor = AnsiColor.BACKGROUND_RED;
                break;
            case INFO:
            case CONFIG:
                messageColor = AnsiColor.YELLOW;
                break;
            case FINE:
                messageColor = AnsiColor.GREEN;
                break;
            case FINER:
            case FINEST:
                messageColor = AnsiColor.GREEN + AnsiColor.HIGH_INTENSITY;
                break;
            case ALL:
                messageColor = AnsiColor.BACKGROUND_YELLOW;
                break;
        }
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage("[mApp] " + messageColor + message + AnsiColor.RESET);
    }

    public static void logDebug(Level level, String message) {
        if (isDebug) log(level, AnsiColor.HIGH_INTENSITY + "DEBUG: " + AnsiColor.RESET + message);
    }
}
