package me.yiyi1234.treesoul.manager;

import me.yiyi1234.treesoul.TreeSoul;
import me.yiyi1234.treesoul.core.TreeCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TreeManager {
    public static HashMap<Location, TreeCore> fur = new HashMap<>();
    public static final HashMap<UUID,List<UUID>> today = new HashMap<>(); // 玩家今日祈福了幾個

    public static TreeCore addTree(Location treeLoc, Player createPlayer) {
        if (fur.containsKey(treeLoc)) {
            fur.remove(treeLoc);
        }
        TreeCore treeCore = new TreeCore(treeLoc, createPlayer.getUniqueId());
        fur.put(treeLoc, treeCore);
        return treeCore;
    }

    public static void saveTree() {
        TreeSoul.getInstance().getTree_Data().set("TreeSoul", null);
        fur.keySet().stream().map(loc -> fur.get(loc)).forEach(tree -> {
            String configLocation = tree.getTreeLoc().getWorld().getName() + "," + tree.getTreeLoc().getBlockX() + "," + tree.getTreeLoc().getBlockY() + "," + tree.getTreeLoc().getBlockZ();
            TreeSoul.getInstance().getTree_Data().set("TreeSoul." + configLocation + ".treeState", tree.getState().toString());
            TreeSoul.getInstance().getTree_Data().set("TreeSoul." + configLocation + ".treeOwner", tree.getTreeOwner().toString());
            TreeSoul.getInstance().getTree_Data().set("TreeSoul." + configLocation + ".fertilizer_sun", tree.getFertilizer_sun());
            TreeSoul.getInstance().getTree_Data().set("TreeSoul." + configLocation + ".fertilizer_river", tree.getFertilizer_river());
            TreeSoul.getInstance().getTree_Data().set("TreeSoul." + configLocation + ".fertilizer_air", tree.getFertilizer_air());
            TreeSoul.getInstance().getTree_Data().set("TreeSoul." + configLocation + ".time", tree.getTime());
            TreeSoul.getInstance().getTree_Data().set("TreeSoul." + configLocation + ".type", tree.getType());
        });
        TreeSoul.getInstance().saveTree_Data();
        today.keySet().stream().map(uuid -> today.get(uuid)).forEach(today -> {



        });


    }

    public static void loadTree() {
        int times = 0;

        if (TreeSoul.getInstance().getTree_Data().getConfigurationSection("TreeSoul") != null) {
            for (String treeLoc : TreeSoul.getInstance().getTree_Data().getConfigurationSection("TreeSoul").getKeys(false)) {
                TreeCore.State state = TreeCore.State.valueOf(TreeSoul.getInstance().getTree_Data().getString("TreeSoul." + treeLoc + ".treeState"));
                String treeOwner = TreeSoul.getInstance().getTree_Data().getString("TreeSoul." + treeLoc + ".treeOwner");
                double fertilizer_sun = TreeSoul.getInstance().getTree_Data().getDouble("TreeSoul." + treeLoc + ".fertilizer_sun");
                double fertilizer_river = TreeSoul.getInstance().getTree_Data().getDouble("TreeSoul." + treeLoc + ".fertilizer_river");
                double fertilizer_air = TreeSoul.getInstance().getTree_Data().getDouble("TreeSoul." + treeLoc + ".fertilizer_air");
                long time = TreeSoul.getInstance().getTree_Data().getLong("TreeSoul." + treeLoc + ".time");
                String type = TreeSoul.getInstance().getTree_Data().getString("TreeSoul." + treeLoc + ".type");
                String[] split = treeLoc.split(",");
                World world = Bukkit.getWorld(split[0]);
                Location loc = new Location(world, Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]));
                TreeCore treeCore = new TreeCore(loc, state, fertilizer_sun, fertilizer_river, fertilizer_air, UUID.fromString(treeOwner), time, type);
                fur.put(loc, treeCore);
                times++;
            }


        }

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7<&6TreeSoul&7> &f成功載入 " + times + " 個 樹靈 "));

    }

    public static TreeCore getTreeCore(Location treeLoc) {
        return fur.get(treeLoc);
    }

    public static void deleteTreeCore(Location treeLoc) {
        fur.remove(treeLoc);
    }


}
