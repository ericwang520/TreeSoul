package me.yiyi1234.treesoul.events;

import de.tr7zw.nbtapi.NBTItem;
import me.yiyi1234.treesoul.TreeSoul;
import me.yiyi1234.treesoul.core.TreeCore;
import me.yiyi1234.treesoul.items.Fertilizer;
import me.yiyi1234.treesoul.manager.TreeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ClickEvent implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory buttomInv = event.getView().getBottomInventory();

        String UI_DisplayName = TreeSoul.getInstance().getConfig().getString("treeUI.displayName");


        if (event.getView().getTitle().contains(UI_DisplayName.replace("%player%", ""))) {
            if (event.getClickedInventory() == event.getView().getBottomInventory()) {
                return;
            }
            if (event.getClickedInventory() == null) {
                return;
            }
            event.setCancelled(true);

            if (event.getSlot() == 16) {
                ItemStack chest = event.getClickedInventory().getItem(10);
                NBTItem nbtChest = new NBTItem(chest);
                String[] info = nbtChest.getString("blockLoc").split(",");
                Location blockLoc = new Location(Bukkit.getWorld(info[0]), Double.parseDouble(info[1]), Double.parseDouble(info[2]), Double.parseDouble(info[3]));
                TreeCore treeCore = TreeManager.getTreeCore(blockLoc);

                event.getWhoClicked().openInventory(treeCore.getfertilizerUI());

            }
            if (event.getSlot() == 10) {
                ItemStack chest = event.getClickedInventory().getItem(10);
                NBTItem nbtChest = new NBTItem(chest);
                String[] info = nbtChest.getString("blockLoc").split(",");
                Location blockLoc = new Location(Bukkit.getWorld(info[0]), Double.parseDouble(info[1]), Double.parseDouble(info[2]), Double.parseDouble(info[3]));
                TreeCore treeCore = TreeManager.getTreeCore(blockLoc);


                if (TreeSoul.getInstance().data.getPrayCount(event.getWhoClicked().getUniqueId()) != 0) {
                    long nowTime = System.currentTimeMillis();
                    long coreTime = treeCore.getTime();
                    if (nowTime - coreTime < 1440000) {
                        String sendMsg = TreeSoul.getInstance().getConfig().getString("prize_commands.prize_owner").replace("%player%", event.getWhoClicked().getName()).replace("%type_of_tree%", treeCore.getType()).replace("%tree_level%", treeCore.getState().toString()).replace("%bonus%", "1");
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), sendMsg);
                    }
                    if (nowTime - coreTime >= 1440000 && nowTime - coreTime < 2880000) {
                        String sendMsg = TreeSoul.getInstance().getConfig().getString("prize_commands.prize_owner").replace("%player%", event.getWhoClicked().getName()).replace("%type_of_tree%", treeCore.getType()).replace("%tree_level%", treeCore.getState().toString()).replace("%bonus%", "2");
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), sendMsg);
                    }
                    if (nowTime - coreTime >= 2880000 && nowTime - coreTime < 4320000) {
                        String sendMsg = TreeSoul.getInstance().getConfig().getString("prize_commands.prize_owner").replace("%player%", event.getWhoClicked().getName()).replace("%type_of_tree%", treeCore.getType()).replace("%tree_level%", treeCore.getState().toString()).replace("%bonus%", "3");
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), sendMsg);
                    }
                    if (nowTime - coreTime >= 4320000) {
                        String sendMsg = TreeSoul.getInstance().getConfig().getString("prize_commands.prize_owner").replace("%player%", event.getWhoClicked().getName()).replace("%type_of_tree%", treeCore.getType()).replace("%tree_level%", treeCore.getState().toString()).replace("%bonus%", "4");
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), sendMsg);
                    }
                    TreeSoul.getInstance().data.resetPrayCount(event.getWhoClicked().getUniqueId());
                } else {
                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a樹靈系統&7] &c您沒有獎勵可以領取。"));
                }

            }

        }

        if (event.getView().getTitle().contains("施肥介面")) {
            if (event.getClickedInventory() == null) {
                return;
            }
            if (event.getClickedInventory() == event.getView().getBottomInventory()) {
                return;
            }
            event.setCancelled(true);

            ItemStack chest = event.getClickedInventory().getItem(10);
            NBTItem nbtChest = new NBTItem(chest);
            String[] info = nbtChest.getString("blockLoc").split(",");
            Location blockLoc = new Location(Bukkit.getWorld(info[0]), Double.parseDouble(info[1]), Double.parseDouble(info[2]), Double.parseDouble(info[3]));
            TreeCore treeCore = TreeManager.getTreeCore(blockLoc);


            if (event.getSlot() == 10) {

                if (event.getClick() == ClickType.LEFT && event.getClick() != ClickType.SHIFT_LEFT) {
                    int itemAmount = 0;
                    for (int i = 0; i < 35; i++) {
                        if (buttomInv.getItem(i) != null && buttomInv.getItem(i).getItemMeta() != null && buttomInv.getItem(i).getItemMeta().getDisplayName() != null) {

                            if (buttomInv.getItem(i).getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("fertilizer.fertilizer_sun.DisplayName")))) {
                                itemAmount = itemAmount + buttomInv.getItem(i).getAmount();
                            }

                        }
                    }
                    if (itemAmount < 1) {
                        event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.failFertilizer_sun")));
                        return;
                    }


                    for (int i = 0; i < 35; i++) {
                        if (buttomInv.getItem(i) != null && buttomInv.getItem(i).getItemMeta().getDisplayName() != null) {
                            if (buttomInv.getItem(i).getItemMeta().getDisplayName().contains(Fertilizer.getFertilizer_Sun().getItemMeta().getDisplayName())) {
                                ItemStack current = buttomInv.getItem(i);
                                if (current.getAmount() == 1) {
                                    current.setAmount(0);
                                    treeCore.setFertilizer_sun(treeCore.getFertilizer_sun() + 1);
                                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.successFertilizer_sun").replace("%amount%", String.valueOf(treeCore.getFertilizer_sun())).replace("%fertitizer_amount%", "1")));
                                    return;
                                } else {
                                    current.setAmount(current.getAmount() - 1);
                                    treeCore.setFertilizer_sun(treeCore.getFertilizer_sun() + 1);
                                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.successFertilizer_sun").replace("%amount%", String.valueOf(treeCore.getFertilizer_sun())).replace("%fertitizer_amount%", "1")));
                                    return;
                                }
                            }
                        }
                    }


                }


                int minusAmount = 10;
                if (event.getClick() != ClickType.LEFT && event.getClick() == ClickType.SHIFT_LEFT) {
                    int itemAmount = 0;
                    for (int i = 0; i < 35; i++) {
                        if (buttomInv.getItem(i) != null && buttomInv.getItem(i).getItemMeta() != null && buttomInv.getItem(i).getItemMeta().getDisplayName() != null) {

                            if (buttomInv.getItem(i).getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("fertilizer.fertilizer_sun.DisplayName")))) {
                                itemAmount = itemAmount + buttomInv.getItem(i).getAmount();
                            }

                        }
                    }
                    if (itemAmount < 10) {
                        event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.failFertilizer_sun")));
                        return;
                    }
                    for (int i = 0; i < 35; i++) {

                        if (buttomInv.getItem(i) != null && buttomInv.getItem(i).getItemMeta().getDisplayName() != null) {
                            if (buttomInv.getItem(i).getItemMeta().getDisplayName().contains(Fertilizer.getFertilizer_Sun().getItemMeta().getDisplayName())) {
                                ItemStack current = buttomInv.getItem(i);
                                if (current.getAmount() >= minusAmount) {
                                    current.setAmount(current.getAmount() - minusAmount);
                                    break;
                                } else if (minusAmount != 0 && current.getAmount() < minusAmount) {
                                    minusAmount = minusAmount - current.getAmount();
                                    current.setAmount(0);
                                }
                            }
                        }
                    }
                    treeCore.setFertilizer_sun(treeCore.getFertilizer_sun() + 10);
                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.successFertilizer_sun").replace("%amount%", String.valueOf(treeCore.getFertilizer_sun())).replace("%fertitizer_amount%", "10")));

                }

            }


            if (event.getSlot() == 13) {

                if (event.getClick() == ClickType.LEFT && event.getClick() != ClickType.SHIFT_LEFT) {
                    int itemAmount = 0;
                    for (int i = 0; i < 35; i++) {
                        if (buttomInv.getItem(i) != null && buttomInv.getItem(i).getItemMeta() != null && buttomInv.getItem(i).getItemMeta().getDisplayName() != null) {

                            if (buttomInv.getItem(i).getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("fertilizer.fertilizer_river.DisplayName")))) {
                                itemAmount = itemAmount + buttomInv.getItem(i).getAmount();
                            }

                        }
                    }
                    if (itemAmount < 1) {
                        event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.failFertilizer_river")));
                        return;
                    }

                    for (int i = 0; i < 35; i++) {
                        if (buttomInv.getItem(i) != null && buttomInv.getItem(i).getItemMeta().getDisplayName() != null) {
                            if (buttomInv.getItem(i).getItemMeta().getDisplayName().contains(Fertilizer.getFertilizer_River().getItemMeta().getDisplayName())) {
                                ItemStack current = buttomInv.getItem(i);
                                if (current.getAmount() == 1) {
                                    current.setAmount(0);
                                    treeCore.setFertilizer_river(treeCore.getFertilizer_river() + 1);
                                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.successFertilizer_river").replace("%amount%", String.valueOf(treeCore.getFertilizer_river())).replace("%fertitizer_amount%", "1")));
                                    return;
                                } else {
                                    current.setAmount(current.getAmount() - 1);
                                    treeCore.setFertilizer_river(treeCore.getFertilizer_river() + 1);
                                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.successFertilizer_river").replace("%amount%", String.valueOf(treeCore.getFertilizer_river())).replace("%fertitizer_amount%", "1")));
                                    return;
                                }
                            }
                        }
                    }

                }


                int minusAmount = 10;
                if (event.getClick() != ClickType.LEFT && event.getClick() == ClickType.SHIFT_LEFT) {
                    int itemAmount = 0;
                    for (int i = 0; i < 35; i++) {
                        if (buttomInv.getItem(i) != null && buttomInv.getItem(i).getItemMeta() != null && buttomInv.getItem(i).getItemMeta().getDisplayName() != null) {

                            if (buttomInv.getItem(i).getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("fertilizer.fertilizer_river.DisplayName")))) {
                                itemAmount = itemAmount + buttomInv.getItem(i).getAmount();
                            }

                        }
                    }
                    if (itemAmount < 10) {
                        event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.failFertilizer_river")));
                        return;
                    }

                    for (int i = 0; i < 35; i++) {
                        if (buttomInv.getItem(i) != null && buttomInv.getItem(i).getItemMeta().getDisplayName() != null) {
                            if (buttomInv.getItem(i).getItemMeta().getDisplayName().contains(Fertilizer.getFertilizer_River().getItemMeta().getDisplayName())) {
                                ItemStack current = buttomInv.getItem(i);
                                if (current.getAmount() >= minusAmount) {
                                    current.setAmount(current.getAmount() - minusAmount);
                                    break;
                                } else if (minusAmount != 0 && current.getAmount() < minusAmount) {
                                    minusAmount = minusAmount - current.getAmount();
                                    current.setAmount(0);
                                }
                            }
                        }
                    }
                    treeCore.setFertilizer_river(treeCore.getFertilizer_river() + 10);
                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.successFertilizer_river").replace("%amount%", String.valueOf(treeCore.getFertilizer_river())).replace("%fertitizer_amount%", "10")));


                }

            }


            if (event.getSlot() == 16) {

                if (event.getClick() == ClickType.LEFT && event.getClick() != ClickType.SHIFT_LEFT) {

                    int itemAmount = 0;
                    for (int i = 0; i < 35; i++) {
                        if (buttomInv.getItem(i) != null && buttomInv.getItem(i).getItemMeta() != null && buttomInv.getItem(i).getItemMeta().getDisplayName() != null) {

                            if (buttomInv.getItem(i).getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("fertilizer.fertilizer_air.DisplayName")))) {
                                itemAmount = itemAmount + buttomInv.getItem(i).getAmount();
                            }

                        }
                    }
                    if (itemAmount < 1) {
                        event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.failFertilizer_air")));
                        return;
                    }

                    for (int i = 0; i < 35; i++) {
                        if (buttomInv.getItem(i) != null && buttomInv.getItem(i).getItemMeta().getDisplayName() != null) {
                            if (buttomInv.getItem(i).getItemMeta().getDisplayName().contains(Fertilizer.getFertilizer_Air().getItemMeta().getDisplayName())) {
                                ItemStack current = buttomInv.getItem(i);
                                if (current.getAmount() == 1) {
                                    current.setAmount(0);
                                    treeCore.setFertilizer_air(treeCore.getFertilizer_air() + 1);
                                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.successFertilizer_air").replace("%amount%", String.valueOf(treeCore.getFertilizer_air())).replace("%fertitizer_amount%", "1")));
                                    return;
                                } else {
                                    current.setAmount(current.getAmount() - 1);
                                    treeCore.setFertilizer_air(treeCore.getFertilizer_air() + 1);
                                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.successFertilizer_air").replace("%amount%", String.valueOf(treeCore.getFertilizer_air())).replace("%fertitizer_amount%", "1")));
                                    return;
                                }
                            }
                        }
                    }


                }


                int minusAmount = 10;
                if (event.getClick() != ClickType.LEFT && event.getClick() == ClickType.SHIFT_LEFT) {
                    int itemAmount = 0;
                    for (int i = 0; i < 35; i++) {
                        if (buttomInv.getItem(i) != null && buttomInv.getItem(i).getItemMeta() != null && buttomInv.getItem(i).getItemMeta().getDisplayName() != null) {

                            if (buttomInv.getItem(i).getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("fertilizer.fertilizer_air.DisplayName")))) {
                                itemAmount = itemAmount + buttomInv.getItem(i).getAmount();
                            }

                        }
                    }
                    if (itemAmount < 10) {
                        event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.failFertilizer_air")));
                        return;
                    }

                    for (int i = 0; i < 35; i++) {
                        if (buttomInv.getItem(i) != null && buttomInv.getItem(i).getItemMeta().getDisplayName() != null) {
                            if (buttomInv.getItem(i).getItemMeta().getDisplayName().contains(Fertilizer.getFertilizer_Air().getItemMeta().getDisplayName())) {
                                ItemStack current = buttomInv.getItem(i);
                                if (current.getAmount() >= minusAmount) {
                                    current.setAmount(current.getAmount() - minusAmount);
                                    break;
                                } else if (minusAmount != 0 && current.getAmount() < minusAmount) {
                                    minusAmount = minusAmount - current.getAmount();
                                    current.setAmount(0);
                                }
                            }
                        }
                    }
                    treeCore.setFertilizer_air(treeCore.getFertilizer_air() + 10);
                    event.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', TreeSoul.getInstance().getConfig().getString("translate.successFertilizer_air").replace("%amount%", String.valueOf(treeCore.getFertilizer_air())).replace("%fertitizer_amount%", "10")));


                }


            }


        }


    }


    @EventHandler
    public void dragEvent(InventoryDragEvent event) {
        String UI_DisplayName = TreeSoul.getInstance().getConfig().getString("treeUI.displayName");
        if (event.getView().getTitle().contains(UI_DisplayName.replace("%player%", ""))) {

            event.setCancelled(true);


        }


    }
}
