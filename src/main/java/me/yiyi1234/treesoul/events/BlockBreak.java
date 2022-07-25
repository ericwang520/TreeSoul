package me.yiyi1234.treesoul.events;

import me.yiyi1234.treesoul.TreeSoul;
import me.yiyi1234.treesoul.core.TreeCore;
import me.yiyi1234.treesoul.manager.TreeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.WALL_SIGN && TreeManager.getTreeCore(event.getBlock().getLocation()) != null)  {
            TreeCore treeCore = TreeManager.getTreeCore(event.getBlock().getLocation());
            if (Bukkit.getPlayer(treeCore.getTreeOwner()) != event.getPlayer()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',"&7[&a樹靈系統&7] &c您不是這棵樹的主人"));
                return;
            }


            TreeManager.deleteTreeCore(event.getBlock().getLocation());
            TreeSoul.getInstance().data.setIsPlaced(event.getPlayer().getUniqueId(), "false");
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a樹靈系統&7] &c成功拆除樹靈。"));
            return;
        }
        Block block = event.getBlock();
        Material sign = Material.WALL_SIGN;



        if (block.getRelative(BlockFace.NORTH).getType() == sign || block.getRelative(BlockFace.EAST).getType() == sign || block.getRelative(BlockFace.WEST).getType() == sign || block.getRelative(BlockFace.SOUTH).getType() == sign) {
            if (TreeManager.getTreeCore(block.getRelative(BlockFace.NORTH).getLocation()) != null || TreeManager.getTreeCore(block.getRelative(BlockFace.EAST).getLocation()) != null || TreeManager.getTreeCore(block.getRelative(BlockFace.WEST).getLocation()) != null || TreeManager.getTreeCore(block.getRelative(BlockFace.SOUTH).getLocation()) != null) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a樹靈系統&7] &c請先拆除附著在方塊上的告示牌，在挖除此方塊。"));
            }
        }
    }

}
