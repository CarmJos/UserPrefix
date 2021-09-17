package cc.carm.plugin.userprefix.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class MessageUtil {

    public static boolean hasPlaceholderAPI() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }

    public static void send(Player player, List<String> messages) {
        for (String s : messages) {
            player.sendMessage(ColorParser.parseColor(s));
        }
    }

    public static void send(Player player, String... messages) {
        send(player, Arrays.asList(messages));
    }

    public static void sendWithPlaceholders(Player player, String... messages) {
        sendWithPlaceholders(player, Arrays.asList(messages));
    }

    public static void sendWithPlaceholders(Player player, List<String> messages) {
        if (hasPlaceholderAPI()) {
            send(player, PlaceholderAPI.setPlaceholders(player, messages));
        } else {
            send(player, messages);
        }
    }

    public static void sendWithPlaceholders(Player player, List<String> messages, String param, Object value) {
        sendWithPlaceholders(player, messages, new String[]{param}, new Object[]{value});
    }

    public static void sendWithPlaceholders(Player player, List<String> messages, String[] params, Object[] values) {
        sendWithPlaceholders(player, setCustomParams(messages, params, values));
    }

    public static List<String> setCustomParams(List<String> messages, String param, Object value) {
        return setCustomParams(messages, new String[]{param}, new Object[]{value});
    }

    public static List<String> setCustomParams(List<String> messages, String[] params, Object[] values) {
        if (params.length != values.length) return messages;
        HashMap<String, Object> paramsMap = new HashMap<>();
        for (int i = 0; i < params.length; i++) {
            paramsMap.put(params[i], values[i]);
        }
        return setCustomParams(messages, paramsMap);
    }


    public static List<String> setCustomParams(List<String> messages, HashMap<String, Object> params) {
        List<String> list = new ArrayList<>();
        for (String message : messages) {
            String afterMessage = message;
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                afterMessage = afterMessage.replace(entry.getKey(), entry.getValue().toString());
            }
            list.add(afterMessage);
        }
        return list;
    }


}
