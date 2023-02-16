package com.boogiebugi.naturaldisaster.inventories;

import com.boogiebugi.naturaldisaster.NaturalDisaster;
import com.boogiebugi.naturaldisaster.disasters.BlackHole;
import com.boogiebugi.naturaldisaster.disasters.Tornado;
import com.boogiebugi.naturaldisaster.disasters.Tsunami;
import com.boogiebugi.naturaldisaster.utils.DisasterManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class DisasterSpawnInventory implements Listener {
    private static final NaturalDisaster plugin = NaturalDisaster.plugin;

    private Inventory inv = Bukkit.createInventory(null, 9, plugin.convertColorText("&lDisaster Spawn"));

    public DisasterSpawnInventory() {
        init();
    }

    public void onOpen(Player player) {
        player.openInventory(inv);
    }

    private void init() {
        ItemStack waterBucket = new ItemStack(Material.WATER_BUCKET);
        ItemMeta meta1 = waterBucket.getItemMeta();
        meta1.setDisplayName(plugin.convertColorText("&lTsunami"));
        meta1.setLore(Arrays.asList("Spawn Tsunami"));
        waterBucket.setItemMeta(meta1);
        inv.setItem(0, waterBucket);

        ItemStack lavaBucket = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta meta2 = lavaBucket.getItemMeta();
        meta2.setDisplayName(plugin.convertColorText("&lLava Tsunami"));
        meta2.setLore(Arrays.asList("Click To Spawn"));
        lavaBucket.setItemMeta(meta2);
        inv.setItem(1, lavaBucket);

        ItemStack feather = new ItemStack(Material.FEATHER);
        ItemMeta meta3 = feather.getItemMeta();
        meta3.setDisplayName(plugin.convertColorText("&lTornado"));
        meta3.setLore(Arrays.asList("Click To Spawn"));
        feather.setItemMeta(meta3);
        inv.setItem(2, feather);

        ItemStack blackConcrete = new ItemStack(Material.BLACK_CONCRETE);
        ItemMeta meta4 = feather.getItemMeta();
        meta4.setDisplayName(plugin.convertColorText("&lBlack Hole"));
        meta4.setLore(Arrays.asList("Click To Spawn"));
        blackConcrete.setItemMeta(meta4);
        inv.setItem(3, blackConcrete);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if (event.getView().getTitle().equals(plugin.convertColorText("&lDisaster Spawn"))) {
            event.setCancelled(true);
            int slot = event.getSlot();
            Player player = (Player) event.getWhoClicked();
            Location loc = player.getLocation();
            if (DisasterManager.inProgress.size() >= 27) {
                player.sendMessage(NaturalDisaster.convertColorText("&cThere are too many natural disasters going on."));
                return ;
            }

            if (slot == 0) {
                Tsunami tsunami = new Tsunami(Material.WATER, loc);
                tsunami.onOccur();
                player.playSound(player, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
                player.closeInventory();
            } else if (slot == 1) {
                Tsunami lavaTsunami = new Tsunami(Material.LAVA, loc);
                lavaTsunami.onOccur();
                player.playSound(player, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
                player.closeInventory();
            } else if (slot == 2) {
                Tornado tornado = new Tornado(loc);
                tornado.onOccur();
                player.playSound(player, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
                player.closeInventory();
            } else if (slot == 3) {
                BlackHole blackHole = new BlackHole(loc);
                blackHole.onOccur();
                player.playSound(player, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
                player.closeInventory();
            }
        }
    }
}
