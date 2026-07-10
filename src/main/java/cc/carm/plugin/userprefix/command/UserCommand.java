package cc.carm.plugin.userprefix.command;

import cc.carm.plugin.userprefix.conf.PluginMessages;
import cc.carm.plugin.userprefix.ui.PrefixSelectGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UserCommand implements CommandExecutor {


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


}