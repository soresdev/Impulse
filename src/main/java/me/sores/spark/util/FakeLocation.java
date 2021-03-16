package me.sores.spark.util;

import me.sores.spark.Spark;
import me.sores.spark.util.serialization.interf.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

/**
 * Created by LavaisWatery on 11/7/2016.
 */
public class FakeLocation implements Serializable {

    void FakeLocation(String worldName, double x, double y, double z, float yaw, float pitch) {
        this.worldName = worldName;

        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public FakeLocation(String worldName, double x, double y, double z) {
        FakeLocation(worldName, x, y, z, 0, 0);
    }

    public FakeLocation(String worldName, double x, double y, double z, float yaw, float pitch) {
        FakeLocation(worldName, x, y, z, yaw, pitch);
    }

    public FakeLocation(Location location) {
        FakeLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public FakeLocation(World world, double x, double y, double z) {
        FakeLocation(world.getName(), x, y, z, 0, 0);
    }

    public FakeLocation(World world, double x, double y, double z, float yaw, float pitch) {
        FakeLocation(world.getName(), x, y, z, yaw, pitch);
    }

    public FakeLocation(String input) {
        deserialize(input);
    }

    private String worldName;
    private double x, y, z;
    private float yaw, pitch;

    public Location toLocation() {
        return isWorldLoaded() ? new Location(getWorld(), x, y, z, yaw, pitch) : null;
    }

    public boolean isWorldLoaded() {
        return getWorld() != null;
    }

    public String getWorldName() {
        return worldName;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public static FakeLocation findRandom(List<FakeLocation> loo) {
        int rand = loo != null ? Spark.RAND.nextInt(loo.size()) : -1;

        return loo != null && !loo.isEmpty() ? loo.get(rand) : null;
    }

    @Override // worldName||
    public String serialize() {
        return worldName + ":" + x + ":" + y + ":" + z + ":" + yaw + ":" + pitch;
    }

    @Override
    public void deserialize(String str) {
        String[] split = str.split(":");

        worldName = split[0];
        x = Double.parseDouble(split[1]);
        y = Double.parseDouble(split[2]);
        z = Double.parseDouble(split[3]);
        yaw = Float.parseFloat(split[4]);
        pitch = Float.parseFloat(split[5]);
    }

    public static String staticSerialize(Location location) {
        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

}
