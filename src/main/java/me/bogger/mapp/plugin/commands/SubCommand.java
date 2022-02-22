package me.bogger.mapp.plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class SubCommand{
    private String name;

    public abstract void onCommand(CommandSender sender,
                                      Command command,
                                      String[] args);
    public abstract String getPermission();

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
