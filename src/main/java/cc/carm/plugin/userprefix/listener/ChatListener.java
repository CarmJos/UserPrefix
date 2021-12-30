package cc.carm.plugin.userprefix.listener;

import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.configuration.PrefixConfig;
import cc.carm.plugin.userprefix.util.MessageUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		if (!PrefixConfig.Functions.Chat.ENABLE.get()) return;
		String format = PrefixConfig.Functions.Chat.FORMAT.get();
		if (format == null || format.length() < 1) return;

		if (!MessageUtil.hasPlaceholderAPI()) return;

		try {
			event.setFormat(PlaceholderAPI.setPlaceholders(event.getPlayer(), format));
		} catch (Exception exception) {
			Main.error("请检查配置文件中聊天相关是否配置正确。");
			Main.error("Please check the chat configuration.");
			exception.printStackTrace();
		}

	}


}
