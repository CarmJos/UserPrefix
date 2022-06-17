package cc.carm.plugin.userprefix.hooker;

import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.UserPrefix;
import cc.carm.plugin.userprefix.configuration.prefix.PrefixConfig;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class UserPrefixExpansion extends PlaceholderExpansion {

    protected final @NotNull Main plugin;
    protected final @NotNull List<String> placeholders = Arrays.asList(
            "%UserPrefix_prefix%", "%UserPrefix_weight%",
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
                return UserPrefix.getUserManager().getPrefix(player).getIdentifier();
            }
            case "prefix": {
                return UserPrefix.getUserManager().getPrefix(player).getContent();
            }
            case "name": {
                return UserPrefix.getUserManager().getPrefix(player).getName();
            }
            case "weight": {
                return Integer.toString(UserPrefix.getUserManager().getPrefix(player).getWeight());
            }
            case "has": {
                if (args.length < 2) return "参数不足";
                PrefixConfig prefix = UserPrefix.getPrefixManager().getPrefix(args[1]);
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
