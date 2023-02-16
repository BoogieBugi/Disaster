package com.boogiebugi.naturaldisaster.disasters;

import com.boogiebugi.naturaldisaster.NaturalDisaster;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Tornado extends Disaster {
    private TornadoTask tornadoTask;

    public Tornado(Location loc) {
        super("Tornado", loc);
    }

    @Override
    public void onOccur() {
        super.onOccur();
        tornadoTask = new TornadoTask(this);
        tornadoTask.runTaskTimer(NaturalDisaster.plugin, 0L, 1L);
    }

    @Override
    public void onDisoccur() {
        super.onDisoccur();
        tornadoTask.cancel();
    }

    @Override
    public Material getIcon() {
        return Material.FEATHER;
    }
}

final class TornadoTask extends BukkitRunnable {

    private final Tornado tornado;
    private final Location loc;

    private Location targetLoc;
    private int tick = 140;
    private int period = 100;

    public TornadoTask(Tornado tornado) {
        this.tornado = tornado;
        this.loc = tornado.getLocation();
    }

    @Override
    public void run() {
        World world = loc.getWorld();
        for (double x = -16; x <= 16; x++) {
            for (double y = -1; y <= 15; y++) {
                for (double z = -16; z <= 16; z++) {
                    Location blockLoc = loc.clone().add(x, y, z);
                    Block block = blockLoc.getBlock();
                    Material material = block.getType();
                    if (loc.distance(blockLoc) > 8 || material.equals(Material.AIR) || material.equals(Material.VOID_AIR) || material.equals(Material.BEDROCK) || Math.random() >= 0.05) {
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
            entityLoc.setY(loc.getY());
            double dist = loc.distance(entityLoc);
            dist = dist > 0 ? dist : 1;
            if (dist > 16 || (entity instanceof Player && ((Player) entity).getGameMode() == GameMode.SPECTATOR)) {
                continue;
            }

            entity.setVelocity(new Vector((loc.getZ() - entityLoc.getZ()) / dist * 3, 0.6, (entityLoc.getX() - loc.getX()) / dist * 3));
        }

        tick++;
        if (tick >= period) {
            double rand1 = Math.random() * 40 + 15;
            rand1 = Math.random() <= 0.5 ? rand1 : -rand1;
            double rand2 = Math.random() * 40 + 15;
            rand2 = Math.random() <= 0.5 ? rand2 : -rand2;
            targetLoc = loc.clone().add(rand1, 0, rand2);
            tick = 0;
        }

        loc.add(new Vector((targetLoc.getX() - loc.getX()) / period, 0, (targetLoc.getZ() - loc.getZ()) / period));
        double highestY = world.getHighestBlockYAt(loc);
        highestY = highestY == 0 ? 1 : highestY;
        loc.setY(highestY);
    }
}
