package cc.carm.plugin.userprefix.command.admin;

import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.userprefix.UserPrefixAPI;
import cc.carm.plugin.userprefix.command.AdminCommand;
import cc.carm.plugin.userprefix.conf.PluginMessages;
import cc.carm.plugin.userprefix.conf.prefix.PrefixConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SetCommand extends SubCommand<AdminCommand> {

    public SetCommand(@NotNull AdminCommand parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length < 2) return getParent().noArgs(sender);

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            PluginMessages.NOT_ONLINE.sendTo(sender, args[0]);
            return null;
        }


        PrefixConfig prefixConfig;
        String prefixInput = args[1];
        if (prefixInput.equalsIgnoreCase("default")) {
            prefixConfig = UserPrefixAPI.getDefaultPrefix();
        } else {
            prefixConfig = UserPrefixAPI.getPrefixManager().getPrefix(prefixInput);
        }

        if (prefixConfig == null) {
            PluginMessages.SET.PREFIX_NOT_FOUND.sendTo(sender, prefixInput);
            return null;
        }

        if (!prefixConfig.checkPermission(target)) {
            PluginMessages.SET.NO_PERM.sendTo(sender, target.getName(), prefixConfig.getName());
            return null;
        }

        UserPrefixAPI.getUserManager().setPrefix(target, prefixConfig, true);
        PluginMessages.SET.SUCCESS.sendTo(sender, target.getName(), prefixConfig.getName());
        return null;
    }

}
