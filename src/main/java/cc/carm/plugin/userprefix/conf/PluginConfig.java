package cc.carm.plugin.userprefix.conf;

import cc.carm.lib.configuration.Configuration;
import cc.carm.lib.configuration.annotation.ConfigPath;
import cc.carm.lib.configuration.annotation.HeaderComments;
import cc.carm.lib.configuration.value.standard.ConfiguredList;
import cc.carm.lib.configuration.value.standard.ConfiguredValue;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredSound;
import cc.carm.lib.mineconfiguration.bukkit.value.item.ConfiguredItem;
import cc.carm.plugin.userprefix.conf.gui.GUIItems;
import cc.carm.plugin.userprefix.folia.MajorUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;

public class PluginConfig implements Configuration {

    @HeaderComments({"开发者查错模式"})
    public static final ConfiguredValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

    @HeaderComments({
            "统计数据设定",
            " 该选项用于帮助开发者统计插件版本与使用情况，且绝不会影响性能与使用体验。",
            " 当然，您也可以选择在这里关闭，或在plugins/bStats下的配置文件中关闭。"
    })
    public static final ConfiguredValue<Boolean> METRICS = ConfiguredValue.of(Boolean.class, true);

    @HeaderComments({
            "检查更新设定",
            "该选项用于插件判断是否要检查更新，若您不希望插件检查更新并提示您，可以选择关闭。",
            "检查更新为异步操作，绝不会影响性能与使用体验。"
    })
    public static final ConfiguredValue<Boolean> CHECK_UPDATE = ConfiguredValue.of(Boolean.class, true);

    @HeaderComments({"自定义存储位置设定", "可以规定到远程文件夹中去加载前缀配置"})
    public static final class CUSTOM_STORAGE implements Configuration {

        @HeaderComments({"是否启用自定义存储位置"})
        public static final ConfiguredValue<Boolean> ENABLE = ConfiguredValue.of(Boolean.class, false);

        @HeaderComments({
                "目标存储路径，必须指向一个文件夹。",
                "默认存储位置为 “插件文件夹”/prefixes",
                "支持绝对文件路径，如 \"/etc/minecraft/configurations/prefixes/\""
        })
        public static final ConfiguredValue<String> PATH = ConfiguredValue.of(String.class, "prefixes/");

    }

    @HeaderComments("功能设定")
    public static class FUNCTIONS implements Configuration {

        @ConfigPath("on-name-prefix")
        @HeaderComments({"头顶与TabList前缀功能，该方法用到了玩家名计分板接口，如有冲突请关掉哦~"})
        public static final class NAME_PREFIX implements Configuration {

            @HeaderComments("是否开启本功能")
            public static final ConfiguredValue<Boolean> ENABLE = ConfiguredValue.of(Boolean.class, true);

            @HeaderComments("是否按降序排列，即权重越高的前缀显示在越上面；若为false则按升序排列。")
            public static final ConfiguredValue<Boolean> ORDER_DESC = ConfiguredValue.of(Boolean.class, true);

        }

        @ConfigPath("auto-prefix-use")
        @HeaderComments("自动使用前缀，即当玩家没有自己选择一个前缀的时候，会自动使用所拥有的的前缀中权重最高的那一个")
        public static final ConfiguredValue<Boolean> AUTO_USE = ConfiguredValue.of(Boolean.class, true);


        @HeaderComments({
                "聊天功能设定",
                "- 我不推荐使用本插件的聊天功能，而是建议使用其他的聊天插件。",
                "- 本插件仅仅提供了**最基本**的格式变量支持，不包含其他任何功能。",
                "- 注意聊天格式需要遵守Bukkit原格式，即不得缺失 “%1$s” 和 “%2$s” 。",
                "- 本插件的聊天功能不影响其他插件对聊天事件的操作。"
        })
        public static final class CHAT implements Configuration {

            @HeaderComments("是否开启本功能")
            public static final ConfiguredValue<Boolean> ENABLE = ConfiguredValue.of(Boolean.class, false);
            @HeaderComments({
                    "聊天的格式，注意 “%1$s” 和 “%2$s” 不可缺少。",
                    "- %1$s -> 玩家名", "- %2$s -> 聊天内容"
            })
            public static final ConfiguredValue<String> FORMAT = ConfiguredValue.of(String.class, "<%1$s> %2$s");

        }

    }

    @HeaderComments({"前缀GUI界面设定"})
    public static class GUI implements Configuration {

        @HeaderComments("GUI的标题")
        public static final ConfiguredValue<String> TITLE = ConfiguredValue.of(String.class, "&f&l我的前缀 &8| 列表");

        @HeaderComments("GUI中的基本按钮物品")
        public static final class BOTTOMS implements Configuration {

            @HeaderComments("前往下一页的物品 (只有存在下一页时才会显示)")
            public static final ConfiguredItem NEXT_PAGE = ConfiguredItem.create()
                    .defaultType(Material.ARROW)
                    .defaultName("下一页")
                    .defaultLore("&7&o右键可前往最后一页哦")
                    .build();

            @ConfigPath("previous-page")
            @HeaderComments({"前往上一页时的物品 (只有当前页不是第一页时才会显示)"})
            public static final ConfiguredItem PREV_PAGE = ConfiguredItem.create()
                    .defaultType(Material.ARROW)
                    .defaultName("上一页")
                    .defaultLore("&7&o右键可前往第一页哦")
                    .build();

        }

        @HeaderComments("GUI中的其他按钮物品 (若与现有物品位置冲突，将被覆盖）")
        public static final ConfiguredValue<GUIItems> ITEMS = ConfiguredValue
                .builderOf(GUIItems.class).fromSection()
                .defaults(GUIItems::defaults)
                .serialize(GUIItems::serialize)
                .parse(GUIItems::parse)
                .build();

    }

    @HeaderComments({"相关的声音，留空则不播放声音", "格式为 【声音名:音量:音调】 或 【声音名:音量】 或 【声音名】"})
    public static final class SOUNDS implements Configuration {

        public static final ConfiguredSound GUI_OPEN = ConfiguredSound.of("BLOCK_NOTE_BLOCK_PLING", 0.5F, 0.8F);
        public static final ConfiguredSound GUI_CLICK = ConfiguredSound.of("UI_BUTTON_CLICK");
        public static final ConfiguredSound PREFIX_CHANGE = ConfiguredSound.of("ENTITY_VILLAGER_YES");
        public static final ConfiguredSound PREFIX_EXPIRED = ConfiguredSound.of("ENTITY_VILLAGER_NO");

    }

    @HeaderComments({"默认前缀配置"})
    public static final class DEFAULT_PREFIX implements Configuration {

        @HeaderComments("默认前缀的显示名称，用于在消息提示中显示。")
        public static final ConfiguredValue<String> NAME = ConfiguredValue.of(String.class, "默认前缀");

        @HeaderComments({"默认前缀的描述信息。"})
        public static final ConfiguredList<String> DESCRIPTION = ConfiguredList.builderOf(String.class).fromString()
                .defaults("&7这是一个默认前缀", "&7您可以在前缀列表中选择其他前缀")
                .build();

        @HeaderComments({"默认前缀的权重，默认为0。"})
        public static final ConfiguredValue<Integer> WEIGHT = ConfiguredValue.of(Integer.class, 0);

        @HeaderComments({"默认前缀的内容，即用于显示的实际前缀"})
        public static final ConfiguredValue<String> CONTENT = ConfiguredValue.of(String.class, "&r");

        @HeaderComments({"选择默认前缀时执行的操作"})
        public static final ConfiguredList<String> ACTIONS = ConfiguredList.builderOf(String.class).fromString()
                .defaults("[CONSOLE] " + "say %player_name% 选择了默认前缀")
                .build();

        @HeaderComments({"默认前缀的显示物品"})
        public static final class ITEM implements Configuration {

            @HeaderComments({"当未选择默认前缀时显示的物品"})
            public static final ConfiguredItem NOT_USING = ConfiguredItem.create()
                    .defaultType(Material.NAME_TAG)
                    .defaultName("&f默认玩家前缀 &f(点击切换)")
                    .defaultLore("","{&f&o  }#description#{1,1}", "&a➥ 点击切换到该前缀")
                    .build();

            @HeaderComments({"当选择了默认前缀时显示的物品"})
            public static final ConfiguredItem USING = ConfiguredItem.create()
                    .defaultType(Material.NAME_TAG)
                    .defaultEnchant(MajorUtil.getEnchantProtection(), 1) // 附魔改过名
                    .defaultFlags(ItemFlag.HIDE_ENCHANTS)
                    .defaultName("&f默认玩家前缀")
                    .defaultLore("","{&f&o  }#description#{1,1}", "&a✔ 您正在使用该前缀")
                    .build();

        }


    }


}
