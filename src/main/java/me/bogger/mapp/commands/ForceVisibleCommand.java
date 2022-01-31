package me.bogger.mapp.commands;

import me.bogger.mapp.MappConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ForceVisibleCommand implements CommandExecutor {

    private final MappConfig config;

    public ForceVisibleCommand(MappConfig config) {
        this.config = config;
    }

    private boolean isBoolean(String s) {
        return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, String[] args) {
        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();

        List<String> playersList = config.getConfig().getStringList("force-visible-players");
        if (args.length == 1 && isBoolean(args[0])) {
            boolean setVisible = Boolean.parseBoolean(args[0]);
            if (setVisible && !playersList.contains(uuid)) {
                playersList.add(uuid);
                sender.sendMessage("[mApp] " + ChatColor.GREEN + "You are visible to other players");
            }
            if (!setVisible) {
                playersList.remove(uuid);
                sender.sendMessage("[mApp] " + ChatColor.YELLOW + "You are invisible to other players");
            }
            config.getConfig().set("force-visible-players", playersList);
            config.save();
        }

        return false;
    }
}
