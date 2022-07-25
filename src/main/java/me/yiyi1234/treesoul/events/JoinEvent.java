package me.yiyi1234.treesoul.events;

import me.yiyi1234.treesoul.TreeSoul;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class JoinEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        TreeSoul.getInstance().data.createPlayer(event.getPlayer().getUniqueId());


    }
}
