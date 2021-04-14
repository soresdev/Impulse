package me.sores.impulse.util;

import com.google.common.collect.Lists;
import me.sores.impulse.Impulse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Created by LavaisWatery on 10/24/2016.
 */
public class LocationUtil {

    private static final Set<Material> SOLID_MATERIAL_WHITELIST = new HashSet<>();
    private static final Set<Integer> SPECIAL_SOLID_MATERIAL_ID_WHITELIST = new HashSet<>(); //Fences, etc

    static {
        SOLID_MATERIAL_WHITELIST.addAll(Arrays.asList(Material.SNOW, Material.SNOW_BLOCK, Material.CARPET, Material.DIODE,
                Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF,
                Material.REDSTONE_COMPARATOR_ON, Material.SKULL, Material.SKULL_ITEM, Material.LADDER, Material.WATER_LILY));

        SPECIAL_SOLID_MATERIAL_ID_WHITELIST.addAll(Arrays.asList(85, 188, 189, 190, 191, 192, 113, 107, 183, 184, 185, 186, 187, 139, 65));
    }

    public static Location setupLocation(Location location, double x, double y, double z) {
        location.setX(x);
        location.setY(y);
        location.setZ(z);

        return location;
    }

    public static Vector makeVectorsSafeAgain(Vector vel) {
        if((vel.getX() > 4.0D)) {
            vel.setX(4.0D);
        }
        if((vel.getX() < -4.0D)) {
            vel.setX(-4.0D);
        }
        if((vel.getY() > 4.0D)) {
            vel.setY(4.0D);
        }
        if((vel.getY() < -4.0D)) {
            vel.setY(-4.0D);
        }
        if((vel.getZ() > 4.0D)) {
            vel.setZ(4.0D);
        }
        if((vel.getZ() < -4.0D)) {
            vel.setZ(-4.0D);
        }

        return vel;
    }

    public static boolean isSolid(Material material) {
        if (material.isSolid() || SOLID_MATERIAL_WHITELIST.contains(material)) {
            return true;
        } else if (SPECIAL_SOLID_MATERIAL_ID_WHITELIST.contains(material.getId())) {
            return true;
        }
        return false;
    }

    public static Vector getRandomCircleVector() {
        double rnd, x, z;
        rnd = Impulse.RAND.nextDouble() * 2 * Math.PI;
        x = Math.cos(rnd);
        z = Math.sin(rnd);

        return new Vector(x, 0, z);
    }

    public boolean isOnGround(Location location) {
        for (Location loc : getLocationsAround(location)) {
            Material below = loc.clone().add(0, -1.0E-13D, 0).getBlock().getType();
            if (below.isSolid() || SOLID_MATERIAL_WHITELIST.contains(below)) {
                return true;
            }
            //For when standing on fences, etc.
            Material specialBelow = loc.clone().add(0, -0.5000000000001D, 0).getBlock().getType();
            if (SPECIAL_SOLID_MATERIAL_ID_WHITELIST.contains(specialBelow.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Distance horizontal from and to.
     *
     * @param from the start location
     * @param to   the final location
     * @return the horizontal distance.
     */
    public static double distanceHorizontal(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }

    /**
     * Distance vertical from and to.
     *
     * @param from the start location
     * @param to   the final location
     * @return the vertical distance.
     */
    public static double distanceVertical(Location from, Location to) {
        double dy = to.getY() - from.getY();
        return Math.sqrt(dy * dy);
    }


    //Including the location you pass
    public List<Location> getLocationsAround(Location location) {
        List<Location> locations = new ArrayList<>();
        for (double x = -0.3D; x <= 0.3D; x += 0.3D) {
            for (double z = -0.3D; z <= 0.3D; z += 0.3D) {
                locations.add(location.clone().add(x, 0, z));
            }
        }

        locations.add(location);

        return locations;
    }

    public List<Block> getBlocksAround(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for (double x = -radius; x <= radius; x++) {
            for (double y = -radius; y <= radius; y++)
                for (double z = -radius; z <= radius; z++) {
                    blocks.add(location.clone().add(x, 0, z).getBlock());
                }
        }
        blocks.add(location.getBlock());

        return blocks;
    }

    public static String serializeLocation(Location location) {
        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    private boolean isPlayerInCone(Player p, Player clicked, double coneAngle, double radius) {
        final double coneArea = Math.tan(coneAngle) * Math.tan(coneAngle), radiusSquared = radius * radius;
        Vector n = p.getLocation().toVector().subtract(clicked.getLocation().toVector()).normalize();
        return (clicked.getLocation().getDirection().normalize().crossProduct(n).lengthSquared() <= coneArea // within cone
                && clicked.getLocation().distanceSquared(p.getLocation()) <= radiusSquared // within radius
                && clicked.getLocation().getDirection().dot(n) >= 0); // same direction
    }

    public static Location deserializeLocation(String input)
    {
        String[] split = input.split(":");

        String worldName = split[0];
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        float yaw = Float.parseFloat(split[4]);
        float pitch = Float.parseFloat(split[5]);

        World wurld = Bukkit.getServer().getWorld(worldName);
        return wurld != null ? new Location(wurld, x, y, z, yaw, pitch) : null;
    }

    public static void assureChunk(Player player) {
        assureChunk(player.getLocation());
    }

    public static void assureChunk(Location loc) {
        if(loc != null && !loc.getChunk().isLoaded()) {
            loc.getChunk().load();
        }
    }

    public static boolean isOnGround(final Location location, final int down) {
        final double posX = location.getX();
        final double posZ = location.getZ();
        final double fracX = (MathUtil.getFraction(posX) > 0.0) ? Math.abs(MathUtil.getFraction(posX)) : (1.0 - Math.abs(MathUtil.getFraction(posX)));
        final double fracZ = (MathUtil.getFraction(posZ) > 0.0) ? Math.abs(MathUtil.getFraction(posZ)) : (1.0 - Math.abs(MathUtil.getFraction(posZ)));
        final int blockX = location.getBlockX();
        final int blockY = location.getBlockY() - down;
        final int blockZ = location.getBlockZ();
        final World world = location.getWorld();
        if (isSolid(world.getBlockAt(blockX, blockY, blockZ))) {
            return true;
        }
        if (fracX < 0.3) {
            if (isSolid(world.getBlockAt(blockX - 1, blockY, blockZ))) {
                return true;
            }
            if (fracZ < 0.3) {
                if (isSolid(world.getBlockAt(blockX - 1, blockY, blockZ - 1))) {
                    return true;
                }
                if (isSolid(world.getBlockAt(blockX, blockY, blockZ - 1))) {
                    return true;
                }
                if (isSolid(world.getBlockAt(blockX + 1, blockY, blockZ - 1))) {
                    return true;
                }
            }
            else if (fracZ > 0.7) {
                if (isSolid(world.getBlockAt(blockX - 1, blockY, blockZ + 1))) {
                    return true;
                }
                if (isSolid(world.getBlockAt(blockX, blockY, blockZ + 1))) {
                    return true;
                }
                if (isSolid(world.getBlockAt(blockX + 1, blockY, blockZ + 1))) {
                    return true;
                }
            }
        }
        else if (fracX > 0.7) {
            if (isSolid(world.getBlockAt(blockX + 1, blockY, blockZ))) {
                return true;
            }
            if (fracZ < 0.3) {
                if (isSolid(world.getBlockAt(blockX - 1, blockY, blockZ - 1))) {
                    return true;
                }
                if (isSolid(world.getBlockAt(blockX, blockY, blockZ - 1))) {
                    return true;
                }
                if (isSolid(world.getBlockAt(blockX + 1, blockY, blockZ - 1))) {
                    return true;
                }
            }
            else if (fracZ > 0.7) {
                if (isSolid(world.getBlockAt(blockX - 1, blockY, blockZ + 1))) {
                    return true;
                }
                if (isSolid(world.getBlockAt(blockX, blockY, blockZ + 1))) {
                    return true;
                }
                if (isSolid(world.getBlockAt(blockX + 1, blockY, blockZ + 1))) {
                    return true;
                }
            }
        }
        else if (fracZ < 0.3) {
            if (isSolid(world.getBlockAt(blockX, blockY, blockZ - 1))) {
                return true;
            }
        }
        else if (fracZ > 0.7 && isSolid(world.getBlockAt(blockX, blockY, blockZ + 1))) {
            return true;
        }
        return false;
    }

    public static boolean isLiquid(final Block block) {
        return block != null && (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER || block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA);
    }

    public static boolean isSolid(final Block block) {
        return block != null && isSolid(block.getTypeId());
    }

    public static boolean isSolid(final int block) {
        return isSolid((byte)block);
    }

    public static HashSet<Byte> blockPassSet = new HashSet<>();

    public static boolean isSolid(final byte block) {
        if (blockPassSet.isEmpty()) {
            blockPassSet.add((byte)0);
            blockPassSet.add((byte)6);
            blockPassSet.add((byte)8);
            blockPassSet.add((byte)9);
            blockPassSet.add((byte)10);
            blockPassSet.add((byte)11);
            blockPassSet.add((byte)27);
            blockPassSet.add((byte)28);
            blockPassSet.add((byte)30);
            blockPassSet.add((byte)31);
            blockPassSet.add((byte)32);
            blockPassSet.add((byte)37);
            blockPassSet.add((byte)38);
            blockPassSet.add((byte)39);
            blockPassSet.add((byte)40);
            blockPassSet.add((byte)50);
            blockPassSet.add((byte)51);
            blockPassSet.add((byte)55);
            blockPassSet.add((byte)59);
            blockPassSet.add((byte)63);
            blockPassSet.add((byte)66);
            blockPassSet.add((byte)68);
            blockPassSet.add((byte)69);
            blockPassSet.add((byte)70);
            blockPassSet.add((byte)72);
            blockPassSet.add((byte)75);
            blockPassSet.add((byte)76);
            blockPassSet.add((byte)77);
            blockPassSet.add((byte)78);
            blockPassSet.add((byte)83);
            blockPassSet.add((byte)90);
            blockPassSet.add((byte)104);
            blockPassSet.add((byte)105);
            blockPassSet.add((byte)115);
            blockPassSet.add((byte)119);
            blockPassSet.add((byte)(-124));
            blockPassSet.add((byte)(-113));
            blockPassSet.add((byte)(-81));
            blockPassSet.add((byte)(-85));
        }
        return !blockPassSet.contains(block);
    }

    public static void addToVector(double x, double y, double z, Vector vector) {
        vector.setX(vector.getX() + x);
        vector.setY(vector.getY() + y);
        vector.setZ(vector.getZ() + z);
    }

    public static void setupVector(double x, double y, double z, Vector vector)
    {
        vector.setX(x);
        vector.setY(y);
        vector.setZ(z);
    }

    public static FakeLocation deserializeLocationToFake(String input)
    {
        return new FakeLocation(input);
    }

    public static List<Block> blocksFromTwoPoints(Location loc1, Location loc2)
    {
        List<Block> blocks = new ArrayList<Block>();

        int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

        int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++)
        {
            for (int z = bottomBlockZ; z <= topBlockZ; z++)
            {
                for (int y = bottomBlockY; y <= topBlockY; y++)
                {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

    public static Location getHighestAt(Location loc)
    {
        loc.setY(loc.getWorld().getMaxHeight());
        int tries = 0;
        while (tries < loc.getWorld().getMaxHeight() && loc.getBlock().getType() == Material.AIR)
        {
            tries = tries + 1;
            loc.subtract(0, 1, 0);
        }

        return loc;
    }

    public static boolean hasMaterialUnder(Location location, Material material, int depth)
    {
        Location check = location.clone();
        int yLevel = check.getBlockY();

        for (int i = 0; i <= depth; i++)
        {
            if (yLevel < 0) break;

            yLevel = yLevel - 1;
            check.subtract(0, 1, 0);
            if (check.getBlock().getType() == material)
            {
                return true;
            }
        }

        return false;
    }

    public static boolean hasMaterialUnder(Player player, Material material, int depth)
    {
        return hasMaterialUnder(player.getLocation(), material, depth);
    }

    public static boolean isDifferentBlock(Location a, Location b) {
        return (a == null || b == null) || (a.getBlock().getLocation().getBlockX() != b.getBlock().getLocation().getBlockX() || a.getBlock().getLocation().getBlockY() != b.getBlock().getLocation().getBlockY() || a.getBlock().getLocation().getBlockZ() != b.getBlock().getLocation().getBlockZ());
}

    public static boolean hasAnyBlockUnder(Location location, int depth)
    {
        Location check = location.clone();
        int yLevel = check.getBlockY();

        for (int i = 0; i <= depth; i++)
        {
            if (yLevel < 0) break;

            yLevel = yLevel - 1;
            check.subtract(0, 1, 0);
            if (check.getBlock().getType() != Material.AIR)
            {
                return true;
            }
        }

        return false;
    }

    public static boolean hasAnyBlockUnder(Player player, int depth)
    {
        return hasAnyBlockUnder(player.getLocation(), depth);
    }

    public static boolean isInside(Location loc, Location corner1, Location corner2)
    {
        double xMin = 0;
        double xMax = 0;
        double yMin = 0;
        double yMax = 0;
        double zMin = 0;
        double zMax = 0;
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        xMin = Math.min(corner1.getX(), corner2.getX());
        xMax = Math.max(corner1.getX(), corner2.getX());

        yMin = Math.min(corner1.getY(), corner2.getY());
        yMax = Math.max(corner1.getY(), corner2.getY());

        zMin = Math.min(corner1.getZ(), corner2.getZ());
        zMax = Math.max(corner1.getZ(), corner2.getZ());

        return (x >= xMin && x <= xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax);
    }

    public ArrayList<Location> getCircle(Location center, double radius, int amount)
    {
        World world = center.getWorld();
        double increment = (2 * Math.PI) / amount;
        ArrayList<Location> locations = new ArrayList<Location>();
        for (int i = 0; i < amount; i++)
        {
            double angle = i * increment;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            locations.add(new Location(world, x, center.getY(), z));
        }
        return locations;
    }

    /**
     * @param position1 - First position
     * @param position2 - Second position
     * @return Cuboid
     * @note - it will return all the blocks from "position1" till "position2".
     * @credits - CaptainBern
     */
    public static List<Location> getCuboid(Location position1,
                                           Location position2)
    {

        if (position1.getWorld().getName() != position2.getWorld().getName())
        {
            throw new UnsupportedOperationException(
                    "'Position1' and 'Position2' location need to be in the same world!");
        }

        List<Location> cube = new ArrayList<Location>();

        int minX = (int) Math.min(position1.getX(), position2.getX());
        int maxX = (int) Math.max(position1.getX(), position2.getX());

        int minY = (int) Math.min(position1.getY(), position2.getY());
        int maxY = (int) Math.max(position1.getY(), position2.getY());

        int minZ = (int) Math.min(position1.getZ(), position2.getZ());
        int maxZ = (int) Math.max(position1.getZ(), position2.getZ());

        for (int x = minX; x <= maxX; x++)
        {
            for (int y = minY; y <= maxY; y++)
            {
                for (int z = minZ; z <= maxZ; z++)
                {
                    cube.add(new Location(position1.getWorld(), x, y, z));
                }
            }
        }
        return cube;
    }

    /**
     * @param position1 - First position
     * @param position2 - Second position
     * @return Plain Cuboid
     * @note - it will return only the blocks that are in the same level as the
     * "position1" and till the "position2".
     */
    public static List<Location> getPlain(Location position1, Location position2)
    {
        List<Location> plain = new ArrayList<Location>();
        if (position1 == null)
            return plain;
        if (position2 == null)
            return plain;
        for (int x = Math.min(position1.getBlockX(), position2.getBlockX()); x <= Math
                .max(position1.getBlockX(), position2.getBlockX()); x++)
            for (int z = Math.min(position1.getBlockZ(), position2.getBlockZ()); z <= Math
                    .max(position1.getBlockZ(), position2.getBlockZ()); z++)
                plain.add(new Location(position1.getWorld(), x, position1
                        .getBlockY(), z));
        return plain;
    }

    /**
     * @param position1          - First position
     * @param position2          - Second position
     * @param getOnlyAboveGround - boolean (see the notes);
     * @return Cuboid
     * @note1 - if "land" is activated, it will return air blocks only one block
     * above the ground;
     * @note2 - if "land" is deactivated, it will return only the air blocks in
     * the cuboid.
     */
    public static List<Location> getBlocks(Location position1,
                                           Location position2, boolean getOnlyAboveGround)
    {
        List<Location> blocks = new ArrayList<Location>();
        if (position1 == null)
            return blocks;
        if (position2 == null)
            return blocks;

        for (int x = Math.min(position1.getBlockX(), position2.getBlockX()); x <= Math
                .max(position1.getBlockX(), position2.getBlockX()); x++)
            for (int z = Math.min(position1.getBlockZ(), position2.getBlockZ()); z <= Math
                    .max(position1.getBlockZ(), position2.getBlockZ()); z++)
                for (int y = Math.min(position1.getBlockY(),
                        position2.getBlockY()); y <= Math.max(
                        position1.getBlockY(), position2.getBlockY()); y++)
                {
                    Block b = position1.getWorld().getBlockAt(x, y, z);
                    if ((b.getType() == Material.AIR)
                            && ((!getOnlyAboveGround) || (b.getRelative(
                            BlockFace.DOWN).getType() != Material.AIR)))
                        blocks.add(b.getLocation());
                }
        return blocks;
    }


    /**
     * @param position1 - First position
     * @param position2 - Second position
     * @return Line
     * @note - it will return only the blocks that are in diagonal from
     * "position1" till "position2".
     */
    public static List<Location> getLine(Location position1, Location position2)
    {
        List<Location> line = new ArrayList<Location>();
        int dx = Math.max(position1.getBlockX(), position2.getBlockX())
                - Math.min(position1.getBlockX(), position2.getBlockX());
        int dy = Math.max(position1.getBlockY(), position2.getBlockY())
                - Math.min(position1.getBlockY(), position2.getBlockY());
        int dz = Math.max(position1.getBlockZ(), position2.getBlockZ())
                - Math.min(position1.getBlockZ(), position2.getBlockZ());
        int x1 = position1.getBlockX();
        int x2 = position2.getBlockX();
        int y1 = position1.getBlockY();
        int y2 = position2.getBlockY();
        int z1 = position1.getBlockZ();
        int z2 = position2.getBlockZ();
        int x = 0;
        int y = 0;
        int z = 0;
        int i = 0;
        int d = 1;
        switch (getHighest(dx, dy, dz))
        {
            case 1:
                i = 0;
                d = 1;
                if (x1 > x2)
                    d = -1;
                x = position1.getBlockX();
                do
                {
                    i++;
                    y = y1 + (x - x1) * (y2 - y1) / (x2 - x1);
                    z = z1 + (x - x1) * (z2 - z1) / (x2 - x1);
                    line.add(new Location(position1.getWorld(), x, y, z));
                    x += d;
                } while (i <= Math.max(x1, x2) - Math.min(x1, x2));
                break;
            case 2:
                i = 0;
                d = 1;
                if (y1 > y2)
                    d = -1;
                y = position1.getBlockY();
                do
                {
                    i++;
                    x = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
                    z = z1 + (y - y1) * (z2 - z1) / (y2 - y1);
                    line.add(new Location(position1.getWorld(), x, y, z));
                    y += d;
                } while (i <= Math.max(y1, y2) - Math.min(y1, y2));
                break;
            case 3:
                i = 0;
                d = 1;
                if (z1 > z2)
                    d = -1;
                z = position1.getBlockZ();
                do
                {
                    i++;
                    y = y1 + (z - z1) * (y2 - y1) / (z2 - z1);
                    x = x1 + (z - z1) * (x2 - x1) / (z2 - z1);
                    line.add(new Location(position1.getWorld(), x, y, z));
                    z += d;
                } while (i <= Math.max(z1, z2) - Math.min(z1, z2));
        }

        return line;
    }

    // support
    private static int getHighest(int x, int y, int z)
    {
        if ((x >= y) && (x >= z))
            return 1;
        if ((y >= x) && (y >= z))
            return 2;
        return 3;
    }

    /**
     * Check the distance from Location A to Location B
     *
     * @param l1 - Location 1
     * @param l2 - Location 2
     * @return
     */
    public static double getDistance(Location l1, Location l2)
    {
        double x1 = l1.getX();
        double x2 = l2.getX();
        double z1 = l1.getZ();
        double z2 = l2.getZ();
        double dist = (double) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2));
        return dist;
    }

    /**
     * @param location - Initial location
     * @param radius   - distance from the "location" that will return all the
     *                 entities from each block;
     * @return HashSet(LivingEntity)
     * @note - it will return only Living Entities in a radius, such as players,
     * mobs and animals.
     * @credits - skore87 (little modification by me)
     */
    public static HashSet<LivingEntity> getNearbyEntities(Location location,
                                                          int radius)
    {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<LivingEntity> radiusEntities = new HashSet<LivingEntity>();

        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++)
        {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++)
            {
                int x = (int) location.getX(), y = (int) location.getY(), z = (int) location
                        .getZ();
                for (Entity e : new Location(location.getWorld(), x
                        + (chX * 16), y, z + (chZ * 16)).getChunk()
                        .getEntities())
                {
                    if (e.getLocation().distance(location) <= radius
                            && e.getLocation().getBlock() != location
                            .getBlock())
                        if (e instanceof LivingEntity)
                        {
                            radiusEntities.add((LivingEntity) e);
                        }
                }
            }
        }
        return radiusEntities;
    }

    /**
     * @param location - Initial location
     * @param radius   - distance from the "location" that will return all the
     *                 entities from each block;
     * @param damager  - the damager to be excluded from this HashSet
     * @return HashSet(LivingEntity)
     * @note - it will return only Living Entities in a radius, such as players,
     * mobs and animals.
     * @credits - skore87 (little modification by me)
     */
    public static HashSet<Player> getNearbyEntitiesWithoutSender(Location location, int radius, LivingEntity damager) {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Player> radiusEntities = new HashSet<>();

        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) location.getX(), y = (int) location.getY(), z = (int) location.getZ();
                for (Entity e : new Location(location.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distance(location) <= radius && e.getLocation().getBlock() != location.getBlock())
                        if (e instanceof Player && damager != e) {
                            radiusEntities.add((Player) e);
                        }
                }
            }
        }
        return radiusEntities;
    }

    /**
     * @param location   --> the location to damage within the radius
     * @param radius     --> the radius to search for players
     * @param damager    --> the damager
     * @param selfDamage --> This is a utility to damage the damager
     * @param damage     --> the amount to damage
     */
    public static void radiusDamageNearby(Location location, int radius, LivingEntity damager, boolean selfDamage, int damage)
    {
        HashSet<Player> ent = damager != null && selfDamage ? getNearbyEntitiesWithoutSender(location, radius, damager) : getNearbyEntitiesWithoutSender(location, radius, null);
        for (Entity entity : ent)
        {
            if (entity instanceof LivingEntity && ((entity == damager && selfDamage) || entity != damager))
            {
                ((LivingEntity) entity).damage(damage);
            }
        }
    }

    public List<Entity> getNearbyEntities(Location loc, double x, double y, double z)
    {
        FallingBlock ent = loc.getWorld().spawnFallingBlock(loc, 0, (byte) 0);
        List<Entity> out = ent.getNearbyEntities(x, y, z);
        ent.remove();

        return out;
    }

    public static List<Location> getCylinder(Location center, int radius, int height) {
        List<Location> list = Lists.newArrayList();
        World w = center.getWorld();

        for(int i = center.getBlockY(); i <= center.getBlockY() + height; i++) {
            int cx = center.getBlockX();
            int cz = center.getBlockZ();
            int rSquared = radius * radius;
            for (int x = cx - radius; x <= cx +radius; x++) {
                for (int z = cz - radius; z <= cz +radius; z++) {
                    if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
                        list.add(w.getBlockAt(x, i, z).getLocation());
                    }
                }
            }
        }

        return list;
    }

    /**
     * @param center
     * @param radiusX
     * @param radiusZ
     * @param height
     * @param filled
     * @return
     * @credits bobacadodl
     */

    public static List<Location> getCylinder(Location center, double radiusX,
                                             double radiusZ, int height, boolean filled) {
        Vector pos = center.toVector();
        World world = center.getWorld();
        List<Location> blocks = new ArrayList<Location>();
        radiusX += 0.5;
        radiusZ += 0.5;

        if (height == 0) {
            return blocks;
        } else if (height < 0) {
            height = -height;
            pos = pos.subtract(new Vector(0, height, 0));
        }

        if (pos.getBlockY() < 0) {
            pos = pos.setY(0);
        } else if (pos.getBlockY() + height - 1 > world.getMaxHeight()) {
            height = world.getMaxHeight() - pos.getBlockY() + 1;
        }

        final double invRadiusX = 1 / radiusX;
        final double invRadiusZ = 1 / radiusZ;

        final int ceilRadiusX = (int) Math.ceil(radiusX);
        final int ceilRadiusZ = (int) Math.ceil(radiusZ);

        double nextXn = 0;
        forX:
        for (int x = 0; x <= ceilRadiusX; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextZn = 0;
            forZ:
            for (int z = 0; z <= ceilRadiusZ; ++z) {
                final double zn = nextZn;
                nextZn = (z + 1) * invRadiusZ;

                double distanceSq = lengthSq(xn, zn);
                if (distanceSq > 1) {
                    if (z == 0) {
                        break forX;
                    }
                    break forZ;
                }

                if (!filled) {
                    if (lengthSq(nextXn, zn) <= 1 && lengthSq(xn, nextZn) <= 1) {
                        continue;
                    }
                }

                for (int y = 0; y < height; ++y) {

                    blocks.add(pos.add(new Vector(x, y, z)).toLocation(world));
                    blocks.add(pos.add(new Vector(-x, y, z)).toLocation(world));
                    blocks.add(pos.add(new Vector(x, y, -z)).toLocation(world));
                    blocks.add(pos.add(new Vector(-x, y, -z)).toLocation(world));
                }
            }
        }

        return blocks;
    }

    /**
     * @param loc
     * @param r
     * @param h
     * @param hollow
     * @param sphere
     * @param plus_y
     * @return
     */
    public static List<Location> circle(Location loc, Integer r, Integer h, Boolean hollow, Boolean sphere, int plus_y) {
        List<Location> circleblocks = new ArrayList<Location>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx + r; x++)
            for (int z = cz - r; z <= cz + r; z++)
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        Location l = new Location(loc.getWorld(), x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }

        return circleblocks;
    }

    /**
     * @param center
     * @param radiusX
     * @param radiusY
     * @param radiusZ
     * @param filled
     * @return
     * @credits bobacadodl
     */

    public static List<Location> getSphere(Location center, double radiusX,
                                           double radiusY, double radiusZ, boolean filled) {
        Vector pos = center.toVector();
        World world = center.getWorld();
        List<Location> blocks = new ArrayList<Location>();

        radiusX += 0.5;
        radiusY += 0.5;
        radiusZ += 0.5;

        final double invRadiusX = 1 / radiusX;
        final double invRadiusY = 1 / radiusY;
        final double invRadiusZ = 1 / radiusZ;

        final int ceilRadiusX = (int) Math.ceil(radiusX);
        final int ceilRadiusY = (int) Math.ceil(radiusY);
        final int ceilRadiusZ = (int) Math.ceil(radiusZ);

        double nextXn = 0;
        forX:
        for (int x = 0; x <= ceilRadiusX; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextYn = 0;
            forY:
            for (int y = 0; y <= ceilRadiusY; ++y) {
                final double yn = nextYn;
                nextYn = (y + 1) * invRadiusY;
                double nextZn = 0;
                forZ:
                for (int z = 0; z <= ceilRadiusZ; ++z) {
                    final double zn = nextZn;
                    nextZn = (z + 1) * invRadiusZ;

                    double distanceSq = lengthSq(xn, yn, zn);
                    if (distanceSq > 1) {
                        if (z == 0) {
                            if (y == 0) {
                                break forX;
                            }
                            break forY;
                        }
                        break forZ;
                    }
                    if (!filled) {
                        if (lengthSq(nextXn, yn, zn) <= 1
                                && lengthSq(xn, nextYn, zn) <= 1
                                && lengthSq(xn, yn, nextZn) <= 1) {
                            continue;
                        }
                    }
                    blocks.add(pos.add(new Vector(x, y, z)).toLocation(world));
                    blocks.add(pos.add(new Vector(-x, y, z)).toLocation(world));
                    blocks.add(pos.add(new Vector(x, -y, z)).toLocation(world));
                    blocks.add(pos.add(new Vector(x, y, -z)).toLocation(world));
                    blocks.add(pos.add(new Vector(-x, -y, z)).toLocation(world));
                    blocks.add(pos.add(new Vector(x, -y, -z)).toLocation(world));
                    blocks.add(pos.add(new Vector(-x, y, -z)).toLocation(world));
                    blocks.add(pos.add(new Vector(-x, -y, -z))
                            .toLocation(world));
                }
            }
        }

        return blocks;
    }

    /**
     * @param x
     * @param y
     * @param z b
     * @return
     */

    private static final double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }

    /**
     * @param x
     * @param z
     * @return
     */

    private static final double lengthSq(double x, double z) {
        return (x * x) + (z * z);
    }



    /*
    Credit to Essentials: https://github.com/essentials/Essentials/blob/d36d80933f8f672cd8bb0f210adc23aac10850ea/Essentials/src/com/earth2me/essentials/utils/LocationUtil.java
     */

    public static final int RADIUS = 15;
    public static class Vector3D
    {
        public int x;
        public int y;
        public int z;

        public Vector3D(int x, int y, int z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    static
    {
        List<Vector3D> pos = new ArrayList<Vector3D>();
        for (int x = -RADIUS; x <= RADIUS; x++)
        {
            for (int y = -RADIUS; y <= RADIUS; y++)
            {
                for (int z = -RADIUS; z <= RADIUS; z++)
                {
                    pos.add(new Vector3D(x, y, z));
                }
            }
        }
        Collections.sort(
                pos, new Comparator<Vector3D>()
                {
                    @Override
                    public int compare(Vector3D a, Vector3D b)
                    {
                        return (a.x * a.x + a.y * a.y + a.z * a.z) - (b.x * b.x + b.y * b.y + b.z * b.z);
                    }
                });
        VOLUME = pos.toArray(new Vector3D[0]);
    }

    public static final Vector3D[] VOLUME;

    public static final Set<Integer> HOLLOW_MATERIALS = new HashSet<Integer>();
    private static final HashSet<Byte> TRANSPARENT_MATERIALS = new HashSet<Byte>();

    static
    {
        HOLLOW_MATERIALS.add(Material.AIR.getId());
        HOLLOW_MATERIALS.add(Material.SAPLING.getId());
        HOLLOW_MATERIALS.add(Material.POWERED_RAIL.getId());
        HOLLOW_MATERIALS.add(Material.DETECTOR_RAIL.getId());
        HOLLOW_MATERIALS.add(Material.LONG_GRASS.getId());
        HOLLOW_MATERIALS.add(Material.DEAD_BUSH.getId());
        HOLLOW_MATERIALS.add(Material.YELLOW_FLOWER.getId());
        HOLLOW_MATERIALS.add(Material.RED_ROSE.getId());
        HOLLOW_MATERIALS.add(Material.BROWN_MUSHROOM.getId());
        HOLLOW_MATERIALS.add(Material.RED_MUSHROOM.getId());
        HOLLOW_MATERIALS.add(Material.TORCH.getId());
        HOLLOW_MATERIALS.add(Material.REDSTONE_WIRE.getId());
        HOLLOW_MATERIALS.add(Material.SEEDS.getId());
        HOLLOW_MATERIALS.add(Material.SIGN_POST.getId());
        HOLLOW_MATERIALS.add(Material.WOODEN_DOOR.getId());
        HOLLOW_MATERIALS.add(Material.LADDER.getId());
        HOLLOW_MATERIALS.add(Material.RAILS.getId());
        HOLLOW_MATERIALS.add(Material.WALL_SIGN.getId());
        HOLLOW_MATERIALS.add(Material.LEVER.getId());
        HOLLOW_MATERIALS.add(Material.STONE_PLATE.getId());
        HOLLOW_MATERIALS.add(Material.IRON_DOOR_BLOCK.getId());
        HOLLOW_MATERIALS.add(Material.WOOD_PLATE.getId());
        HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_OFF.getId());
        HOLLOW_MATERIALS.add(Material.REDSTONE_TORCH_ON.getId());
        HOLLOW_MATERIALS.add(Material.STONE_BUTTON.getId());
        HOLLOW_MATERIALS.add(Material.SNOW.getId());
        HOLLOW_MATERIALS.add(Material.SUGAR_CANE_BLOCK.getId());
        HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_OFF.getId());
        HOLLOW_MATERIALS.add(Material.DIODE_BLOCK_ON.getId());
        HOLLOW_MATERIALS.add(Material.PUMPKIN_STEM.getId());
        HOLLOW_MATERIALS.add(Material.MELON_STEM.getId());
        HOLLOW_MATERIALS.add(Material.VINE.getId());
        HOLLOW_MATERIALS.add(Material.FENCE_GATE.getId());
        HOLLOW_MATERIALS.add(Material.WATER_LILY.getId());
        HOLLOW_MATERIALS.add(Material.NETHER_WARTS.getId());
        HOLLOW_MATERIALS.add(Material.CARPET.getId());

        for (Integer integer : HOLLOW_MATERIALS)
        {
            TRANSPARENT_MATERIALS.add(integer.byteValue());
        }
        TRANSPARENT_MATERIALS.add((byte)Material.WATER.getId());
        TRANSPARENT_MATERIALS.add((byte)Material.STATIONARY_WATER.getId());
    }

    static boolean isBlockAboveAir(final World world, final int x, final int y, final int z)
    {
        if (y > world.getMaxHeight())
        {
            return true;
        }
        return HOLLOW_MATERIALS.contains(world.getBlockAt(x, y - 1, z).getType().getId());
    }

    public static boolean isBlockUnsafe(final World world, final int x, final int y, final int z)
    {
        if (isBlockDamaging(world, x, y, z))
        {
            return true;
        }
        return isBlockAboveAir(world, x, y, z);
    }

    public static boolean isBlockDamaging(final World world, final int x, final int y, final int z)
    {
        final Block below = world.getBlockAt(x, y - 1, z);
        if (below.getType() == Material.LAVA || below.getType() == Material.STATIONARY_LAVA)
        {
            return true;
        }
        if (below.getType() == Material.FIRE)
        {
            return true;
        }
        if (below.getType() == Material.BED_BLOCK)
        {
            return true;
        }
        if ((!HOLLOW_MATERIALS.contains(world.getBlockAt(x, y, z).getType().getId())) || (!HOLLOW_MATERIALS.contains(world.getBlockAt(x, y + 1, z).getType().getId())))
        {
            return true;
        }
        return false;
    }

    public static Location getSafeDestination(final Location loc) throws Exception
    {
        if (loc == null || loc.getWorld() == null)
        {
            throw new Exception("null");
        }
        final World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = (int)Math.round(loc.getY());
        int z = loc.getBlockZ();
        final int origX = x;
        final int origY = y;
        final int origZ = z;
        while (isBlockAboveAir(world, x, y, z))
        {
            y -= 1;
            if (y < 0)
            {
                y = origY;
                break;
            }
        }
        if (isBlockUnsafe(world, x, y, z))
        {
            x = Math.round(loc.getX()) == origX ? x - 1 : x + 1;
            z = Math.round(loc.getZ()) == origZ ? z - 1 : z + 1;
        }
        int i = 0;
        while (isBlockUnsafe(world, x, y, z))
        {
            i++;
            if (i >= VOLUME.length)
            {
                x = origX;
                y = origY + RADIUS;
                z = origZ;
                break;
            }
            x = origX + VOLUME[i].x;
            y = origY + VOLUME[i].y;
            z = origZ + VOLUME[i].z;
        }
        while (isBlockUnsafe(world, x, y, z))
        {
            y += 1;
            if (y >= world.getMaxHeight())
            {
                x += 1;
                break;
            }
        }
        while (isBlockUnsafe(world, x, y, z))
        {
            y -= 1;
            if (y <= 1)
            {
                x += 1;
                y = world.getHighestBlockYAt(x, z);
                if (x - 48 > loc.getBlockX())
                {
                    throw new Exception("holeInFloor");
                }
            }
        }
        return new Location(world, x + 0.5, y, z + 0.5, loc.getYaw(), loc.getPitch());
    }

}
