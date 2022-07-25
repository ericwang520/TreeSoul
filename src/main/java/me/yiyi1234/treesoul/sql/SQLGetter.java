package me.yiyi1234.treesoul.sql;

import me.yiyi1234.treesoul.TreeSoul;
import me.yiyi1234.treesoul.core.Prayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLGetter {
    private TreeSoul plugin;

    public SQLGetter(TreeSoul plugin) {
        this.plugin = plugin;
    }
    //

    public void createTable() {
        try {

            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS treesouldaily (playeruuid VARCHAR(36), dailysigntimes INTEGER, isplaced VARCHAR(100), weekLog VARCHAR(1500), PrayCount INTEGER);");
            ps.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void createPlayer(UUID playerUUID) {
        try {
            if (!exists(playerUUID)) {
                PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("INSERT INTO treesouldaily (playeruuid,dailysigntimes,isplaced,PrayCount) VALUES (?,?,?,?);");
                ps.setString(1, playerUUID.toString());
                ps.setInt(2,0);
                ps.setString(3, "false");
                ps.setInt(4,0);
                ps.executeUpdate();
                Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&7[&a樹靈系統&7] &a成功建立玩家 " + Bukkit.getPlayer(playerUUID).getName() + " 的個人資料。"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(UUID playerUUID) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM treesouldaily WHERE playeruuid=?;");
            ps.setString(1, playerUUID.toString());
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void dailyPlus(UUID uuid){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE treesouldaily SET dailysigntimes=? WHERE playeruuid=?;");
            ps.setInt(1, getInt(uuid) + 1);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public int getInt(UUID uuid) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT dailysigntimes FROM treesouldaily WHERE playeruuid=?;");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int amount = rs.getInt("dailysigntimes");
                return amount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void setIsPlaced(UUID uuid,String isplaced) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE treesouldaily SET isplaced=? WHERE playeruuid=?;");
            ps.setString(1, isplaced);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean getIsPlaced(UUID uuid) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT isplaced FROM treesouldaily WHERE playeruuid=?;");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean isPlaced =  Boolean.valueOf(rs.getString("isplaced"));
                return isPlaced;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getWeekLogs(UUID uuid) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT weekLog FROM treesouldaily WHERE playeruuid=?;");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String isPlaced =  rs.getString("weekLog");
                return isPlaced;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void setWeekLogs(UUID uuid,String string) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE treesouldaily SET weekLog=? WHERE playeruuid=?;");
            ps.setString(1, string);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void resetDaily(){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE treesouldaily SET dailysigntimes=?;");
            ps.setInt(1, 0);
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void resetWeek(){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE treesouldaily SET weekLog=?;");
            ps.setString(1,null);
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void resetPlayerWeek(UUID uuid){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE treesouldaily SET weekLog=? WHERE playeruuid=?;");
            ps.setString(1,null);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void resetPlayerDaily(UUID uuid){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE treesouldaily SET dailysigntimes=? WHERE playeruuid=?;");
            ps.setString(1,null);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void resetPlayerPlaced(UUID uuid){

        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE treesouldaily SET isplaced=? WHERE playeruuid=?;");
            ps.setString(1,"false");
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void prayCountPlus(UUID uuid){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE treesouldaily SET PrayCount=? WHERE playeruuid=?;");
            ps.setInt(1, getPrayCount(uuid) + 1);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public int getPrayCount(UUID uuid) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT PrayCount FROM treesouldaily WHERE playeruuid=?;");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int amount = rs.getInt("PrayCount");
                return amount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public void resetPrayCount(UUID uuid){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE treesouldaily SET PrayCount=? WHERE playeruuid=?;");
            ps.setInt(1, 0);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
