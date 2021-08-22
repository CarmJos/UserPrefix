package cc.carm.plugin.userprefix.manager;

import cc.carm.plugin.userprefix.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private static FileConfiguration config;

    public static void initConfig() {
        Main.getInstance().saveDefaultConfig();
        Main.getInstance().reloadConfig();

        config = Main.getInstance().getConfig();
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static void reloadConfig() {
        Main.getInstance().reloadConfig();
        config = Main.getInstance().getConfig();
    }

    public static void saveConfig() {
        Main.getInstance().saveConfig();
    }


}
