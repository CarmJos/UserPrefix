package cc.carm.plugin.userprefix;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.gui.GUI;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.lib.easyplugin.updatechecker.GHUpdateChecker;
import cc.carm.lib.easyplugin.utils.MessageUtils;
import cc.carm.plugin.userprefix.command.AdminCommand;
import cc.carm.plugin.userprefix.command.UserCommand;
import cc.carm.plugin.userprefix.conf.PluginConfig;
import cc.carm.plugin.userprefix.hooker.UserPrefixExpansion;
import cc.carm.plugin.userprefix.listener.ChatListener;
import cc.carm.plugin.userprefix.listener.UserListener;
import cc.carm.plugin.userprefix.listener.processor.UserNodeUpdateProcessor;
import cc.carm.plugin.userprefix.manager.ConfigManager;
import cc.carm.plugin.userprefix.manager.PrefixManager;
import cc.carm.plugin.userprefix.manager.ServiceManager;
import cc.carm.plugin.userprefix.manager.UserManager;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Main extends EasyPlugin {

    private static Main instance;

    protected ConfigManager configManager;
    protected PrefixManager prefixManager;
    protected UserManager userManager;

    @Override
    protected boolean initialize() {
        instance = this;

        log("加载插件配置...");
        this.configManager = new ConfigManager(getDataFolder());
        this.prefixManager = new PrefixManager();

        log("加载前缀配置...");
        int loaded = prefixManager.loadPrefixes();
        log("   成功加载了 " + loaded + " 个前缀配置。");

        log("加载用户管理器...");
        this.userManager = new UserManager();

        log("注册指令...");
        registerCommand("UserPrefix", new UserCommand());
        registerCommand("UserPrefixAdmin", new AdminCommand(this));

        log("注册监听器...");
        registerListener(new UserListener());
        registerListener(new ChatListener());
        ServiceManager.getService().getEventBus().subscribe(
                this, UserDataRecalculateEvent.class,
                UserNodeUpdateProcessor::process
        );

        log("初始化GUI管理器...");
        GUI.initialize(this);
        AutoPagedGUI.defaultNextPage = (PluginConfig.GUI.ITEMS.NEXT_PAGE::getItem);
        AutoPagedGUI.defaultPreviousPage = (PluginConfig.GUI.ITEMS.PREV_PAGE::getItem);

        if (MessageUtils.hasPlaceholderAPI()) {
            log("注册变量...");
            new UserPrefixExpansion(getInstance()).register();
        } else {
            log("未安装 PlaceholderAPI ，跳过变量注册...");
            log("若您想使用变量进行前缀的显示，请安装PlaceholderAPI！");
        }

        if (PluginConfig.METRICS.getNotNull()) {
            log("启用统计数据...");
            Metrics metrics = new Metrics(this, 13776);
            metrics.addCustomChart(new SingleLineChart("active_prefixes", () -> prefixManager.getPrefixes().size()));
            metrics.addCustomChart(new SimplePie("custom_storage", () -> PluginConfig.CUSTOM_STORAGE.ENABLE.getNotNull() ? "ENABLE" : "DISABLE"));
            metrics.addCustomChart(new SimplePie("lp_version", () -> ServiceManager.getService().getPluginMetadata().getVersion()));
            metrics.addCustomChart(new SimplePie("papi_version", () -> {
                Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
                if (plugin == null) return "Not installed";
                else return plugin.getDescription().getVersion();
            }));
        }

        if (PluginConfig.CHECK_UPDATE.getNotNull()) {
            log("开始检查更新...");
            getScheduler().runAsync(GHUpdateChecker.runner(this));
        } else {
            log("已禁用检查更新，跳过。");
        }

        Bukkit.getOnlinePlayers().forEach(userManager::initPlayer);  // 适配热重载

        log("&7感谢您使用 &3&lUserPrefix " + getDescription().getVersion() + "&7!");
        log("&7本插件由 &b&lYourCraft &7提供长期支持与维护。");
        return true;
    }

    @Override
    public boolean isDebugging() {
        return PluginConfig.DEBUG.getNotNull();
    }

    public static void info(String... message) {
        getInstance().log(message);
    }

    public static void debugging(String... message) {
        getInstance().debug(message);
    }

    public static void serve(String... message) {
        getInstance().error(message);
    }

    public static Main getInstance() {
        return instance;
    }
}
