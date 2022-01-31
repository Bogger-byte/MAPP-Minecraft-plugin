package me.bogger.mapp.managers;

import me.bogger.mapp.commands.ForceVisibleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandsManager implements CommandExecutor {

    private final ForceVisibleCommand forceVisibleSwitchCommand;

    public CommandsManager(ForceVisibleCommand forceVisibleSwitchCommand) {
        this.forceVisibleSwitchCommand = forceVisibleSwitchCommand;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             String[] args) {
        if (sender instanceof Player
                && command.getName().equalsIgnoreCase("force-visible"))
            forceVisibleSwitchCommand.execute(sender, args);
        return true;
    }
}
