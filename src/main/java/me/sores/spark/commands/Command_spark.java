package me.sores.spark.commands;

import me.sores.spark.Spark;
import me.sores.spark.util.StringUtil;
import me.sores.spark.util.command.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 3/4/2020.
 */
public class Command_spark implements ICommand {

    private Spark spark;

    public Command_spark(Spark spark) {
        this.spark = spark;
        register();
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            if(!player.hasPermission("spark.spark")){
                sendPermissionMessage(player);
                return;
            }

            player.sendMessage(StringUtil.color("&a&lSpark &7is currently running version: &a" + spark.getDescription().getVersion()));
        }else{
            sender.sendMessage(StringUtil.color("&cYou must be a player to use that command."));
        }
    }

    @Override
    public void register() {
        spark.getCommand("spark").setExecutor(this);
    }

    @Override
    public void sendPermissionMessage(Player player) {
        player.sendMessage(StringUtil.color("&cNo Permission."));
    }

    @Override
    public boolean hasPermisson(Player player, String perm) {
        return false;
    }
}
