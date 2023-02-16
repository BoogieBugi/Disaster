package com.boogiebugi.naturaldisaster.disasters;

import com.boogiebugi.naturaldisaster.NaturalDisaster;
import com.boogiebugi.naturaldisaster.utils.DisasterManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class Tsunami extends Disaster {
    private final Material material;
    private TsunamiTask tsunamiTask;

    public Tsunami(Material material, Location loc) {
        super("Tsunami", loc);
        this.material = material;
    }

    @Override
    public void onOccur() {
        super.onOccur();
        tsunamiTask = new TsunamiTask(this);
        tsunamiTask.runTaskTimer(NaturalDisaster.plugin, 0L, 20L);
    }

    @Override
    public void onDisoccur() {
        super.onDisoccur();
        tsunamiTask.cancel();
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public Material getIcon() {
        if (material.equals(Material.WATER)) {
            return Material.WATER_BUCKET;
        }
        return Material.LAVA_BUCKET;
    }
}

final class TsunamiTask extends BukkitRunnable {

    private Tsunami tsunami;
    private Location loc;
    private Material material;
    private int radius = 1;

    public TsunamiTask(Tsunami tsunami) {
        this.tsunami = tsunami;
        this.loc = tsunami.getLocation();
        this.material = tsunami.getMaterial();
    }

    @Override
    public void run() {
        for (double x = -radius; x <= radius; x++) {
            for (double z = -radius; z <= radius; z++) {
                Location liquidLoc = loc.clone().add(x, 0, z);
                Block block = liquidLoc.getBlock();
                if (loc.distance(liquidLoc) > radius || block.getType().equals(material) || !block.isPassable()) {
                    continue;
                }
                block.setType(material);
            }
        }

        if (!loc.getBlock().getType().equals(material)) {
            tsunami.onDisoccur();
        }

        radius++;
    }
}
