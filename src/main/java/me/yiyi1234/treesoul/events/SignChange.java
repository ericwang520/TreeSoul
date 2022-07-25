package me.yiyi1234.treesoul.events;

import me.yiyi1234.treesoul.TreeSoul;
import me.yiyi1234.treesoul.manager.TreeManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChange implements Listener {
    @EventHandler
    public void onSignChange(SignChangeEvent event) {

        String configformat = TreeSoul.getInstance().getConfig().getString("sign-format");
        String[] lines = event.getLines();


        if (lines[0].contains(configformat)) {
            if (event.getBlock().getType() == Material.WALL_SIGN) {

                boolean haveLog = false;
                boolean haveleaves = false;
                int radius = 3;
                Block block = event.getBlock();
                for (int x = -(radius); x <= radius; x ++) {
                    for (int y = -(radius); y <= radius; y ++) {
                        for (int z = -(radius); z <= radius; z ++) {
                            if (block.getRelative(x,y,z).getType() == Material.LOG) {
                                haveLog = true;
                            }
                            if (block.getRelative(x,y,z).getType() == Material.LEAVES) {
                                haveleaves = true;
                            }
                            if (block.getRelative(x,y,z).getType() == Material.WALL_SIGN) {
                                if (!block.getRelative(x,y,z).getLocation().equals(block.getLocation())) {
                                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a樹靈系統&7] &c請先拆除周圍 3x3 的告示牌"));
                                    return;
                                }
                            }
                        }
                    }
                }



                if (haveLog && haveleaves) {
                    if (!TreeSoul.getInstance().data.getIsPlaced(event.getPlayer().getUniqueId())){
                        TreeManager.addTree(event.getBlock().getLocation(), event.getPlayer());
                        TreeSoul.getInstance().data.setIsPlaced(event.getPlayer().getUniqueId(), "true");
                        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a樹靈系統&7] &a成功創建樹靈。"));

                        return;
                    }else {
                        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a樹靈系統&7] &c您只能放置一顆樹靈。"));
                    }

                }else if (!haveLog && !haveleaves) {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a樹靈系統&7] &c周圍 3x3 沒有 原木及樹葉 &7(以告示牌為中心)"));
                    return;
                } else if (!haveLog) {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a樹靈系統&7] &c周圍 3x3 沒有 原木 &7(以告示牌為中心)"));
                    return;
                }else if (!haveleaves) {
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a樹靈系統&7] &c周圍 3x3 沒有 樹葉 &7(以告示牌為中心)"));
                    return;
                }
            }else {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a樹靈系統&7] &c請把告示牌放在樹上，而不是插在地上。 &7(F3 對準告示牌會顯示 minecraft:wall_sign)"));
            }

        }




    }
}
