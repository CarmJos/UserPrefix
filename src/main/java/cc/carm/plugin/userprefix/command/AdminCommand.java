package cc.carm.plugin.userprefix.command;

import cc.carm.lib.easyplugin.command.CommandHandler;
import cc.carm.plugin.userprefix.command.admin.ListCommand;
import cc.carm.plugin.userprefix.command.admin.ReloadCommand;
import cc.carm.plugin.userprefix.command.admin.SetCommand;
import cc.carm.plugin.userprefix.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AdminCommand extends CommandHandler {


    public AdminCommand(@NotNull JavaPlugin plugin) {
        super(plugin);
        registerSubCommand(new ListCommand(this, "list", "l"));
        registerSubCommand(new SetCommand(this, "set"));
        registerSubCommand(new ReloadCommand(this, "reload"));
    }

    @Override
    public Void noArgs(CommandSender sender) {
        return help(sender);
    }

    @Override
    public Void noPermission(CommandSender sender) {
        PluginMessages.COMMAND_USAGE.NO_PERM.sendTo(sender);
        return null;
    }

    public static Void help(CommandSender sender) {
        PluginMessages.COMMAND_USAGE.ADMIN.sendTo(sender);
        return null;
    }

}
