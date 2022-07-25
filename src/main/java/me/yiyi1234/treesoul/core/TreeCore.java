package me.yiyi1234.treesoul.core;

import de.tr7zw.nbtapi.NBTItem;
import me.yiyi1234.treesoul.TreeSoul;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TreeCore {
    private Location treeLoc;
    private State treestate; // 成長階段
    private double fertilizer_sun; // 肥料值
    private double fertilizer_river; // 肥料值
    private double fertilizer_air; // 肥料值
    private UUID treeOwner;
    private long time; // 距離上次祈福的時間
    private String type;
    private Inventory treeUI;
    private Inventory fertilizerUI;

    public TreeCore(Location loc, UUID treeOwnerUUID) {
        this(loc, State.seedling, 0.0, 0.0, 0.0, treeOwnerUUID, System.currentTimeMillis(), "陽光樹木");
    }

    public enum State {
        seedling, small_tree, big_tree, ancient_tree
    }

    public TreeCore(Location loc, State state, double fertilizer_sun, double fertilizer_river, double fertilizer_air, UUID owner, long time, String type) {
        this.treeLoc = loc;
        this.treestate = state;
        this.fertilizer_sun = fertilizer_sun;
        this.fertilizer_river = fertilizer_river;
        this.fertilizer_air = fertilizer_air;
        this.treeOwner = owner;
        this.time = time;
        this.type = type;

        setTreeUI();
        setFertilizerUI();
        refresh();
    }

    public Inventory setTreeUI() {
        String displayName = TreeSoul.getInstance().getConfig().getString("treeUI.displayName").replace("%player%", Bukkit.getOfflinePlayer(this.treeOwner).getName());
        treeUI = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', displayName));

        ItemStack greenGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);
        ItemMeta greenGlassMeta = greenGlass.getItemMeta();
        greenGlassMeta.setDisplayName(" ");

        greenGlass.setItemMeta(greenGlassMeta);

        ItemStack grayglass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta grayGlassMeta = grayglass.getItemMeta();
        grayGlassMeta.setDisplayName(" ");
        grayglass.setItemMeta(grayGlassMeta);


        ItemStack chest = new ItemStack(Material.CHEST);
        ItemMeta chestMeta = chest.getItemMeta();
        chestMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("treeUI.chest.DisplayName")));
        List<String> chestLore = new ArrayList<>();
        for (String i : TreeSoul.getInstance().getConfig().getStringList("treeUI.chest.DisplayLore")) {
            chestLore.add(ChatColor.translateAlternateColorCodes('&', i));
        }
        chestMeta.setLore(chestLore);
        chest.setItemMeta(chestMeta);
        NBTItem nbtChest = new NBTItem(chest);
        nbtChest.setString("blockLoc" , treeLoc.getWorld().getName() + "," + treeLoc.getBlockX() + "," + treeLoc.getBlockY() + "," + treeLoc.getBlockZ());


        ItemStack sapling = new ItemStack(Material.SAPLING);
        ItemMeta saplingMeta = sapling.getItemMeta();
        saplingMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("treeUI.sapling.DisplayName")));
        List<String> saplingLore = new ArrayList<>();
        for (String i : TreeSoul.getInstance().getConfig().getStringList("treeUI.sapling.DisplayLore")) {
            String stateName = null;
            if (treestate == State.seedling) {
                stateName = "幼苗";
            }
            if (treestate == State.small_tree) {
                stateName = "小樹木";
            }
            if (treestate == State.big_tree) {
                stateName = "大樹靈";
            }
            if (treestate == State.ancient_tree) {
                stateName = "古樹靈";
            }

            double allfertilizer = fertilizer_sun+fertilizer_air+fertilizer_river;
            saplingLore.add(ChatColor.translateAlternateColorCodes('&', i).replace("%fertilizer%", String.valueOf(allfertilizer)).replace("%state%", stateName));
        }
        saplingMeta.setLore(saplingLore);
        sapling.setItemMeta(saplingMeta);


        ItemStack boneMeal = new ItemStack(351, 1, (short) 15);
        ItemMeta boneMealMeta = boneMeal.getItemMeta();
        boneMealMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("treeUI.boneMeal.DisplayName")));
        List<String> boneMealLore = new ArrayList<>();
        for (String i : TreeSoul.getInstance().getConfig().getStringList("treeUI.boneMeal.DisplayLore")) {
            boneMealLore.add(ChatColor.translateAlternateColorCodes('&', i));
        }
        boneMealMeta.setLore(boneMealLore);
        boneMeal.setItemMeta(boneMealMeta);

        for (int i = 0; i < 9; i++) {
            treeUI.setItem(i, greenGlass);
        }
        for (int i = 9; i < 18; i++) {
            treeUI.setItem(i, grayglass);
        }
        for (int i = 18; i < 27; i++) {
            treeUI.setItem(i, greenGlass);
        }
        treeUI.setItem(10, nbtChest.getItem());
        treeUI.setItem(13, sapling);
        treeUI.setItem(16, boneMeal);
        return treeUI;
    }
    public void setFertilizerUI() {
        ItemStack chest = new ItemStack(Material.DOUBLE_PLANT);
        NBTItem nbtChest = new NBTItem(chest);
        nbtChest.setString("blockLoc" , treeLoc.getWorld().getName() + "," + treeLoc.getBlockX() + "," + treeLoc.getBlockY() + "," + treeLoc.getBlockZ());

        fertilizerUI = Bukkit.createInventory(null, 27, "施肥介面");

        ItemStack fertilizer_Sun = new ItemStack(Material.DOUBLE_PLANT, 1);
        ItemMeta itemMetaSun = fertilizer_Sun.getItemMeta();
        itemMetaSun.setDisplayName(ChatColor.translateAlternateColorCodes('&',TreeSoul.getInstance().getConfig().getString("fertilizerUI.fertilizer_sun.DisplayName")));
        List<String> sunLore = new ArrayList<>();

        for (String i : TreeSoul.getInstance().getConfig().getStringList("fertilizerUI.fertilizer_sun.DisplayLore")) {
            sunLore.add(ChatColor.translateAlternateColorCodes('&', i).replace("%amount%", String.valueOf(getFertilizer_sun())));
        }
        itemMetaSun.setLore(sunLore);
        fertilizer_Sun.setItemMeta(itemMetaSun);
        NBTItem nbtItem = new NBTItem(fertilizer_Sun);
        nbtItem.setString("blockLoc" , nbtChest.getString("blockLoc"));



        ItemStack getFertilizer_river = new ItemStack(351, 1, (short) 12);
        ItemMeta itemMetaRiver = getFertilizer_river.getItemMeta();
        itemMetaRiver.setDisplayName(ChatColor.translateAlternateColorCodes('&',TreeSoul.getInstance().getConfig().getString("fertilizerUI.fertilizer_river.DisplayName")));
        List<String> riverLore = new ArrayList<>();
        for (String i : TreeSoul.getInstance().getConfig().getStringList("fertilizerUI.fertilizer_river.DisplayLore")) {
            riverLore.add(ChatColor.translateAlternateColorCodes('&', i).replace("%amount%", String.valueOf(getFertilizer_river())));
        }
        itemMetaRiver.setLore(riverLore);
        getFertilizer_river.setItemMeta(itemMetaRiver);

        ItemStack fertilizer_air = new ItemStack(Material.FEATHER);
        ItemMeta itemMetaAir = fertilizer_air.getItemMeta();
        itemMetaAir.setDisplayName(ChatColor.translateAlternateColorCodes('&',TreeSoul.getInstance().getConfig().getString("fertilizerUI.fertilizer_air.DisplayName")));
        List<String> airLore = new ArrayList<>();
        for (String i : TreeSoul.getInstance().getConfig().getStringList("fertilizerUI.fertilizer_air.DisplayLore")) {
            airLore.add(ChatColor.translateAlternateColorCodes('&', i).replace("%amount%", String.valueOf(getFertilizer_air())));
        }
        itemMetaAir.setLore(airLore);
        fertilizer_air.setItemMeta(itemMetaAir);

        ItemStack greenGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);
        ItemMeta greenGlassMeta = greenGlass.getItemMeta();
        greenGlassMeta.setDisplayName(" ");

        greenGlass.setItemMeta(greenGlassMeta);

        ItemStack grayglass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta grayGlassMeta = greenGlass.getItemMeta();
        grayGlassMeta.setDisplayName(" ");
        grayglass.setItemMeta(grayGlassMeta);

        for (int i = 0; i < 9; i++) {
            fertilizerUI.setItem(i, greenGlass);
        }
        for (int i = 9; i < 18; i++) {
            fertilizerUI.setItem(i, grayglass);
        }
        for (int i = 18; i < 27; i++) {
            fertilizerUI.setItem(i, greenGlass);
        }

        fertilizerUI.setItem(10, nbtItem.getItem());
        fertilizerUI.setItem(13, getFertilizer_river);
        fertilizerUI.setItem(16, fertilizer_air);

    }

    public void refresh() {
        TreeSoul.getInstance().getServer().getScheduler().runTaskTimer(TreeSoul.getInstance(), () -> {

            ItemStack sapling = new ItemStack(Material.SAPLING);
            ItemMeta saplingMeta = sapling.getItemMeta();
            saplingMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("treeUI.sapling.DisplayName")));
            List<String> saplingLore = new ArrayList<>();
            for (String i : TreeSoul.getInstance().getConfig().getStringList("treeUI.sapling.DisplayLore")) {
                String stateName = null;
                if (treestate == State.seedling) {
                    stateName = "幼苗";
                }
                if (treestate == State.small_tree) {
                    stateName = "小樹木";
                }
                if (treestate == State.big_tree) {
                    stateName = "大樹靈";
                }
                if (treestate == State.ancient_tree) {
                    stateName = "古樹靈";
                }

                double allfertilizer = fertilizer_sun+fertilizer_air+fertilizer_river;
                saplingLore.add(ChatColor.translateAlternateColorCodes('&', i).replace("%fertilizer%", String.valueOf(allfertilizer)).replace("%state%", stateName));
            }
            saplingMeta.setLore(saplingLore);
            sapling.setItemMeta(saplingMeta);
            treeUI.setItem(13, sapling);

            ItemStack fertilizer_Sun = new ItemStack(Material.DOUBLE_PLANT, 1);
            ItemMeta itemMetaSun = fertilizer_Sun.getItemMeta();
            itemMetaSun.setDisplayName(ChatColor.translateAlternateColorCodes('&',TreeSoul.getInstance().getConfig().getString("fertilizerUI.fertilizer_sun.DisplayName")));
            List<String> sunLore = new ArrayList<>();

            for (String i : TreeSoul.getInstance().getConfig().getStringList("fertilizerUI.fertilizer_sun.DisplayLore")) {
                sunLore.add(ChatColor.translateAlternateColorCodes('&', i).replace("%amount%", String.valueOf(getFertilizer_sun())));
            }
            itemMetaSun.setLore(sunLore);
            fertilizer_Sun.setItemMeta(itemMetaSun);
            NBTItem nbtItem = new NBTItem(fertilizer_Sun);
            NBTItem nbtChest = new NBTItem(fertilizerUI.getItem(10));
            nbtItem.setString("blockLoc" , nbtChest.getString("blockLoc"));



            ItemStack getFertilizer_river = new ItemStack(351, 1, (short) 12);
            ItemMeta itemMetaRiver = getFertilizer_river.getItemMeta();
            itemMetaRiver.setDisplayName(ChatColor.translateAlternateColorCodes('&',TreeSoul.getInstance().getConfig().getString("fertilizerUI.fertilizer_river.DisplayName")));
            List<String> riverLore = new ArrayList<>();
            for (String i : TreeSoul.getInstance().getConfig().getStringList("fertilizerUI.fertilizer_river.DisplayLore")) {
                riverLore.add(ChatColor.translateAlternateColorCodes('&', i).replace("%amount%", String.valueOf(getFertilizer_river())));
            }
            itemMetaRiver.setLore(riverLore);
            getFertilizer_river.setItemMeta(itemMetaRiver);

            ItemStack fertilizer_air = new ItemStack(Material.FEATHER);
            ItemMeta itemMetaAir = fertilizer_air.getItemMeta();
            itemMetaAir.setDisplayName(ChatColor.translateAlternateColorCodes('&',TreeSoul.getInstance().getConfig().getString("fertilizerUI.fertilizer_air.DisplayName")));
            List<String> airLore = new ArrayList<>();
            for (String i : TreeSoul.getInstance().getConfig().getStringList("fertilizerUI.fertilizer_air.DisplayLore")) {
                airLore.add(ChatColor.translateAlternateColorCodes('&', i).replace("%amount%", String.valueOf(getFertilizer_air())));
            }
            itemMetaAir.setLore(airLore);
            fertilizer_air.setItemMeta(itemMetaAir);

            fertilizerUI.setItem(10, nbtItem.getItem());
            fertilizerUI.setItem(13, getFertilizer_river);
            fertilizerUI.setItem(16, fertilizer_air);

            for (Player playerOnline : Bukkit.getServer().getOnlinePlayers ()) {
                if (playerOnline.getOpenInventory().getTitle().equals(TreeSoul.getInstance().getConfig().getString("treeUI.displayName").replace("%player%", ""))) {
                    playerOnline.updateInventory();
                }
                if (playerOnline.getOpenInventory().getTitle().equals(TreeSoul.getInstance().getConfig().getString("施肥介面"))) {
                    playerOnline.updateInventory();
                }

            }
            double fertilizer_all = fertilizer_sun + fertilizer_river + fertilizer_river;

            if (fertilizer_all > 50 && fertilizer_all < 300) {
                treestate = State.small_tree;
            }
            if (fertilizer_all >= 300 && fertilizer_all < 2000) {
                treestate = State.big_tree;
            }
            if (fertilizer_all > 2000) {
                treestate = State.ancient_tree;
            }

            double air = getFertilizer_air();
            double river = fertilizer_river;
            double sun = fertilizer_sun;

            if (air > river && air > sun) {
                type = "土壤樹木";
            }
            if (river > air && river > sun) {
                type = "溪水樹木";
            }
            if (sun > river && sun > air) {
                type = "陽光樹木";
            }


        }, 0, 10);

    }

    public Location getTreeLoc() {
        return treeLoc;
    }

    public void setTreeLoc(Location treeLoc) {
        this.treeLoc = treeLoc;
    }

    public State getState() {
        return treestate;
    }

    public void setTreestate(State treestate) {
        this.treestate = treestate;
    }

    public double getFertilizer_sun() {
        return fertilizer_sun;
    }

    public void setFertilizer_sun(double fertilizer_sun) {
        this.fertilizer_sun = fertilizer_sun;
    }

    public double getFertilizer_river() {
        return fertilizer_river;
    }

    public void setFertilizer_river(double fertilizer_river) {
        this.fertilizer_river = fertilizer_river;
    }

    public double getFertilizer_air() {
        return fertilizer_air;
    }

    public void setFertilizer_air(double fertilizer_air) {
        this.fertilizer_air = fertilizer_air;
    }

    public UUID getTreeOwner() {
        return treeOwner;
    }

    public void setTreeOwner(UUID treeOwner) {
        this.treeOwner = treeOwner;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setType(String type) {this.type = type;}

    public String getType() {return type;}

    public Inventory getTreeUI() {
        return treeUI;
    }

    public Inventory getfertilizerUI() {
        return fertilizerUI;
    }
}
