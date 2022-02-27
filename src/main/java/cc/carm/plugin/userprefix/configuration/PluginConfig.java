package cc.carm.plugin.userprefix.configuration;

import cc.carm.plugin.userprefix.configuration.message.ConfigMessageList;
import cc.carm.plugin.userprefix.configuration.values.ConfigSound;
import cc.carm.plugin.userprefix.configuration.values.ConfigValue;
import cc.carm.plugin.userprefix.util.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PluginConfig {

    public static ConfigValue<Boolean> DEBUG = new ConfigValue<>("debug", Boolean.class, false);

    public static ConfigValue<Boolean> METRICS = new ConfigValue<>("metrics", Boolean.class, true);

    public static final ConfigValue<Boolean> CHECK_UPDATE = new ConfigValue<>("check-update", Boolean.class, true);

    public static class CustomStorage {

        public static ConfigValue<Boolean> ENABLE = new ConfigValue<>("custom-storage.enable", Boolean.class, false);

        public static ConfigValue<String> PATH = new ConfigValue<>("custom-storage.path", String.class, "prefixes/");

    }

    public static class Functions {

        public static ConfigValue<Boolean> NAME_PREFIX = new ConfigValue<>("functions.on-name-prefix.enable", Boolean.class, true);
        public static ConfigValue<Boolean> NAME_ORDER_DESC = new ConfigValue<>("functions.on-name-prefix.order-desc", Boolean.class, true);

        public static ConfigValue<Boolean> AUTO_USE = new ConfigValue<>("functions.auto-prefix-use", Boolean.class, true);

        public static class Chat {

            public static ConfigValue<Boolean> ENABLE = new ConfigValue<>("functions.chat.enable", Boolean.class, false);
            public static ConfigValue<String> FORMAT = new ConfigValue<>("functions.chat.format", String.class, "<%1$s> %2$s");

        }

    }

    public static class GUI {

        public static ConfigValue<String> TITLE = new ConfigValue<>("GUI.title", String.class, "&f&l我的前缀 &8| 列表");

        public static class Items {

            public static ConfigValue<ItemStack> NEXT_PAGE = new ConfigValue<>("GUI.items.next-page", ItemStack.class,
                    new ItemStackFactory(Material.ARROW)
                            .setDisplayName("下一页")
                            .addLore("&7&o右键可前往最后一页哦")
                            .toItemStack()
            );
            public static ConfigValue<ItemStack> PREVIOUS_PAGE = new ConfigValue<>("GUI.items.previous-page", ItemStack.class,
                    new ItemStackFactory(Material.ARROW)
                            .setDisplayName("上一页")
                            .addLore("&7&o右键可前往第一页哦")
                            .toItemStack()
            );

        }

    }

    public static class Messages {

        public static ConfigMessageList SELECTED = new ConfigMessageList("selected");
        public static ConfigMessageList EXPIRED = new ConfigMessageList("expired");
        public static ConfigMessageList REMOVED = new ConfigMessageList("removed");

        public static ConfigMessageList RELOAD = new ConfigMessageList("reload");
        public static ConfigMessageList HELP = new ConfigMessageList("help");

        public static ConfigMessageList LIST_TITLE = new ConfigMessageList("list-title");
        public static ConfigMessageList LIST_VALUE = new ConfigMessageList("list-value");

    }

    public static class Sounds {

        public static ConfigSound GUI_OPEN = new ConfigSound("Sounds.openGUI");
        public static ConfigSound GUI_CLICK = new ConfigSound("Sounds.guiClick");
        public static ConfigSound PREFIX_CHANGE = new ConfigSound("Sounds.prefixChange");
        public static ConfigSound PREFIX_EXPIRED = new ConfigSound("Sounds.prefixExpired");

    }


}
