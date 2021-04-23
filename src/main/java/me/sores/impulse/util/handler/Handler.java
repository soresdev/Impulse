package me.sores.impulse.util.handler;

import org.bukkit.event.Listener;

/**
 * Created by sores on 4/20/2021.
 */
public abstract class Handler implements Listener {

    public abstract void init();
    public abstract void unload();

}
