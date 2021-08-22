package cc.carm.plugin.userprefix.configuration.values;

import cc.carm.plugin.userprefix.manager.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ConfigValueList<V> {
    FileConfiguration source;
    String configSection;
    Class<V> clazz;

    public ConfigValueList(String configSection, Class<V> clazz) {
        this.source = ConfigManager.getConfig();
        this.configSection = configSection;
        this.clazz = clazz;
    }

    public ArrayList<V> get() {
        List<?> list = this.source.getList(this.configSection);
        if (list == null) {
            return new ArrayList(0);
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
        this.source.set(this.configSection, value);
        this.save();
    }

    public void save() {
        ConfigManager.saveConfig();
    }
}
