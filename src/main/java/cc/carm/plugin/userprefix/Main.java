package cc.carm.plugin.userprefix;

import cc.carm.plugin.userprefix.command.UserPrefixAdminCommand;
import cc.carm.plugin.userprefix.command.UserPrefixCommand;
import cc.carm.plugin.userprefix.configuration.PrefixConfig;
import cc.carm.plugin.userprefix.hooker.UserPrefixExpansion;
import cc.carm.plugin.userprefix.listener.UserListener;
import cc.carm.plugin.userprefix.listener.processor.UserNodeUpdateProcessor;
import cc.carm.plugin.userprefix.manager.ConfigManager;
import cc.carm.plugin.userprefix.manager.PrefixManager;
import cc.carm.plugin.userprefix.manager.ServiceManager;
import cc.carm.plugin.userprefix.manager.UserManager;
import cc.carm.plugin.userprefix.util.ColorParser;
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
        ServiceManager.getService().getEventBus().subscribe(this, UserDataRecalculateEvent.class, UserNodeUpdateProcessor::process);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            log("注册变量...");
            new UserPrefixExpansion(getInstance()).register();
        } else {
            log("未安装 PlaceholderAPI 放弃注册变量...");
        }


        log("加载完成 ，共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");

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
        Bukkit.getConsoleSender().sendMessage(ColorParser.parseColor("[" + getInstance().getName() + "] " + message));
    }

    public static void debug(String message) {
        if (PrefixConfig.DEBUG.get()) {
            log("[DEBUG] " + ColorParser.parseColor(message));
        }
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

}
