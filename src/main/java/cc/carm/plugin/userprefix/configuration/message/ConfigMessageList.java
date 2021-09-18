package cc.carm.plugin.userprefix.configuration.message;

import cc.carm.plugin.userprefix.configuration.values.ConfigValueList;
import cc.carm.plugin.userprefix.manager.ConfigManager;
import cc.carm.plugin.userprefix.util.MessageUtil;
import org.bukkit.command.CommandSender;

public class ConfigMessageList extends ConfigValueList<String> {

    public ConfigMessageList(String configSection) {
        super(ConfigManager.getMessageConfig(), configSection, String.class);
    }

    public ConfigMessageList(String configSection, String[] defaultValue) {
        super(ConfigManager.getMessageConfig(), configSection, String.class, defaultValue);
    }

    public void send(CommandSender sender) {
        MessageUtil.send(sender, get());
    }

    public void sendWithPlaceholders(CommandSender sender) {
        MessageUtil.sendWithPlaceholders(sender, get());
    }

    public void sendWithPlaceholders(CommandSender sender, String[] params, Object[] values) {
        MessageUtil.sendWithPlaceholders(sender, get(), params, values);
    }
}
