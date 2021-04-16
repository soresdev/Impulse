package me.sores.impulse.util.cmdfrmwrk;

import org.bukkit.command.CommandSender;

/**
 * Created by LavaisWatery on 10/25/2016.
 */
public interface IBaseCommand {

    void execute(CommandSender sender, String[] args);

}
