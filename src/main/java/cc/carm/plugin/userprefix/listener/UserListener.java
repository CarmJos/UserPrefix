package cc.carm.plugin.userprefix.listener;

import cc.carm.plugin.userprefix.manager.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UserManager.initPlayer(event.getPlayer());
    }


    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        UserManager.unloadPlayer(event.getPlayer());
    }


}
