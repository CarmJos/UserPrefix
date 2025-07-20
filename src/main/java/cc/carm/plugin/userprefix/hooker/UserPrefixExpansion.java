package cc.carm.plugin.userprefix.hooker;

import cc.carm.lib.easyplugin.papi.EasyPlaceholder;
import cc.carm.lib.easyplugin.papi.expansion.SubExpansion;
import cc.carm.lib.easyplugin.papi.handler.PlaceholderHandler;
import cc.carm.plugin.userprefix.UserPrefixAPI;
import cc.carm.plugin.userprefix.conf.prefix.PrefixConfig;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Function;

public class UserPrefixExpansion extends EasyPlaceholder {

    public UserPrefixExpansion(@NotNull JavaPlugin plugin, @NotNull String rootIdentifier) {
        super(plugin, rootIdentifier);

        handle("version", (player, args) -> getVersion());

        handle("identifier", handlePrefix(PrefixConfig::getIdentifier), "id");
        handle("prefix", handlePrefix(PrefixConfig::getContent));
        handle("name", handlePrefix(PrefixConfig::getName));
        handle("description", handlePrefix(p -> String.join("\n", p.getDescription())));
        handle("weight", handlePrefix(PrefixConfig::getWeight));
        handle("amount", handlePlayer(
                (player) -> UserPrefixAPI.getUserManager().getUsablePrefixes(player).size() + 1)
        );

        handle("has", handlePlayer((player, args) -> {
            if (args.length < 1) return "参数不足";
            PrefixConfig prefix = UserPrefixAPI.getPrefixManager().getPrefix(args[0]);
            if (prefix == null) return "该前缀不存在";
            return prefix.checkPermission(player);
        }), Collections.singletonList("<前缀ID>"));

    }

    public PlaceholderHandler handlePrefix(Function<PrefixConfig, Object> handler) {
        return handlePlayer((player, args) -> handler.apply(UserPrefixAPI.getUserManager().getPrefix(player)));
    }

    public PlaceholderHandler handlePrefix(BiFunction<PrefixConfig, Player, Object> handler) {
        return handlePlayer((player, args) -> handler.apply(UserPrefixAPI.getUserManager().getPrefix(player), player));
    }

    /**
     * This is required or else PlaceholderAPI will unregister the Expansion on reload
     */
    @Override
    public boolean persist() {
        return true;
    }

    public PlaceholderHandler handlePlayer(BiFunction<Player, String[], Object> handler) {
        return (player, args) -> {
            if (player == null || !player.isOnline()) return "Loading...";
            return handler.apply((Player) player, args);
        };
    }

    public PlaceholderHandler handlePlayer(Function<Player, Object> handler) {
        return handlePlayer((player, args) -> handler.apply(player));
    }

    @Override
    public String onErrorParams(@Nullable OfflinePlayer player) {
        return "参数不足";
    }

    @Override
    public String onException(@Nullable OfflinePlayer player, @NotNull SubExpansion<?> expansion,
                              @NotNull Exception exception) {
        exception.printStackTrace();
        return "参数错误";
    }

}
