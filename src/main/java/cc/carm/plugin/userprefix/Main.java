package cc.carm.plugin.userprefix;

import cc.carm.plugin.userprefix.command.UserPrefixAdminCommand;
import cc.carm.plugin.userprefix.command.UserPrefixCommand;
import cc.carm.plugin.userprefix.configuration.PrefixConfig;
import cc.carm.plugin.userprefix.hooker.UserPrefixExpansion;
import cc.carm.plugin.userprefix.listener.ChatListener;
import cc.carm.plugin.userprefix.listener.UserListener;
import cc.carm.plugin.userprefix.listener.processor.UserNodeUpdateProcessor;
import cc.carm.plugin.userprefix.manager.ConfigManager;
import cc.carm.plugin.userprefix.manager.PrefixManager;
import cc.carm.plugin.userprefix.manager.ServiceManager;
import cc.carm.plugin.userprefix.manager.UserManager;
import cc.carm.plugin.userprefix.util.ColorParser;
import cc.carm.plugin.userprefix.util.MessageUtil;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        log(getName() + " " + getDescription().getVersion() + " &7开始加载...");
        long startTime = System.currentTimeMillis();

        log("加载配置文件...");
        ConfigManager.initConfig();
        PrefixManager.init();

        log("注册指令...");
        Bukkit.getPluginCommand("UserPrefix").setExecutor(new UserPrefixCommand());
        Bukkit.getPluginCommand("UserPrefixAdmin").setExecutor(new UserPrefixAdminCommand());

        log("注册监听器...");
        regListener(new UserListener());
        regListener(new ChatListener());
        ServiceManager.getService().getEventBus().subscribe(this, UserDataRecalculateEvent.class, UserNodeUpdateProcessor::process);

        if (MessageUtil.hasPlaceholderAPI()) {
            log("注册变量...");
            new UserPrefixExpansion(getInstance()).register();
        } else {
            log("未安装 PlaceholderAPI 不进行变量注册...");
            log("若您想使用变量进行前缀的显示，请安装PlaceholderAPI！");
        }


        log("加载完成 ，共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");

        showAD();

        if (Bukkit.getOnlinePlayers().size() > 0) {
            Bukkit.getOnlinePlayers().forEach(UserManager::initPlayer);  // 适配热重载
        }

    }

    @Override
    public void onDisable() {
        log(getName() + " " + getDescription().getVersion() + " 开始卸载...");
        long startTime = System.currentTimeMillis();

        log("卸载监听器...");
        Bukkit.getServicesManager().unregisterAll(this);

        log("卸载完成 ，共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");

        showAD();

    }

    /**
     * 注册监听器
     *
     * @param listener 监听器
     */
    public static void regListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, getInstance());
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(ColorParser.parse("[" + getInstance().getName() + "] " + message));
    }

    public static void debug(String message) {
        if (PrefixConfig.DEBUG.get()) {
            log("[DEBUG] " + message);
        }
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

    private void showAD() {
        log("&7感谢您使用 &3&lUserPrefix " + getDescription().getVersion() + "&7!");
        log("&7本插件由 &b&lYourCraft &7提供长期支持与维护。");
    }

}
