package me.sores.impulse.util.entity;

import me.sores.impulse.Impulse;
import me.sores.impulse.util.PlayerUtil;
import net.minecraft.server.v1_8_R3.EntityFishingHook;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntitySnowball;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSnowball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.metadata.FixedMetadataValue;

public class CopyOfFishingHook extends EntityFishingHook {
    private Snowball sb;
    private EntitySnowball controller;
    public int a;
    public EntityHuman owner;
    public Entity hooked;
    public boolean lastControllerDead;
    public boolean isHooked;

    public static FixedMetadataValue HOOK_META;

    public static FixedMetadataValue getHookMeta() {
        if(HOOK_META == null) {
            HOOK_META = new FixedMetadataValue(Impulse.getInstance(), true);
        }

        return HOOK_META;
    }

    public CopyOfFishingHook(org.bukkit.World world, EntityHuman entityhuman)
    {
        super(((CraftWorld)world).getHandle(), entityhuman);
        this.owner = entityhuman;
    }

    protected void c() {}

    public void t_()
    {
        if (!this.lastControllerDead) {}
        this.lastControllerDead = this.controller.dead;
        for (Entity entity : this.controller.world.getWorld().getEntities()) {
            if ((!(entity instanceof Firework)) &&
                    (entity.getEntityId() !=
                            getBukkitEntity().getEntityId())) {
                if (entity.getEntityId() !=
                        this.owner.getBukkitEntity().getEntityId()) {
                    if (entity.getEntityId() !=
                            this.controller.getBukkitEntity().getEntityId()) {
                        if (entity.getLocation().distance(this.controller.getBukkitEntity().getLocation()) >= 2.0D)
                        {
                            if ((entity instanceof Player)) {
                                if (((Player)entity).getEyeLocation().distance(this.controller.getBukkitEntity().getLocation()) >= 2.0D) {}
                            }
                        }
                        else
                        {
                            this.controller.die();
                            this.hooked = entity;
                            this.isHooked = true;
                            this.locX = entity.getLocation().getX();
                            this.locY = entity.getLocation().getY();
                            this.locZ = entity.getLocation().getZ();
                            this.motX = 0.0D;
                            this.motY = 0.04D;
                            this.motZ = 0.0D;
                        }
                    }
                }
            }
        }
        try
        {
            this.locX = this.hooked.getLocation().getX();
            this.locY = this.hooked.getLocation().getY();
            this.locZ = this.hooked.getLocation().getZ();
            this.motX = 0.0D;
            this.motY = 0.06D;
            this.motZ = 0.0D;
            this.isHooked = true;
        }
        catch (Exception e)
        {
            if (this.controller.dead) {
                this.isHooked = true;
            }
            this.locX = this.controller.locX;
            this.locY = this.controller.locY;
            this.locZ = this.controller.locZ;
        }
    }

    public void die() {}

    public void remove()
    {
        super.die();
    }

    public void spawn(Location location)
    {
        this.sb = ((Snowball)this.owner.getBukkitEntity().launchProjectile(Snowball.class));
        sb.setMetadata("fakehook", getHookMeta());
        this.controller = ((CraftSnowball)this.sb).getHandle();

        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { this.controller.getId() });
        for (Player p : PlayerUtil.getOnlinePlayers()) {
            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        }
        ((CraftWorld)location.getWorld()).getHandle().addEntity(this);
    }

    public boolean isHooked()
    {
        return this.isHooked;
    }

    public void setHookedEntity(Entity damaged)
    {
        this.hooked = damaged;
    }
}
