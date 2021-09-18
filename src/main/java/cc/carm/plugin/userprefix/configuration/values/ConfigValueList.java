package cc.carm.plugin.userprefix.configuration.values;

import cc.carm.plugin.userprefix.configuration.file.FileConfig;
import cc.carm.plugin.userprefix.manager.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigValueList<V> {
    FileConfig source;
    String configSection;
    Class<V> clazz;

    V[] defaultValue;

    public ConfigValueList(String configSection, Class<V> clazz) {
        this(ConfigManager.getPluginConfig(), configSection, clazz);
    }

    public ConfigValueList(String configSection, Class<V> clazz, V[] defaultValue) {
        this(ConfigManager.getPluginConfig(), configSection, clazz, defaultValue);
    }

    public ConfigValueList(FileConfig configuration, String configSection, Class<V> clazz) {
        this(configuration, configSection, clazz, null);
    }

    public ConfigValueList(FileConfig configuration, String configSection, Class<V> clazz, V[] defaultValue) {
        this.source = configuration;
        this.configSection = configSection;
        this.clazz = clazz;
        this.defaultValue = defaultValue;
    }

    public FileConfiguration getConfiguration() {
        return this.source.getConfig();
    }


    public ArrayList<V> get() {
        List<?> list = getConfiguration().getList(this.configSection);
        if (list == null) {
            if (defaultValue != null) {
                return new ArrayList<>(Arrays.asList(defaultValue));
            } else {
                return new ArrayList(0);
            }
        } else {
            ArrayList<V> result = new ArrayList();

            for (Object object : list) {
                if (this.clazz.isInstance(object)) {
                    result.add(this.clazz.cast(object));
                }
            }
            return result;
        }
    }

    public void set(ArrayList<V> value) {
        getConfiguration().set(this.configSection, value);
        this.save();
    }

    public void save() {
        this.source.save();
    }

}
