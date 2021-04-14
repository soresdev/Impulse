package me.sores.spark.util.profile;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by sores on 4/14/2021.
 */
public interface IPlayerProfile {

    Player getPlayer();

    String getName();

    OfflinePlayer getOfflinePlayer();

    UUID getID();

}
