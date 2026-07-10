package cc.carm.plugin.userprefix.command;

import cc.carm.plugin.userprefix.UserPrefixAPI;
import cc.carm.plugin.userprefix.conf.PluginMessages;
import cc.carm.plugin.userprefix.manager.PrefixManager;
import cc.carm.plugin.userprefix.ui.PrefixSelectGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserCommand implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String group = strings.length > 0 ? strings[0] : null;
            PrefixSelectGUI.open(player, group);
        } else {
            if (strings.length < 1) {
                PluginMessages.COMMAND_USAGE.CONSOLE.sendTo(sender);
            } else {
                Player player = Bukkit.getPlayer(strings[0]);
                if (player != null) {
                    String group = strings.length > 1 ? strings[1] : null;
                    PrefixSelectGUI.open(player, group);
                }
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        List<String> completions = new ArrayList<>();

        if (sender instanceof Player) {
            if (strings.length == 1) {
                // 补全 group 名称
                Set<String> groups = UserPrefixAPI.getPrefixManager().getGroups();
                String input = strings[0].toLowerCase();
                completions.addAll(groups.stream()
                        .filter(g -> g.toLowerCase().startsWith(input))
                        .collect(Collectors.toList()));
            }
        } else {
            // 控制台
            if (strings.length == 1) {
                // 补全在线玩家名
                String input = strings[0].toLowerCase();
                completions.addAll(Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(input))
                        .collect(Collectors.toList()));
            } else if (strings.length == 2) {
                // 补全 group 名称
                Set<String> groups = UserPrefixAPI.getPrefixManager().getGroups();
                String input = strings[1].toLowerCase();
                completions.addAll(groups.stream()
                        .filter(g -> g.toLowerCase().startsWith(input))
                        .collect(Collectors.toList()));
            }
        }

        return completions;
    }


}