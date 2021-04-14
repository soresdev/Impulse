package me.sores.spark.util.profile;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by sores on 4/14/2021.
 */
public abstract class PlayerProfile implements IPlayerProfile {

    private UUID uuid;

    public PlayerProfile(UUID uuid) {
        this.uuid = uuid;
    }

    public abstract void saveData();
    public abstract void loadData();
    public abstract Document createDocument();
    public abstract Document fetchCurrentObject();

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public String getName() {
        if(Bukkit.getPlayer(uuid) == null) return Bukkit.getOfflinePlayer(uuid).getName();

        return Bukkit.getPlayer(uuid).getName();
    }

    @Override
    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.uuid);
    }

    @Override
    public UUID getID() {
        return uuid;
    }
}
