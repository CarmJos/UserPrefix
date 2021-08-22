package cc.carm.plugin.userprefix.configuration.values;

import cc.carm.plugin.userprefix.manager.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigValue<V> {
    FileConfiguration source;
    String configSection;
    Class<V> clazz;
    V defaultValue;

    public ConfigValue(String configSection, Class<V> clazz) {
        this(configSection, clazz, null);
    }

    public ConfigValue(String configSection, Class<V> clazz, V defaultValue) {
        this.source = ConfigManager.getConfig();
        this.configSection = configSection;
        this.clazz = clazz;
        this.defaultValue = defaultValue;
    }

    public V get() {
        Object val = this.source.get(this.configSection, this.defaultValue);
        return this.clazz.isInstance(val) ? this.clazz.cast(val) : this.defaultValue;
    }

    public void set(V value) {
        this.source.set(this.configSection, value);
        this.save();
    }

    public void save() {
        ConfigManager.saveConfig();
    }

}
