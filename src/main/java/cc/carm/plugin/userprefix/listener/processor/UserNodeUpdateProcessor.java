package cc.carm.plugin.userprefix.listener.processor;

import net.luckperms.api.event.user.UserDataRecalculateEvent;
import net.luckperms.api.model.user.User;
import cc.carm.plugin.userprefix.manager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UserNodeUpdateProcessor {

//    public static void process(NodeRemoveEvent event) {
//        if (event.getTarget() instanceof User) {
//            if (!(event.getNode() instanceof PermissionNode)) return;
//            User user = (User) event.getTarget();
//            Player player = Bukkit.getPlayer(user.getUniqueId());
//            if (player == null) return;
//            UserManager.checkPrefix(player, true);
//        }
//    }

    public static void process(UserDataRecalculateEvent event) {
        User user = event.getUser();
        Player player = Bukkit.getPlayer(user.getUniqueId());
        if (player == null) return;
        UserManager.checkPrefix(player, true);

    }

}
