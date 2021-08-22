package cc.carm.plugin.userprefix.manager;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.platform.PlayerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * 服务管理器，旨在于LuckPerms互联，调用其原始接口。
 */
public class ServiceManager {

    public static User getUser(Player player) {
        return getAPI().getUser(player);
    }

    public static LuckPerms getService() {
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            return provider.getProvider();
        } else {
            return LuckPermsProvider.get();
        }
    }

    public static PlayerAdapter<Player> getAPI() {
        return getService().getPlayerAdapter(Player.class);
    }


    /**
     * 通过LuckPermsAPI判断玩家是否有对应的权限
     *
     * @param user       用户
     * @param permission 权限
     * @return true / false
     */
    public static boolean hasPermission(User user, String permission) {
        return user.getCachedData().getPermissionData().checkPermission(permission).asBoolean();
    }

}
