package me.sores.impulse.commands;

import me.sores.impulse.Impulse;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import me.sores.impulse.util.command.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by sores on 3/4/2020.
 */
public class Command_impulse implements ICommand {

    private Impulse impulse;

    public Command_impulse(Impulse impulse) {
        this.impulse = impulse;
        register();
    }

    @Override
    public void execute(CommandSender sender, String... args) {
        if(sender instanceof Player){
            Player player = (Player) sender;

            if(!hasPermisson(player, "impulse.impulse")){
                sendPermissionMessage(player);
                return;
            }

            MessageUtil.message(player, "&a&lImpulse &7is currently running version: &a" + impulse.getDescription().getVersion());
        }else{
            sender.sendMessage(StringUtil.color("&cYou must be a player to use that command."));
        }
    }

    @Override
    public void register() {
        impulse.getCommand("impulse").setExecutor(this);
    }

    @Override
    public void sendPermissionMessage(Player player) {
        MessageUtil.noPermission(player);
    }

    @Override
    public boolean hasPermisson(Player player, String perm) {
        return player.hasPermission(perm);
    }
}
