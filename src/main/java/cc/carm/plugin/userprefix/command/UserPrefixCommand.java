package cc.carm.plugin.userprefix.command;

import cc.carm.plugin.userprefix.ui.PrefixSelectGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UserPrefixCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (sender instanceof Player) {
            PrefixSelectGUI.open((Player) sender);
        } else {
            if (strings.length != 1) {
                sender.sendMessage("输入 /prefix <ID> 为玩家打开前缀GUI。");
            } else {
                Player player = Bukkit.getPlayer(strings[0]);
                if (player != null) {
                    PrefixSelectGUI.open(player);
                }
            }
        }
        return true;
    }


}