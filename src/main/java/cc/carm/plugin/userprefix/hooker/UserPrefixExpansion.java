package cc.carm.plugin.userprefix.hooker;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import cc.carm.plugin.userprefix.manager.PrefixManager;
import cc.carm.plugin.userprefix.manager.UserManager;
import cc.carm.plugin.userprefix.model.ConfiguredPrefix;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserPrefixExpansion extends PlaceholderExpansion {

    JavaPlugin plugin;

    public UserPrefixExpansion(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        List<String> placeholders = new ArrayList<>();
        placeholders.add("%UserPrefix_prefix%");
        placeholders.add("%UserPrefix_weight%");
        placeholders.add("%UserPrefix_identifier%");
        placeholders.add("%UserPrefix_name%");
        placeholders.add("%UserPrefix_has_<Identifier>%");
        return placeholders;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "UserPrefix";
    }

    @Override
    public String getVersion() {
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
                return UserManager.getPrefix(player).getIdentifier();
            }
            case "prefix": {
                return UserManager.getPrefix(player).getContent();
            }
            case "name": {
                return UserManager.getPrefix(player).getName();
            }
            case "weight": {
                return Integer.toString(UserManager.getPrefix(player).getWeight());
            }
            case "has": {
                if (args.length < 2) return "参数不足";
                ConfiguredPrefix prefix = PrefixManager.getPrefix(args[1]);
                if (prefix == null) return "该前缀不存在";
                return Boolean.toString(UserManager.isPrefixUsable(player, prefix));
            }
            case "version": {
                return getVersion().replace("-SNAPSHOT", "");
            }
        }
        return null;
    }

}
