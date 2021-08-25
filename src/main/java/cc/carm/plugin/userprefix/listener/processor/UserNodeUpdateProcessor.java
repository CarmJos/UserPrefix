package cc.carm.plugin.userprefix.listener.processor;

import cc.carm.plugin.userprefix.manager.UserManager;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UserNodeUpdateProcessor {

    public static void process(UserDataRecalculateEvent event) {
        Player player = Bukkit.getPlayer(event.getUser().getUniqueId());
        if (player == null) return;
        UserManager.checkPrefix(player, true);
    }

}
