package me.bogger.mapp.managers;

import me.bogger.mapp.commands.ForceVisibleCommand;
import me.bogger.mapp.commands.PublishAllRegionsCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandsManager implements CommandExecutor {

    private final ForceVisibleCommand forceVisibleSwitchCommand;
    public final PublishAllRegionsCommand publishAllRegionsCommand;

    public CommandsManager(ForceVisibleCommand forceVisibleSwitchCommand,
                           PublishAllRegionsCommand loadAllRegionsCommand) {
        this.forceVisibleSwitchCommand = forceVisibleSwitchCommand;
        this.publishAllRegionsCommand = loadAllRegionsCommand;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             String[] args) {
        if (sender instanceof Player
                && command.getName().equalsIgnoreCase("force-visible"))
            forceVisibleSwitchCommand.execute(sender, args);
        if (sender instanceof ConsoleCommandSender
                && command.getName().equalsIgnoreCase("publish-all-regions"))
            publishAllRegionsCommand.execute(sender, args);
        return true;
    }
}
