package cc.carm.plugin.userprefix.manager;

import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.configuration.file.FileConfig;

import java.io.File;

public class ConfigManager {

    private static FileConfig config;
    private static FileConfig messageConfig;


    public static void initConfig() {
        File configFile = new File(Main.getInstance().getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            //没找到配置文件，可能是第一次加载此插件
            //把一些英文版的东西复制出来，方便英文用户使用。
            Main.getInstance().saveResource("prefixes/example-prefix.yml", false);
            Main.getInstance().saveResource("en_US/config.yml", false);
            Main.getInstance().saveResource("en_US/messages.yml", false);
            Main.getInstance().saveResource("en_US/example-prefix.yml", false);
        }

        ConfigManager.config = new FileConfig(Main.getInstance(), "config.yml");
        ConfigManager.messageConfig = new FileConfig(Main.getInstance(), "messages.yml");
    }

    public static FileConfig getPluginConfig() {
        return config;
    }

    public static FileConfig getMessageConfig() {
        return messageConfig;
    }

    public static void reload() {
        getPluginConfig().reload();
        getMessageConfig().reload();
    }

    public static void saveConfig() {
        getPluginConfig().save();
        getMessageConfig().save();
    }


}
