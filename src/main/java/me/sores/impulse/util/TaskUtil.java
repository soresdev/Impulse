package me.sores.impulse.util;

import me.sores.impulse.Impulse;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by sores on 4/22/2021.
 */
public class TaskUtil {

    public static void runTask(Plugin plugin, Runnable runnable, boolean async){
        if(async){
            plugin.getServer().getScheduler().runTaskAsynchronously(Impulse.getInstance(), runnable);
        }else{
            plugin.getServer().getScheduler().runTask(Impulse.getInstance(), runnable);
        }
    }

    public static void runTaskLater(Plugin plugin, Runnable runnable, long delay, boolean async){
        if(async){
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(Impulse.getInstance(), runnable, delay);
        }else{
            plugin.getServer().getScheduler().runTaskLater(Impulse.getInstance(), runnable, delay);
        }
    }

    public static void runTaskTimer(Plugin plugin, Runnable runnable, long delay, long timer, boolean async){
        if(async){
            plugin.getServer().getScheduler().runTaskTimerAsynchronously(Impulse.getInstance(), runnable, delay, timer);
        }else{
            plugin.getServer().getScheduler().runTaskTimer(Impulse.getInstance(), runnable, delay, timer);
        }
    }

    public static void runTaskTimer(Plugin plugin, BukkitRunnable runnable, long delay, long timer, boolean async){
        if(async){
            runnable.runTaskTimerAsynchronously(plugin, delay, timer);
        }else{
            runnable.runTaskTimer(plugin, delay, timer);
        }
    }

}
