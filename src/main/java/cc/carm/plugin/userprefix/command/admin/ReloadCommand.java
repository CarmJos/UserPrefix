package cc.carm.plugin.userprefix.command.admin;

import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.userprefix.UserPrefixAPI;
import cc.carm.plugin.userprefix.command.AdminCommand;
import cc.carm.plugin.userprefix.conf.PluginMessages;
import cc.carm.plugin.userprefix.ui.PrefixSelectGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends SubCommand<AdminCommand> {

    public ReloadCommand(@NotNull AdminCommand parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        long s1 = System.currentTimeMillis();
        PrefixSelectGUI.closeAll(); // 关掉所有正在显示的前缀列表
        try {
            UserPrefixAPI.getConfigManager().reload(); // 重载配置文件
            int num = UserPrefixAPI.getPrefixManager().loadPrefixes(); //加载重载后了的前缀配置
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                UserPrefixAPI.getUserManager().checkPrefix(onlinePlayer, false);
                /*
                 * 这里关掉loadOthers(为其他玩家更新)了。
                 * 因为每个玩家更新的时候会为其他人更新自己，
                 * 全部走完一遍后，所有玩家都会加载最新的前缀内容。
                 */
                UserPrefixAPI.getUserManager().updatePrefixView(onlinePlayer, false);
            }
            PluginMessages.RELOAD.SUCCESS.sendTo(sender, System.currentTimeMillis() - s1, num);
        } catch (Exception e) {
            PluginMessages.RELOAD.FAILED.sendTo(sender, e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
