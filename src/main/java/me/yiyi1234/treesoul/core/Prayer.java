package me.yiyi1234.treesoul.core;

import me.yiyi1234.treesoul.TreeSoul;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Prayer {

    private final UUID uuid;
    private final HashMap<UUID,UUID> prayWeek = new HashMap<>();


    public Prayer(UUID uuid){
        this.uuid = uuid;
    }
    public void pray(TreeCore core, Player player){

        int prayDailyAmount = TreeSoul.getInstance().data.getInt(player.getUniqueId());
        if (prayDailyAmount > 4) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.praylimitday")));
            return;
        }

        if (TreeSoul.getInstance().data.getWeekLogs(player.getUniqueId()) != null && TreeSoul.getInstance().data.getWeekLogs(player.getUniqueId()).contains(core.getTreeOwner().toString())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.praylimitweek")));
            return;
        }

        if (TreeSoul.getInstance().data.getWeekLogs(player.getUniqueId()) == null) {
            TreeSoul.getInstance().data.setWeekLogs(player.getUniqueId(),  core.getTreeOwner().toString());
        }else {
            TreeSoul.getInstance().data.setWeekLogs(player.getUniqueId(), TreeSoul.getInstance().data.getWeekLogs(player.getUniqueId()) + "," + core.getTreeOwner().toString());
        }

        long nowTime = System.currentTimeMillis();
        long coreTime = core.getTime();
        if (nowTime - coreTime < 1440000) {
            String msg = TreeSoul.getInstance().getConfig().getString("prize_commands.prize_customer").replace("%player%", player.getName()).replace("%type_of_tree%", core.getType()).replace("%tree_level%",core.getState().toString()).replace("%bonus%", "1");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), msg);
        }
        if (nowTime - coreTime >= 1440000 && nowTime - coreTime < 2880000) {
            String msg = TreeSoul.getInstance().getConfig().getString("prize_commands.prize_customer").replace("%player%", player.getName()).replace("%type_of_tree%", core.getType()).replace("%tree_level%",core.getState().toString()).replace("%bonus%", "2");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), msg);
        }
        if (nowTime - coreTime >= 2880000 && nowTime - coreTime < 4320000) {
            String msg = TreeSoul.getInstance().getConfig().getString("prize_commands.prize_customer").replace("%player%", player.getName()).replace("%type_of_tree%", core.getType()).replace("%tree_level%",core.getState().toString()).replace("%bonus%", "3");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), msg);
        }
        if (nowTime - coreTime >= 4320000) {
            String msg = TreeSoul.getInstance().getConfig().getString("prize_commands.prize_customer").replace("%player%", player.getName()).replace("%type_of_tree%", core.getType()).replace("%tree_level%",core.getState().toString()).replace("%bonus%", "4");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), msg);
        }



        TreeSoul.getInstance().data.dailyPlus(player.getUniqueId());
        TreeSoul.getInstance().data.prayCountPlus(core.getTreeOwner());
        core.setTime(System.currentTimeMillis());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.praysuccess").replace("%amount%", String.valueOf(TreeSoul.getInstance().data.getInt(player.getUniqueId())))));
    }


}
