package cc.carm.plugin.userprefix.command;

import cc.carm.plugin.userprefix.UserPrefix;
import cc.carm.plugin.userprefix.configuration.PluginMessages;
import cc.carm.plugin.userprefix.configuration.prefix.PrefixConfig;
import cc.carm.plugin.userprefix.ui.PrefixSelectGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            String aim = args[0];
            if (aim.equalsIgnoreCase("list")) {
                PluginMessages.LIST.HEADER.send(sender);
                for (PrefixConfig value : UserPrefix.getPrefixManager().getPrefixes().values()) {
                    PluginMessages.LIST.VALUE.send(sender,
                            value.getWeight(), value.getIdentifier(),
                            value.getName(), value.getPermission(),
                            value.getContent(), sender.getName()
                    );
                }
                return true;
            } else if (aim.equalsIgnoreCase("reload")) {
                long s1 = System.currentTimeMillis();
                PrefixSelectGUI.closeAll(); // 关掉所有正在显示的前缀列表
                try {
                    UserPrefix.getConfigManager().reload(); // 重载配置文件
                    int num = UserPrefix.getPrefixManager().loadPrefixes(); //加载重载后了的前缀配置
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        UserPrefix.getUserManager().checkPrefix(onlinePlayer, false);
                        /*
                         * 这里关掉loadOthers(为其他玩家更新)了。
                         * 因为每个玩家更新的时候会为其他人更新自己，
                         * 全部走完一遍后，所有玩家都会加载最新的前缀内容。
                         */
                        UserPrefix.getUserManager().updatePrefixView(onlinePlayer, false);
                    }
                    PluginMessages.RELOAD.SUCCESS.send(sender, System.currentTimeMillis() - s1, num);
                } catch (Exception e) {
                    PluginMessages.RELOAD.FAILED.send(sender, e.getMessage());
                    e.printStackTrace();
                }
                return true;
            }
            return help(sender);
        }
        return help(sender);
    }

    public static boolean help(CommandSender sender) {
        PluginMessages.COMMAND_USAGE.ADMIN.send(sender);
        return true;
    }


}
