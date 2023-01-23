package cc.carm.plugin.userprefix.hooker;

import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.UserPrefixAPI;
import cc.carm.plugin.userprefix.conf.prefix.PrefixConfig;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class UserPrefixExpansion extends PlaceholderExpansion {

    protected final @NotNull Main plugin;
    protected final @NotNull List<String> placeholders = Arrays.asList(
            "%UserPrefix_prefix%", "%UserPrefix_amount%",  "%UserPrefix_weight%",
            "%UserPrefix_identifier%", "%UserPrefix_name%",
            "%UserPrefix_has_<Identifier>%"
    );

    public UserPrefixExpansion(@NotNull Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return this.placeholders;
    }

    @Override
    public boolean canRegister() {
        return true;
    }
    
    /**
     * This is required or else PlaceholderAPI will unregister the Expansion on reload
     */
    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "UserPrefix";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null) return "加载中...";
        String[] args = identifier.split("_");


        if (args.length < 1) {
            return "参数不足";
        }

        switch (args[0].toLowerCase()) {
            case "identifier": {
                return UserPrefixAPI.getUserManager().getPrefix(player).getIdentifier();
            }
            case "prefix": {
                return UserPrefixAPI.getUserManager().getPrefix(player).getContent();
            }
            case "amount": {
                return String.valueOf(UserPrefixAPI.getUserManager().getUsablePrefixes(player).size() + 1);
            }
            case "name": {
                return UserPrefixAPI.getUserManager().getPrefix(player).getName();
            }
            case "weight": {
                return Integer.toString(UserPrefixAPI.getUserManager().getPrefix(player).getWeight());
            }
            case "has": {
                if (args.length < 2) return "参数不足";
                PrefixConfig prefix = UserPrefixAPI.getPrefixManager().getPrefix(args[1]);
                if (prefix == null) return "该前缀不存在";
                return Boolean.toString(prefix.checkPermission(player));
            }
            case "version": {
                return getVersion();
            }
            default: {
                return "参数错误";
            }
        }
    }

}
