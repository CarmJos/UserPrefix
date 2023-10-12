package cc.carm.plugin.userprefix.manager;

import cc.carm.lib.configuration.core.source.ConfigurationProvider;
import cc.carm.lib.easyplugin.utils.JarResourceUtils;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import cc.carm.plugin.userprefix.conf.PluginConfig;
import cc.carm.plugin.userprefix.conf.PluginMessages;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final ConfigurationProvider<?> configProvider;
    private final ConfigurationProvider<?> messageProvider;

    public ConfigManager(File dataFolder) {
        firstInitialize(dataFolder);
        this.configProvider = MineConfiguration.from(new File(dataFolder, "config.yml"));
        this.messageProvider = MineConfiguration.from(new File(dataFolder, "messages.yml"));
        this.configProvider.initialize(PluginConfig.class);
        this.messageProvider.initialize(PluginMessages.class);
    }

    protected void firstInitialize(File dataFolder) {
        File configFile = new File(dataFolder, "config.yml");
        if (!configFile.exists()) {
            try {
                JarResourceUtils.copyFolderFromJar("i18n", dataFolder, JarResourceUtils.CopyOption.COPY_IF_NOT_EXIST);
                JarResourceUtils.copyFolderFromJar("prefixes", dataFolder, JarResourceUtils.CopyOption.COPY_IF_NOT_EXIST);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public ConfigurationProvider<?> getConfigProvider() {
        return configProvider;
    }

    public ConfigurationProvider<?> getMessageProvider() {
        return messageProvider;
    }

    public void reload() throws Exception {
        getConfigProvider().reload();
        getMessageProvider().reload();
    }

}
