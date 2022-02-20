package cc.carm.plugin.userprefix.configuration.file;


import cc.carm.plugin.userprefix.util.ConfigurationUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class FileConfig {

    private final JavaPlugin plugin;

    private final String fileName;

    private File file;
    private FileConfiguration config;

    public FileConfig(final JavaPlugin plugin) {
        this(plugin, "config.yml");
    }

    public FileConfig(final JavaPlugin plugin, final String name) {
        this.plugin = plugin;
        this.fileName = name;
        initFile();
    }

    private void initFile() {
        this.file = new File(plugin.getDataFolder(), fileName);
        if (!this.file.exists()) {
            if (!this.file.getParentFile().exists()) {
                this.file.getParentFile().mkdirs();
            }
            plugin.saveResource(fileName, true);
        }
        this.config = ConfigurationUtil.bang(this.file);
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            getConfig().save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        if (getFile().exists()) {
            this.config =  ConfigurationUtil.bang(getFile());
        } else {
            initFile();
        }
    }
}
