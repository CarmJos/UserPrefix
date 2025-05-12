package cc.carm.plugin.userprefix.manager;

import cc.carm.lib.configuration.source.ConfigurationHolder;
import cc.carm.lib.easyplugin.utils.JarResourceUtils;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import cc.carm.plugin.userprefix.conf.PluginConfig;
import cc.carm.plugin.userprefix.conf.PluginMessages;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private final ConfigurationHolder<?> configProvider;
    private final ConfigurationHolder<?> messageProvider;

    public ConfigManager(File dataFolder) {
        firstInitialize(dataFolder);
        this.configProvider = MineConfiguration.from(new File(dataFolder, "config.yml"), null);
        this.messageProvider = MineConfiguration.from(new File(dataFolder, "messages.yml"), null);
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

    public ConfigurationHolder<?> getConfigProvider() {
        return configProvider;
    }

    public ConfigurationHolder<?> getMessageProvider() {
        return messageProvider;
    }

    public void reload() throws Exception {
        getConfigProvider().reload();
        getMessageProvider().reload();
    }

}
