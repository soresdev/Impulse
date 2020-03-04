package me.sores.spark.util.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 3/4/2020.
 */
public interface ICommand extends CommandExecutor {

    @Override
    default boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        execute(sender, args);
        return true;
    }

    void execute(CommandSender sender, String... args);
    void register();
    void sendPermissionMessage(Player player);
}
