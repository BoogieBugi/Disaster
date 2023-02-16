package com.boogiebugi.naturaldisaster.inventories;

import com.boogiebugi.naturaldisaster.NaturalDisaster;
import com.boogiebugi.naturaldisaster.disasters.Disaster;
import com.boogiebugi.naturaldisaster.utils.DisasterManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisasterManageInventory implements Listener {
    private static final NaturalDisaster plugin = NaturalDisaster.plugin;

    private final Inventory inv;

    public DisasterManageInventory() {
        inv = Bukkit.createInventory(null, 27, plugin.convertColorText("&lDisaster Manage"));
        init();
    }

    public void onOpen(Player player) {
        player.openInventory(inv);
    }

    public void init() {
        for (int i = 0; i < DisasterManager.inProgress.size(); i++) {
            Disaster disaster = DisasterManager.inProgress.get(i);
            Location loc = disaster.getLocation();
            ItemStack item = new ItemStack(disaster.getIcon());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(NaturalDisaster.convertColorText("&l" + disaster.getName()));
            List<String> lores = new ArrayList<String>();
            lores.add("X : " + Math.floor(loc.getX()));
            lores.add("Y : " + Math.floor(loc.getY()));
            lores.add("Z : " + Math.floor(loc.getZ()));
            lores.add("Click To Remove");
            meta.setLore(lores);
            item.setItemMeta(meta);
            inv.setItem(i, item);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(plugin.convertColorText("&lDisaster Manage"))) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            Disaster disaster = DisasterManager.inProgress.get(event.getSlot());
            disaster.onDisoccur();
            DisasterManageInventory disasterManageInventory = new DisasterManageInventory();
            disasterManageInventory.onOpen(player);
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 10.0f, 2.0f);
        }
    }
}
