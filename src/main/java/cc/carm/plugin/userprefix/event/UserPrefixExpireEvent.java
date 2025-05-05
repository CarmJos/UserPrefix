package cc.carm.plugin.userprefix.event;

import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.conf.prefix.PrefixConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class UserPrefixExpireEvent extends UserPrefixEvent {

    public static HandlerList handler = new HandlerList();


    public final @NotNull PrefixConfig expiredPrefix;

    public UserPrefixExpireEvent(@NotNull Player who,
                                 @NotNull PrefixConfig expiredPrefix) {
        super(who);
        this.expiredPrefix = expiredPrefix;
    }

    public @NotNull PrefixConfig getExpiredPrefix() {
        return expiredPrefix;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handler;
    }

    public static void call(@NotNull Player player, @NotNull PrefixConfig currentPrefix) {
        Main.getInstance().getFoliaScheduler().runGlobal(true, () -> Bukkit.getPluginManager().callEvent(new UserPrefixExpireEvent(player, currentPrefix)));
    }

}
