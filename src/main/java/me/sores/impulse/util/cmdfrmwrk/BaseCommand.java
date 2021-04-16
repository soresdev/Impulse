package me.sores.impulse.util.cmdfrmwrk;

import me.sores.impulse.util.MessageUtil;
import me.sores.impulse.util.StringUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LavaisWatery on 10/24/2016.
 */
public abstract class BaseCommand implements TabExecutor {

    String name;
    String permission;
    CommandUsageBy usageBy;
    String[] aliases;
    String usage;
    int maxArgs;
    int minArgs;

    public BaseCommand(String name, String permission, CommandUsageBy usageBy, String... aliases) {
        this.name = name;
        this.permission = permission;
        this.usageBy = usageBy;
        this.aliases = aliases;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if ((!this.permission.isEmpty()) && (this.permission != null) && (!sender.hasPermission(this.permission) && !sender.hasPermission("*"))) {
            MessageUtil.noPermission(sender);
            return false;
        }
        if(sender instanceof Player && usageBy == CommandUsageBy.CONSOLE) {
            mustExecuteFromConsole(sender);
            return true;
        }
        else if(!(sender instanceof Player) && usageBy == CommandUsageBy.PLAYER) {
            mustExecuteByPlayer(sender);
            return true;
        }
        if ((this.maxArgs >= 0) &&
                (strings.length > this.maxArgs)) {
            if (getUsage() != null) {
                sender.sendMessage(StringUtil.getDefaultUsage() + getUsage().replace("<command>", s));
            }
            return true;
        }
        if ((this.minArgs >= 0) &&
                (strings.length < this.minArgs)) {
            if (getUsage() != null) {
                sender.sendMessage(StringUtil.getDefaultUsage() + getUsage().replace("<command>", s));
            }
            return true;
        }
        execute(sender, strings);

        return true;
    }

    public static boolean containsFlag(String[] args, String flag) {
        return Arrays.asList(args).contains(flag);
    }

    public static String[] filterFlags(String[] args) {
        List<String> aids = new ArrayList<>();

        for(String str : args) {
            if(!str.startsWith("-"))
                aids.add(str);
        }

        return aids.toArray(new String[aids.size()]);
    }

    public static HashMap<String, CommandFlag> getFlags(String[] args) {
        HashMap<String, CommandFlag> flags = new HashMap<>();

        for(String str : args) {
            if(str.startsWith("-")) {
                CommandFlag flag = new CommandFlag(str);

                if(flag.isValid())
                    flags.put(flag.getFlagName(), flag);
            }
        }

        return flags;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] strings)
    {
        return null;
    }

    public abstract void execute(CommandSender paramCommandSender, String[] paramArrayOfString);

    public String getName() {
        return this.name;
    }

    public String getPermission() {
        return this.permission;
    }

    public String[] getAliases() {
        return this.aliases;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getUsage() {
        return this.usage;
    }

    public void setMaxArgs(int maxArgs) {
        this.maxArgs = maxArgs;
    }

    public void setMinArgs(int minArgs) {
        this.minArgs = minArgs;
    }

    public void setArgRange(int minArgs, int maxArgs) {
        this.maxArgs = maxArgs;
        this.minArgs = minArgs;
    }

    public void mustExecuteFromConsole(CommandSender sender){ MessageUtil.message(sender, ChatColor.RED + "You must execute this command VIA console."); }

    public void mustExecuteByPlayer(CommandSender sender) { MessageUtil.message(sender, "You must execute this command in-game."); }

}
