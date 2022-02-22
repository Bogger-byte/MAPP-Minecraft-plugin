package me.bogger.mapp.plugin.commands.subCommands;

import me.bogger.mapp.plugin.commands.SubCommand;
import me.bogger.mapp.plugin.Main;
import me.bogger.mapp.plugin.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ForceVisibleSC extends SubCommand {

    private final ConfigManager config;

    public ForceVisibleSC() {
        setName("force-visible");
        this.config = Main.getInstance().getConfigManager();
    }

    @Override
    public void onCommand(@NotNull CommandSender sender,
                          @NotNull Command command,
                          @NotNull String[] args) {
        if (args.length == 0) {
            return;
        }

        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();

        if (args.length != 1) {
            sender.sendMessage("[mApp] Arguments required");
            return;
        }

        if (!(args[0].equals("true") || args[0].equals("false"))) {
            sender.sendMessage("[mApp] Invalid argument");
            return;
        }

        List<String> playersList = config.getConfig().getStringList("force-visible-players");
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

    @Override
    public String getPermission() {
        return null;
    }

}
