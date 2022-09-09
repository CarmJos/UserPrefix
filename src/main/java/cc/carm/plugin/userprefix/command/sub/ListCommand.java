package cc.carm.plugin.userprefix.command.sub;

import cc.carm.lib.easyplugin.command.SubCommand;
import cc.carm.plugin.userprefix.UserPrefixAPI;
import cc.carm.plugin.userprefix.command.AdminCommand;
import cc.carm.plugin.userprefix.conf.PluginMessages;
import cc.carm.plugin.userprefix.conf.prefix.PrefixConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ListCommand extends SubCommand<AdminCommand> {

    public ListCommand(@NotNull AdminCommand parent, String name, String... aliases) {
        super(parent, name, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) {
        PluginMessages.LIST.HEADER.send(sender);
        for (PrefixConfig value : UserPrefixAPI.getPrefixManager().getPrefixes().values()) {
            PluginMessages.LIST.VALUE.send(sender,
                    value.getWeight(), value.getIdentifier(),
                    value.getName(), value.getPermission(),
                    value.getContent(), sender.getName()
            );
        }
        return null;
    }


}
