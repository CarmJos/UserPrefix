package cc.carm.plugin.userprefix.configuration;

import cc.carm.plugin.userprefix.configuration.values.ConfigSound;
import cc.carm.plugin.userprefix.configuration.values.ConfigValue;
import cc.carm.plugin.userprefix.configuration.values.ConfigValueList;

public class PrefixConfig {

    public static ConfigValue<Boolean> DEBUG = new ConfigValue<>("debug", Boolean.class, false);

    public static class Functions {

        public static ConfigValue<Boolean> NAME_PREFIX = new ConfigValue<>("functions.OnNamePrefix", Boolean.class, true);
        public static ConfigValue<Boolean> AUTO_USE = new ConfigValue<>("functions.autoUsePrefix", Boolean.class, true);

    }

    public static class GUI {

        public static ConfigValue<String> TITLE = new ConfigValue<>("GUI.title", String.class, "&f&l我的前缀 &8| 列表");

    }

    public static class Messages {

        public static ConfigValueList<String> SELECTED = new ConfigValueList<>("messages.selected", String.class);
        public static ConfigValueList<String> EXPIRED = new ConfigValueList<>("messages.expired", String.class);

        public static ConfigValueList<String> RELOAD = new ConfigValueList<>("messages.reload", String.class);
        public static ConfigValueList<String> HELP = new ConfigValueList<>("messages.help", String.class);

        public static ConfigValueList<String> LIST_TITLE = new ConfigValueList<>("messages.list-title", String.class);
        public static ConfigValueList<String> LIST_VALUE = new ConfigValueList<>("messages.list-value", String.class);
    }

    public static class Sounds {

        public static ConfigSound GUI_OPEN = new ConfigSound("Sounds.openGUI");
        public static ConfigSound GUI_CLICK = new ConfigSound("Sounds.guiClick");
        public static ConfigSound PREFIX_CHANGE = new ConfigSound("Sounds.prefixChange");
        public static ConfigSound PREFIX_EXPIRED = new ConfigSound("Sounds.prefixExpired");

    }


}
