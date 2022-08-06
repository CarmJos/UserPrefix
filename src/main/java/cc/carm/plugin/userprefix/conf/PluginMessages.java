package cc.carm.plugin.userprefix.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;

public class PluginMessages extends ConfigurationRoot {

    public static final class COMMAND_USAGE {

        public static ConfiguredMessageList<String> CONSOLE = ConfiguredMessageList.asStrings().defaults(
                "&f请输入 &b/prefix <玩家ID> &f为指定玩家打开前缀GUI。"
        ).build();

        public static ConfiguredMessageList<String> ADMIN = ConfiguredMessageList.asStrings().defaults(
                "&3&l用户前缀系统 &f帮助",
                "&8# &f/upa list",
                "&8- &7查看当前前缀列表。",
                "&8# &f/upa reload",
                "&8- &7重载前缀配置。"
        ).build();

    }


    public static ConfiguredMessageList<String> SELECTED = ConfiguredMessageList.asStrings()
            .defaults("&7您选择了 &f%(name) &7作为当前显示的前缀。")
            .params("name").build();

    public static ConfiguredMessageList<String> EXPIRED = ConfiguredMessageList.asStrings()
            .defaults(
                    "&7您先前使用的前缀 &f%(oldName) &7已到期。",
                    "&7现在已为您重新调整为 &f%(newName) &7。"
            ).params("oldName", "newName").build();

    public static ConfiguredMessageList<String> REMOVED = ConfiguredMessageList.asStrings()
            .defaults("&7您先前使用的前缀已被移除，现在已为您重新调整为 &f%(newName) &7。")
            .params("newName").build();

    public static final class RELOAD {

        public static ConfiguredMessageList<String> SUCCESS = ConfiguredMessageList.asStrings()
                .defaults("&a&l重载完成！&7耗时 &f%(time)ms&7，共加载了 &b%(count) &7个前缀。")
                .params("time", "count").build();

        public static ConfiguredMessageList<String> FAILED = ConfiguredMessageList.asStrings()
                .defaults("&c&l重载出错！&7错误提示为 &8“&r%(error)&8”。")
                .params("error").build();
    }


    @HeaderComment("管理员使用的 “/upa list” 指令的格式")
    public static final class LIST {

        public static ConfiguredMessageList<String> HEADER = ConfiguredMessageList.asStrings()
                .defaults("&3&l用户前缀系统 &f前缀列表").build();

        public static ConfiguredMessageList<String> VALUE = ConfiguredMessageList.asStrings().defaults(
                "&8#%(weight) &f%(identifier)",
                "&8- &7显示名 &r%(name) &7权限 &r%(permission)",
                "&8- &7内容示例&r %(content) %(sender_name)"
        ).params("weight", "identifier", "name", "permission", "content", "sender_name").build();

    }


}
