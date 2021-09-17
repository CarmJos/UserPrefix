package cc.carm.plugin.userprefix.command;

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
                MessageUtil.send(sender, "&3&l用户前缀系统 &f前缀列表");
                for (ConfiguredPrefix value : PrefixManager.getPrefixes().values()) {
                    MessageUtil.send(sender, "&8#" + value.getWeight() + " &f" + value.getIdentifier());
                    MessageUtil.send(sender, "&8- &7显示名 &r" + value.getName() + (value.isPublic() ? "" : " &7权限&r " + value.getPermission()));
                    MessageUtil.send(sender, "&8- &7内容示例&r " + value.getContent() + sender.getName());
                }
                return true;
            } else if (aim.equalsIgnoreCase("reload")) {
                long s1 = System.currentTimeMillis();
                PrefixSelectGUI.closeAll(); // 关掉所有正在显示的前缀列表
                ConfigManager.reloadConfig(); // 重载配置文件
                PrefixManager.loadConfiguredPrefixes(); //加载重载后了的配置文件
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    UserManager.checkPrefix(onlinePlayer, false);
                    /*
                     * 这里关掉loadOthers(为其他玩家更新)了。
                     * 因为每个玩家更新的时候会为其他人更新自己，
                     * 全部走完一遍后，所有玩家都会加载最新的前缀内容。
                     */
                    UserManager.updatePrefixView(onlinePlayer, false);
                }
                MessageUtil.send(sender, "&a&l重载完成！&7共耗时 &f" + (System.currentTimeMillis() - s1) + " ms&7。");
                return true;
            }
            return help(sender);
        }
        return help(sender);
    }

    public static boolean help(CommandSender sender) {
        MessageUtil.send(sender, "&3&l用户前缀系统 &f帮助");
        MessageUtil.send(sender, "&8#&f list");
        MessageUtil.send(sender, "&8- &7查看当前前缀列表。");
        MessageUtil.send(sender, "&8#&f reload");
        MessageUtil.send(sender, "&8- &7重载前缀配置。");
        return true;
    }


}
