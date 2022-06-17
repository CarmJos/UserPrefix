package cc.carm.plugin.userprefix.listener;

import cc.carm.lib.easyplugin.utils.MessageUtils;
import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.configuration.PluginConfig;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!PluginConfig.FUNCTIONS.CHAT.ENABLE.getNotNull()) return;

        String format = PluginConfig.FUNCTIONS.CHAT.FORMAT.get();
        if (format == null || format.length() < 1) return;

        try {
            event.setFormat(MessageUtils.setPlaceholders(event.getPlayer(), format));
        } catch (Exception exception) {
            Main.serve("请检查配置文件中聊天相关是否配置正确。");
            Main.serve("Please check the chat configuration.");
            exception.printStackTrace();
        }

    }


}
