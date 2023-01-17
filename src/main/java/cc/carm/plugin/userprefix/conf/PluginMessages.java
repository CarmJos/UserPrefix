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
                "&8# &f/upa set &b<玩家ID> &b<前缀ID>",
                "&8- &7为玩家设定指定前缀。",
                "&8- &7&o注意：玩家必须拥有指定前缀的权限。",
                "&8# &f/upa reload",
                "&8- &7重载前缀配置。"
        ).build();

        public static ConfiguredMessageList<String> NO_PERM = ConfiguredMessageList.asStrings()
                .defaults("&c&l抱歉！&f但您没有权限使用该指令。")
                .build();

    }

    public static ConfiguredMessageList<String> NOT_ONLINE = ConfiguredMessageList.asStrings()
            .defaults("&7玩家 &b%(player) &7并不在线。")
            .params("player").build();

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

    public static final class SET {

        public static ConfiguredMessageList<String> SUCCESS = ConfiguredMessageList.asStrings()
                .defaults("&a&l设置成功！&7成功设定玩家 &b%(player) &f的前缀为 &r%(prefix) &f。")
                .params("player", "prefix").build();

        public static ConfiguredMessageList<String> PREFIX_NOT_FOUND = ConfiguredMessageList.asStrings()
                .defaults("&c&l无法设置！&7不存在ID为 &b%(prefix) &7的前缀。")
                .params("prefix").build();

        public static ConfiguredMessageList<String> NO_PERM = ConfiguredMessageList.asStrings()
                .defaults("&c&l无法设置！&7玩家 &b%(player) &7并没有前缀 &r%(prefix) &7的前缀。")
                .params("player", "prefix")
                .build();

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
