package me.sores.impulse;

import me.sores.impulse.commands.Command_impulse;
import me.sores.impulse.listeners.Listener_playerlistener;
import me.sores.impulse.util.abstr.AbstractInit;
import me.sores.impulse.util.menu.MenuListener;
import org.bukkit.plugin.Plugin;

/**
 * Created by sores on 4/16/2021.
 */
public class Init extends AbstractInit {

    private static Init instance;

    public Init(Plugin plugin) {
        super(plugin);
        instance = this;

        initInstances();
        registerCommands();
        registerEvents();
    }

    @Override
    public void initInstances() {

    }

    @Override
    public void registerEvents() {
        registerListener(new Listener_playerlistener(), new MenuListener());
    }

    @Override
    public void registerCommands() {
        registerCommand(getCommandRegistrar(), "impulse", new Command_impulse());
    }

    @Override
    public void unload() {

    }

    public static Init getInstance() {
        return instance;
    }
}
