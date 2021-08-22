package cc.carm.plugin.userprefix.listener;

import cc.carm.plugin.userprefix.configuration.PrefixConfig;
import cc.carm.plugin.userprefix.manager.UserManager;
import cc.carm.plugin.userprefix.ui.PrefixSelectGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UserListener implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        UserManager.checkPrefix(player, false);


        if (PrefixConfig.Functions.NAME_PREFIX.get()) {
            UserManager.createNameTag(event.getPlayer());
            UserManager.updatePrefixView(event.getPlayer(), true);
        }


    }


    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        PrefixSelectGUI.removeOpening(event.getPlayer());
        UserManager.unloadNameTag(event.getPlayer().getUniqueId());
        UserManager.checkingPlayers.remove(event.getPlayer().getUniqueId());
    }


}
