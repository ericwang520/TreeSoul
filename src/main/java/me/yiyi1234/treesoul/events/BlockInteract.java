package me.yiyi1234.treesoul.events;

import de.tr7zw.nbtapi.NBTItem;
import me.yiyi1234.treesoul.TreeSoul;
import me.yiyi1234.treesoul.core.Prayer;
import me.yiyi1234.treesoul.core.TreeCore;
import me.yiyi1234.treesoul.manager.TreeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BlockInteract implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType() == Material.WALL_SIGN) {
            if (TreeManager.getTreeCore(event.getClickedBlock().getLocation()) == null) {
                return;
            }
            TreeCore core = TreeManager.getTreeCore(event.getClickedBlock().getLocation());
            Sign sign = (Sign) event.getClickedBlock().getState();
            String[] lines = sign.getLines();
            String configformat = TreeSoul.getInstance().getConfig().getString("sign-format");
            if (lines[0].contains(configformat)) {
                if (Bukkit.getPlayer(core.getTreeOwner()) == event.getPlayer()) {
                    Inventory TreeUI = core.getTreeUI();
                    event.getPlayer().openInventory(TreeUI);
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a樹靈系統&7] &a正在為您開啟 &f樹靈介面"));
                }else {
                    Inventory prayUI = Bukkit.createInventory(null, 27, "祭祀介面");
                    ItemStack sapling = new ItemStack(Material.SAPLING);
                    ItemMeta saplingMeta = sapling.getItemMeta();
                    saplingMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7左鍵即可祭祀"));
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&7持有者: &6" + Bukkit.getOfflinePlayer(core.getTreeOwner()).getName()));
                    String stateName = null;
                    if (core.getState() == TreeCore.State.seedling) {
                        stateName = "幼苗";
                    }
                    if (core.getState() == TreeCore.State.small_tree) {
                        stateName = "小樹木";
                    }
                    if (core.getState() == TreeCore.State.big_tree) {
                        stateName = "大樹靈";
                    }
                    if (core.getState() == TreeCore.State.ancient_tree) {
                        stateName = "古樹靈";
                    }

                    lore.add(ChatColor.translateAlternateColorCodes('&', "&7品種: &6" + core.getType()));
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&7階段: &6" + stateName));
                    String bonus = null;
                    long nowTime = System.currentTimeMillis();
                    long coreTime = core.getTime();
                    if (nowTime - coreTime < 1440000) {
                        bonus = "&7無加成";
                    }
                    if (nowTime - coreTime >= 1440000 && nowTime - coreTime < 2880000) {
                        bonus = "&6兩倍加成";
                    }
                    if (nowTime - coreTime >= 2880000 && nowTime - coreTime < 4320000) {
                        bonus = "&6三倍加成";
                    }
                    if (nowTime - coreTime >= 4320000) {
                        bonus = "&6四倍加成";
                    }
                    lore.add(ChatColor.translateAlternateColorCodes('&', "&7加成獎勵: &6" + bonus));
                    saplingMeta.setLore(lore);
                    sapling.setItemMeta(saplingMeta);
                    NBTItem item = new NBTItem(sapling);

                    item.setString("blockLoc" , event.getClickedBlock().getLocation().getWorld().getName() + "," + event.getClickedBlock().getLocation().getBlockX() + "," + event.getClickedBlock().getLocation().getBlockY() + "," + event.getClickedBlock().getLocation().getBlockZ());

                    ItemStack grayglass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
                    ItemMeta grayGlassMeta = grayglass.getItemMeta();
                    grayGlassMeta.setDisplayName(" ");
                    grayglass.setItemMeta(grayGlassMeta);

                    for (int i = 0; i < 27; i++) {
                        prayUI.setItem(i, grayglass);
                    }

                    prayUI.setItem(13, item.getItem());

                    event.getPlayer().openInventory(prayUI);

                }
            }
        }

    }
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("祭祀介面")) {
            event.setCancelled(true);

            if (event.getClickedInventory() == null) {
                return;
            }
            ItemStack chest = event.getClickedInventory().getItem(13);
            NBTItem nbtChest = new NBTItem(chest);
            String[] info = nbtChest.getString("blockLoc").split(",");
            Location blockLoc = new Location(Bukkit.getWorld(info[0]), Double.parseDouble(info[1]), Double.parseDouble(info[2]), Double.parseDouble(info[3]));
            TreeCore core = TreeManager.getTreeCore(blockLoc);

            if (event.getSlot() == 13) {
                Prayer prayer = new Prayer(event.getWhoClicked().getUniqueId());
                prayer.pray(core,Bukkit.getPlayer(event.getWhoClicked().getName()));
            }

        }
    }


}
