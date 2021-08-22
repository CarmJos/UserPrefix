package cc.carm.plugin.userprefix.command;

import cc.carm.plugin.userprefix.ui.PrefixSelectGUI;
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
        }
        return true;
    }


}