package cc.carm.plugin.userprefix.command;

import cc.carm.lib.easyplugin.command.CommandHandler;
import cc.carm.plugin.userprefix.command.sub.ListCommand;
import cc.carm.plugin.userprefix.command.sub.ReloadCommand;
import cc.carm.plugin.userprefix.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AdminCommand extends CommandHandler {


    public AdminCommand(@NotNull JavaPlugin plugin) {
        super(plugin);
        registerSubCommand(new ListCommand("list", "l"));
        registerSubCommand(new ReloadCommand("reload"));

    }

    @Override
    public void noArgs(CommandSender sender) {
        help(sender);
    }

    @Override
    public void noPermission(CommandSender sender) {
        PluginMessages.COMMAND_USAGE.NO_PERM.send(sender);
    }

    public static Void help(CommandSender sender) {
        PluginMessages.COMMAND_USAGE.ADMIN.send(sender);
        return null;
    }

}
