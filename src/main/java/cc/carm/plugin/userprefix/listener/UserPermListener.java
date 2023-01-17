package cc.carm.plugin.userprefix.listener;

import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.UserPrefixAPI;
import cc.carm.plugin.userprefix.ui.PrefixSelectGUI;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class UserPermListener {

    public static void process(UserDataRecalculateEvent event) {
        Player player = Bukkit.getPlayer(event.getUser().getUniqueId());
        if (player == null) return;
        UserPrefixAPI.getUserManager().checkPrefix(player, true);
        if (PrefixSelectGUI.openingUsers.contains(player)) {
            Main.getInstance().getScheduler().run(() -> {
                // 玩家权限更新，同步关闭其GUI，以令其重新打开刷新自己的前缀。
                player.closeInventory();
                PrefixSelectGUI.removeOpening(player);
            });
        }
    }

}
