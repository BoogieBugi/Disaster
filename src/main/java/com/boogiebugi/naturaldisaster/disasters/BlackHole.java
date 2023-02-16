package com.boogiebugi.naturaldisaster.disasters;

import com.boogiebugi.naturaldisaster.NaturalDisaster;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BlackHole extends Disaster{
    private BlackHoleTask blackHoleTask;

    public BlackHole(Location loc) {
        super("Black Hole", loc);
    }

    @Override
    public void onOccur() {
        super.onOccur();
        blackHoleTask = new BlackHoleTask(this);
        blackHoleTask.runTaskTimer(NaturalDisaster.plugin, 60L, 1L);
    }

    @Override
    public void onDisoccur() {
        super.onDisoccur();
        blackHoleTask.cancel();
    }

    @Override
    public Material getIcon() {
        return Material.BLACK_CONCRETE;
    }
}

final class BlackHoleTask extends BukkitRunnable {
    private final double maxSpeed = 1;
    private final BlackHole blackHole;
    private final Location loc;
    private final World world;
    private double radius = 2;

    public BlackHoleTask(BlackHole blackHole) {
        this.blackHole = blackHole;
        this.loc = blackHole.getLocation();
        this.world = loc.getWorld();
    }

    @Override
    public void run() {
        for (double x = -radius; x <= radius; x++) {
            for (double y = -radius; y <= radius; y++) {
                for (double z = -radius; z <= radius; z++) {
                    Location blockLoc = loc.clone().add(x, y, z);
                    Block block = blockLoc.getBlock();
                    Material material = block.getType();
                    if (loc.distance(blockLoc) > radius || material.equals(Material.AIR) || material.equals(Material.VOID_AIR)) {
                        continue;
                    }

                    block.setType(Material.AIR);
                    if (material.equals(Material.WATER) || material.equals(Material.LAVA)) {
                        continue;
                    }

                    world.spawnFallingBlock(blockLoc.add(0, 1.2, 0), material, block.getData());
                }
            }
        }

        for (Entity entity : world.getEntities()) {
            Location entityLoc = entity.getLocation().clone();
            double dist = loc.distance(entityLoc);
            dist = dist > 0 ? dist : 0.2;
            if (dist > radius * 4 || (entity instanceof Player && ((Player) entity).getGameMode() == GameMode.SPECTATOR)) {
                continue;
            }

            if (dist <= radius / 2) {
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).setHealth(0);
                } else if (entity instanceof FallingBlock || entity instanceof Item) {
                    entity.remove();
                }
                entity.teleport(entityLoc.add(0, 100, 0));
            } else if (dist <= radius) {
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 60, 0, true, false));
                }
            }

            entity.setVelocity(calSpeed(loc, entityLoc));
        }

        world.spawnParticle(Particle.REDSTONE, loc, (int) (radius * 20), radius / 4, radius / 4, radius / 4, 0.0001, new Particle.DustOptions(Color.BLACK, (float) radius), true);

        radius += 0.01;
    }

    private Vector calSpeed(Location loc, Location entityLoc) {
        double dist = loc.distance(entityLoc);
        if (dist == 0) {
            return new Vector(0, 0, 0);
        }

        double distX = loc.getX() - entityLoc.getX();
        double distY = loc.getY() - entityLoc.getY();
        double distZ = loc.getZ() - entityLoc.getZ();

        double speedX = distX > 0 ? 1 : -1;
        double speedY = distY > 0 ? 1 : -1;
        double speedZ = distZ > 0 ? 1 : -1;

        Vector speed = new Vector(maxSpeed, maxSpeed, maxSpeed).multiply(new Vector(speedX, speedY, speedZ)).multiply(1 / Math.pow(dist, 2)).multiply(Math.pow(radius, 2));
        return speed;
    }
}

