package com.boogiebugi.naturaldisaster;

import com.boogiebugi.naturaldisaster.disasters.Tsunami;
import com.boogiebugi.naturaldisaster.inventories.DisasterManageInventory;
import com.boogiebugi.naturaldisaster.inventories.DisasterMenuInventory;
import com.boogiebugi.naturaldisaster.inventories.DisasterSpawnInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.EventListener;

public class NaturalDisaster extends JavaPlugin implements Listener {
    public static NaturalDisaster plugin;

    @Override
    public void onEnable() {
        plugin = this;

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(this, this);
        pluginManager.registerEvents(new DisasterMenuInventory(), this);
        pluginManager.registerEvents(new DisasterSpawnInventory(), this);
        pluginManager.registerEvents(new DisasterManageInventory(), this);
    }

    public static String convertColorText(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null || event.getItem().getType().isAir()) {
            return ;
        }

        ItemStack item = event.getItem();
        if (item.getType().equals(Material.NETHER_STAR)) {
            DisasterMenuInventory inv = new DisasterMenuInventory();
            inv.onOpen(event.getPlayer());
        }
    }
}
