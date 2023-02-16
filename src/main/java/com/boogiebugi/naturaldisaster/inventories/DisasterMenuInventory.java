package com.boogiebugi.naturaldisaster.inventories;

import com.boogiebugi.naturaldisaster.NaturalDisaster;
import com.boogiebugi.naturaldisaster.utils.DisasterManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class DisasterMenuInventory implements Listener {
    private static final NaturalDisaster plugin = NaturalDisaster.plugin;

    private final Inventory inv;

    public DisasterMenuInventory() {
        inv = Bukkit.createInventory(null, 9, plugin.convertColorText("&lDisaster Menu"));
        init();
    }

    public void onOpen(Player player) {
        player.openInventory(inv);
    }

    public void init() {
        ItemStack tnt = new ItemStack(Material.TNT);
        ItemMeta meta1 = tnt.getItemMeta();
        meta1.setDisplayName(plugin.convertColorText("&l&cSpawn Menu"));
        meta1.setLore(Arrays.asList("Spawn Disasters"));
        tnt.setItemMeta(meta1);
        inv.setItem(2, tnt);

        ItemStack comparator = new ItemStack(Material.COMPARATOR);
        ItemMeta meta3 = comparator.getItemMeta();
        meta3.setDisplayName(plugin.convertColorText("&lManage Menu"));
        meta3.setLore(Arrays.asList("Manage Disasters in Progress"));
        comparator.setItemMeta(meta3);
        inv.setItem(6, comparator);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(plugin.convertColorText("&lDisaster Menu"))) {
            event.setCancelled(true);
            int slot = event.getSlot();
            Player player = (Player) event.getWhoClicked();
            if (slot == 2) {
                DisasterSpawnInventory disasterSpawnInventory = new DisasterSpawnInventory();
                disasterSpawnInventory.onOpen(player);
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            } else if (slot == 6) {
                DisasterManageInventory disasterManageInventory = new DisasterManageInventory();
                disasterManageInventory.onOpen(player);
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        }
    }
}
