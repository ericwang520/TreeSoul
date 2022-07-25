package me.yiyi1234.treesoul.commands;

import me.yiyi1234.treesoul.TreeSoul;
import me.yiyi1234.treesoul.items.Fertilizer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TreeSoulCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender.hasPermission("TreeSoul.op"))) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a樹靈系統&7] &c您沒有權限。"));
            return false;
        }

        Player player = Bukkit.getPlayer(sender.getName());
        // Treesoul

        if (args[0].equalsIgnoreCase("get")) {
            if (args[1].equalsIgnoreCase("air")) {
                player.getInventory().setItemInMainHand(Fertilizer.getFertilizer_Air());
            }
            if (args[1].equalsIgnoreCase("river")) {
                player.getInventory().setItemInMainHand(Fertilizer.getFertilizer_River());
            }
            if (args[1].equalsIgnoreCase("sun")) {
                player.getInventory().setItemInMainHand(Fertilizer.getFertilizer_Sun());
            }
        }

        if (args[0].equalsIgnoreCase("reset")) {
            if (args[1].equalsIgnoreCase("isplaced")) {
                if (args.length != 3) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6系統&7] &c使用方式 /treesoul reset isplaced <ID>"));
                    return false;
                }
                TreeSoul.getInstance().data.resetPlayerPlaced(Bukkit.getOfflinePlayer(args[2]).getUniqueId());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6系統&7] &a成功刷新 該玩家的 isplaced。"));
                return false;
            }

            if (args[1].equalsIgnoreCase("daily")) {
                if (args.length == 3) {
                    TreeSoul.getInstance().data.resetPlayerDaily(Bukkit.getOfflinePlayer(args[2]).getUniqueId());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6系統&7] &a成功刷新 該玩家的日限制。"));
                    return false;
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6系統&7] &c使用方式 /treesoul reset daily <ID>"));
                    return false;
                }

            }
            if (args[1].equalsIgnoreCase("week")) {
                if (args.length == 3) {
                    TreeSoul.getInstance().data.resetPlayerWeek(Bukkit.getOfflinePlayer(args[2]).getUniqueId());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6系統&7] &a成功刷新 該玩家的周紀錄。"));
                    return false;
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6系統&7] &c使用方式 /treesoul reset week <ID>"));
                    return false;
                }

            }

        }


        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("TreeSoul.op")) {
            if (args.length == 1) {

                List<String> arguments = new ArrayList<>();
                arguments.add("get");
                arguments.add("reset");
                return arguments;

            } else if (args[0].equalsIgnoreCase("get")) {

                List<String> arguments = new ArrayList<>();
                arguments.add("air");
                arguments.add("river");
                arguments.add("sun");
                return arguments;

            } else if (args[0].equalsIgnoreCase("reset")) {
                List<String> arguments = new ArrayList<>();
                arguments.add("isplaced");
                arguments.add("daily");
                arguments.add("week");
                return arguments;
            }
        }
        return null;
    }
}
