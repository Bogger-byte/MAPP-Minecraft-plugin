package me.bogger.mapp.plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

public class GeneralMappCommand implements CommandExecutor {

    private final HashMap<String, SubCommand> subCommandsMap;

    public GeneralMappCommand(SubCommand... subCommands) {
        this.subCommandsMap = new HashMap<>();
        for (SubCommand subCommand : subCommands) {
            this.subCommandsMap.put(subCommand.getName(), subCommand);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }

        String subCommandName = args[0];
        if (!subCommandsMap.containsKey(subCommandName)) {
            sender.sendMessage("No such sub-command");
            return false;
        }

        SubCommand subCommand = subCommandsMap.get(subCommandName);
        String[] shiftedArgs = Arrays.copyOfRange(args, 1, args.length);
        subCommand.onCommand(sender, command, shiftedArgs);

        return false;
    }
}
