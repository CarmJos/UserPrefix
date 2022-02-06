package cc.carm.plugin.userprefix;

import cc.carm.plugin.userprefix.command.UserPrefixAdminCommand;
import cc.carm.plugin.userprefix.command.UserPrefixCommand;
import cc.carm.plugin.userprefix.configuration.PluginConfig;
import cc.carm.plugin.userprefix.hooker.UpdateChecker;
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
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        showPluginName();
        log(getName() + " " + getDescription().getVersion() + " &7开始加载...");
        long startTime = System.currentTimeMillis();

        log("加载配置文件...");
        ConfigManager.initConfig();
        PrefixManager.init();

        log("注册指令...");
        registerCommand("UserPrefix", new UserPrefixCommand());
        registerCommand("UserPrefixAdmin", new UserPrefixAdminCommand());

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

        if (PluginConfig.METRICS.get()) {
            log("启用统计数据...");
            Metrics metrics = new Metrics(this, 13776);
            metrics.addCustomChart(new SingleLineChart("active_prefixes", () -> PrefixManager.getPrefixes().size()));
            metrics.addCustomChart(new SimplePie("custom_storage", () -> PluginConfig.CustomStorage.ENABLE.get() ? "ENABLE" : "DISABLE"));
            metrics.addCustomChart(new SimplePie("lp_version", () -> ServiceManager.getService().getPluginMetadata().getVersion()));
            metrics.addCustomChart(new SimplePie("papi_version", () -> {
                Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
                if (plugin == null) return "Not installed";
                else return plugin.getDescription().getVersion();
            }));
        }

        if (PluginConfig.CHECK_UPDATE.get()) {
            log("开始检查更新...");
            UpdateChecker.checkUpdate(getDescription().getVersion());
        } else {
            log("已禁用检查更新，跳过。");
        }

        log("加载完成 ，共耗时 " + (System.currentTimeMillis() - startTime) + " ms 。");

        showAD();

        if (Bukkit.getOnlinePlayers().size() > 0) {
            Bukkit.getOnlinePlayers().forEach(UserManager::initPlayer);  // 适配热重载
        }


    }

    @Override
    public void onDisable() {
        showPluginName();
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
        if (PluginConfig.DEBUG.get()) {
            log("[DEBUG] " + message);
        }
    }

    public static void error(String message) {
        log("&c[ERROR] &r" + message);
    }


    public static JavaPlugin getInstance() {
        return instance;
    }


    public static void registerCommand(String commandName,
                                       @NotNull CommandExecutor executor) {
        registerCommand(commandName, executor, null);
    }

    public static void registerCommand(String commandName,
                                       @NotNull CommandExecutor executor,
                                       @Nullable TabCompleter tabCompleter) {
        PluginCommand command = Bukkit.getPluginCommand(commandName);
        if (command == null) return;
        command.setExecutor(executor);
        if (tabCompleter != null) command.setTabCompleter(tabCompleter);
    }

    private void showPluginName() {
        log("&b _    _                  &f _____              __  _      ");
        log("&b| |  | |                 &f|  __ \\            / _|(_)    ");
        log("&b| |  | | ___   ___  _ __ &f| |__) |_ __  ___ | |_  _ __  __");
        log("&b| |  | |/ __| / _ \\| '__|&f|  ___/| '__|/ _ \\|  _|| |\\ \\/ /");
        log("&b| |__| |\\__ \\|  __/| |   &f| |    | |  |  __/| |  | | >  < ");
        log("&b \\____/ |___/ \\___||_|   &f|_|    |_|   \\___||_|  |_|/_/\\_\\");
        log("&8  ");
        log("&8> &f" + getDescription().getWebsite());
    }

    private void showAD() {
        log("&7感谢您使用 &3&lUserPrefix " + getDescription().getVersion() + "&7!");
        log("&7本插件由 &b&lYourCraft &7提供长期支持与维护。");
    }

}
