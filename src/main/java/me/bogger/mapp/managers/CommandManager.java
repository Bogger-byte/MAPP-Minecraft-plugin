package me.bogger.mapp.managers;

import me.bogger.mapp.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {

    public CommandManager(Main plugin) {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reauthenticate")) {

        }
        if (command.getName().equalsIgnoreCase("verify-server")) {

        }
        if (command.getName().equalsIgnoreCase("force-visible")) {

        }
        return true;
    }
}
