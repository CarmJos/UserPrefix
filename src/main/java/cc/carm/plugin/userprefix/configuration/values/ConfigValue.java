package cc.carm.plugin.userprefix.configuration.values;

import cc.carm.plugin.userprefix.configuration.file.FileConfig;
import cc.carm.plugin.userprefix.manager.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigValue<V> {

    FileConfig source;

    String configSection;
    Class<V> clazz;
    V defaultValue;

    public ConfigValue(String configSection, Class<V> clazz) {
        this(configSection, clazz, null);
    }

    public ConfigValue(String configSection, Class<V> clazz, V defaultValue) {
        this(ConfigManager.getPluginConfig(), configSection, clazz, defaultValue);
    }

    public ConfigValue(FileConfig source, String configSection, Class<V> clazz, V defaultValue) {
        this.source = source;
        this.configSection = configSection;
        this.clazz = clazz;
        this.defaultValue = defaultValue;
    }

    public FileConfiguration getConfiguration() {
        return this.source.getConfig();
    }

    public V get() {
        Object val = getConfiguration().get(this.configSection, this.defaultValue);
        return this.clazz.isInstance(val) ? this.clazz.cast(val) : this.defaultValue;
    }

    public void set(V value) {
        getConfiguration().set(this.configSection, value);
        this.save();
    }

    public void save() {
        this.source.save();
    }

}
