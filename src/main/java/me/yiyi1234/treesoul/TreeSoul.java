package me.yiyi1234.treesoul;

import me.yiyi1234.treesoul.commands.TreeSoulCommand;
import me.yiyi1234.treesoul.core.Prayer;
import me.yiyi1234.treesoul.events.*;
import me.yiyi1234.treesoul.manager.TreeManager;
import me.yiyi1234.treesoul.sql.MySQL;
import me.yiyi1234.treesoul.sql.SQLGetter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

public final class TreeSoul extends JavaPlugin {
    private FileConfiguration config;
    private FileConfiguration tree_Data;
    private FileConfiguration date_Data;
    private static TreeSoul instance;
    public MySQL SQL;
    public SQLGetter data;
    private TreeManager treeManager;
    public int restartDate;

    @Override
    public void onEnable() {
        // Plugin startup logic
        setInstance(this);
        this.SQL = new MySQL();
        this.data = new SQLGetter(this);
        this.treeManager = new TreeManager();
        config();
        setTree_Data();
        setDate_Data();
        try {
            SQL.connect();
        } catch (SQLException throwables) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7<&6TreeSoul&7> &c資料庫連線失敗"));
        }
        if (SQL.isConnected()) {
            this.getServer().getPluginManager().registerEvents(new SignChange(), this);
            this.getServer().getPluginManager().registerEvents(new BlockBreak(), this);
            this.getServer().getPluginManager().registerEvents(new BlockInteract(), this);
            this.getServer().getPluginManager().registerEvents(new ClickEvent(), this);
            this.getServer().getPluginManager().registerEvents(new JoinEvent(), this);
            getCommand("TreeSoul").setExecutor(new TreeSoulCommand());
            getCommand("TreeSoul").setTabCompleter(new TreeSoulCommand());
            TreeManager.loadTree();
            data.createTable();

            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "－－－－－－－－－－－－－－－－－－－－－－");
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6TreeSoul &c成功啟動"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f依依#1350 製作"));
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "－－－－－－－－－－－－－－－－－－－－－－");

            autoSave();
            reset();


        }
    }

    public void autoSave() {
        new BukkitRunnable() {
            public void run() {
                TreeManager.saveTree();
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7<&6TreeSoul&7> &a成功儲存所有資料至 data.yml。"));
            }
        }.runTaskTimer(this, 6000, 6000);
    }

    public void reset() {
        this.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            new BukkitRunnable() {
                public void run() {


                    Date date = new Date(System.currentTimeMillis());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    if (TreeSoul.getInstance().getDate_Data().getString("Date.daily") == null) {
                        TreeSoul.getInstance().getDate_Data().set("Date.daily", calendar.get(Calendar.DAY_OF_YEAR));
                        saveDate_Data();
                    }

                    if (TreeSoul.getInstance().getDate_Data().getString("Date.week")  == null) {
                        TreeSoul.getInstance().getDate_Data().set("Date.week", calendar.get(Calendar.DAY_OF_YEAR));
                        saveDate_Data();
                    }

                    if (TreeSoul.getInstance().getDate_Data().getInt("Date.daily") != calendar.get(Calendar.DAY_OF_YEAR)) {
                        data.resetDaily();
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a樹靈系統&7] &a成功刷新每天簽到上限。"));
                        TreeSoul.getInstance().getDate_Data().set("Date.daily", calendar.get(Calendar.DAY_OF_YEAR));
                        saveDate_Data();
                    }
                    if (calendar.get(Calendar.DAY_OF_YEAR) - TreeSoul.getInstance().getDate_Data().getInt("Date.week") > 7) {
                        data.resetWeek();
                        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a樹靈系統&7] &a成功刷新每月簽到上限。"));
                        TreeSoul.getInstance().getDate_Data().set("Date.week", calendar.get(Calendar.DAY_OF_YEAR));
                        saveDate_Data();
                    }


                }
            }.runTaskTimer(this, 0, 1200);
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        TreeManager.saveTree();
    }

    private void setInstance(TreeSoul instance) {
        TreeSoul.instance = instance;
    }

    public static TreeSoul getInstance() {
        return instance;
    }

    public void config() {
        File file = new File(this.getDataFolder().getAbsolutePath() + "/config.yml");
        if (!file.exists()) {
            Bukkit.getConsoleSender().sendMessage("§7<§6TreeSoul§7> §f正在生成 config.yml！");
            this.getDataFolder().mkdir();
            this.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setTree_Data() {
        File data = new File(this.getDataFolder().getAbsolutePath() + "/data.yml");

        if (!data.exists()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7<&6TreeSoul&7> &f正在生成 data.yml"));
            this.getDataFolder().mkdir();
        }

        tree_Data = YamlConfiguration.loadConfiguration(data);

        try {
            tree_Data.save(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setDate_Data() {
        File data = new File(this.getDataFolder().getAbsolutePath() + "/date.yml");

        if (!data.exists()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7<&6TreeSoul&7> &f正在生成 date.yml"));
            this.getDataFolder().mkdir();
        }

        date_Data = YamlConfiguration.loadConfiguration(data);

        try {
            date_Data.save(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveDate_Data() {
        File data = new File(this.getDataFolder().getAbsolutePath() + "/date.yml");
        try {
            date_Data.save(data);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7<&6TreeSoul&7> &f無法儲存資料到 " + data.getName()));
        }
    }
    public FileConfiguration getDate_Data() {return date_Data;}

    public void saveTree_Data() {
        File data = new File(this.getDataFolder().getAbsolutePath() + "/data.yml");
        try {
            tree_Data.save(data);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7<&6TreeSoul&7> &f無法儲存資料到 " + data.getName()));
        }
    }

    public FileConfiguration getTree_Data() {
        return tree_Data;
    }
}
