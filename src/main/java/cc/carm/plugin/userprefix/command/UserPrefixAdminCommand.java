package cc.carm.plugin.userprefix.command;

import cc.carm.plugin.userprefix.configuration.PluginConfig;
import cc.carm.plugin.userprefix.manager.ConfigManager;
import cc.carm.plugin.userprefix.manager.PrefixManager;
import cc.carm.plugin.userprefix.manager.UserManager;
import cc.carm.plugin.userprefix.model.ConfiguredPrefix;
import cc.carm.plugin.userprefix.ui.PrefixSelectGUI;
import cc.carm.plugin.userprefix.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UserPrefixAdminCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            String aim = args[0];
            if (aim.equalsIgnoreCase("list")) {
                MessageUtil.sendWithPlaceholders(sender, PluginConfig.Messages.LIST_TITLE.get());
                for (ConfiguredPrefix value : PrefixManager.getPrefixes().values()) {
                    MessageUtil.sendWithPlaceholders(
                            sender, PluginConfig.Messages.LIST_VALUE.get(),
                            new String[]{
                                    "%(weight)", "%(identifier)",
                                    "%(name)", "%(permission)",
                                    "%(content)", "%(sender_name)"
                            },
                            new Object[]{
                                    value.getWeight(), value.getIdentifier(),
                                    value.getName(), value.getPermission(),
                                    value.getContent(), sender.getName()
                            }
                    );
                }
                return true;
            } else if (aim.equalsIgnoreCase("reload")) {
                long s1 = System.currentTimeMillis();
                PrefixSelectGUI.closeAll(); // 关掉所有正在显示的前缀列表
                ConfigManager.reload(); // 重载配置文件
                PrefixManager.loadPrefixes(); //加载重载后了的前缀配置
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    UserManager.checkPrefix(onlinePlayer, false);
                    /*
                     * 这里关掉loadOthers(为其他玩家更新)了。
                     * 因为每个玩家更新的时候会为其他人更新自己，
                     * 全部走完一遍后，所有玩家都会加载最新的前缀内容。
                     */
                    UserManager.updatePrefixView(onlinePlayer, false);
                }
                MessageUtil.sendWithPlaceholders(
                        sender, PluginConfig.Messages.RELOAD.get(),
                        new String[]{"%(time)"}, new Object[]{(System.currentTimeMillis() - s1)}
                );
                return true;
            }
            return help(sender);
        }
        return help(sender);
    }

    public static boolean help(CommandSender sender) {
        MessageUtil.send(sender, PluginConfig.Messages.HELP.get());
        return true;
    }


}
