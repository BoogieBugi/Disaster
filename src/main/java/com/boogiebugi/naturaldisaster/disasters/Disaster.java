package com.boogiebugi.naturaldisaster.disasters;

import com.boogiebugi.naturaldisaster.utils.DisasterManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Disaster {
    private String name;
    private Material icon;

    protected Location loc;

    public Disaster(String name, Location loc) {
        this.name = name;
        this.loc = loc;
    }

    public void onOccur() {
        DisasterManager.inProgress.add(this);
    }

    public void onDisoccur() {
        DisasterManager.inProgress.remove(this);
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return loc;
    }

    public Material getIcon() {
        return null;
    }
}
