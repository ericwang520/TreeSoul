package me.yiyi1234.treesoul.items;

import me.yiyi1234.treesoul.TreeSoul;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Fertilizer {
    public static ItemStack getFertilizer_Sun() {
        ItemStack fertilizer_Sun = new ItemStack(Material.DOUBLE_PLANT, 1);
        ItemMeta itemMeta = fertilizer_Sun.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',TreeSoul.getInstance().getConfig().getString("fertilizer.fertilizer_sun.DisplayName")));
        List<String> lore = new ArrayList<>();

        for (String i : TreeSoul.getInstance().getConfig().getStringList("fertilizer.fertilizer_sun.DisplayLore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', i));
        }
        itemMeta.setLore(lore);
        fertilizer_Sun.setItemMeta(itemMeta);

        return fertilizer_Sun;
    }

    public static ItemStack getFertilizer_River() {
        ItemStack getFertilizer_river = new ItemStack(351, 1, (short) 12);
        ItemMeta itemMeta = getFertilizer_river.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',TreeSoul.getInstance().getConfig().getString("fertilizer.fertilizer_river.DisplayName")));
        List<String> chestLore = new ArrayList<>();
        for (String i : TreeSoul.getInstance().getConfig().getStringList("fertilizer.fertilizer_river.DisplayLore")) {
            chestLore.add(ChatColor.translateAlternateColorCodes('&', i));
        }
        itemMeta.setLore(chestLore);
        getFertilizer_river.setItemMeta(itemMeta);

        return getFertilizer_river;
    }

    public static ItemStack getFertilizer_Air() {
        ItemStack Fertilizer_air = new ItemStack(Material.FEATHER);
        ItemMeta itemMeta = Fertilizer_air.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',TreeSoul.getInstance().getConfig().getString("fertilizer.fertilizer_air.DisplayName")));
        List<String> chestLore = new ArrayList<>();
        for (String i : TreeSoul.getInstance().getConfig().getStringList("fertilizer.fertilizer_air.DisplayLore")) {
            chestLore.add(ChatColor.translateAlternateColorCodes('&', i));
        }
        itemMeta.setLore(chestLore);
        Fertilizer_air.setItemMeta(itemMeta);

        return Fertilizer_air;
    }
}
