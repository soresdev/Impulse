package me.sores.impulse.commands;

import me.sores.impulse.Impulse;
import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.cmdfrmwrk.BaseCommand;
import me.sores.impulse.util.cmdfrmwrk.CommandUsageBy;
import org.bukkit.command.CommandSender;

/**
 * Created by sores on 3/4/2020.
 */
public class Command_impulse extends BaseCommand {

    public Command_impulse() {
        super("impulse", "impulse.impulse", CommandUsageBy.ALL);
        setUsage("/<command>");
        setMinArgs(0);
        setMaxArgs(0);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("impulse.impulse")){
            MessageUtil.message(sender, "&a&lImpulse &7is currently running version: &a" + Impulse.getInstance().getDescription().getVersion());
        }
    }

}
