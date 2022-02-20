package cc.carm.plugin.userprefix.util;

import cc.carm.plugin.userprefix.wrapper.ItemStackWrapper;
import com.google.common.io.Files;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;

public class ConfigurationUtil {
    public static FileConfiguration bang(File file) {
        YamlConfiguration conf = new YamlConfiguration();
        StringJoiner builder = new StringJoiner("\n");
        try {
            //noinspection UnstableApiUsage
            Files.readLines(file, StandardCharsets.UTF_8).forEach(builder::add);
        } catch (IOException e) {
            e.printStackTrace();
            return conf;
        }
        String tmpConf = builder.toString().replace("==: "+ ItemStack.class.getName(), "==: "+ ItemStackWrapper.class.getName());
        try {
             conf.loadFromString(tmpConf);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return conf;
    }

    public static String dong(YamlConfiguration conf) {
        return conf.saveToString().replace("==: "+ ItemStackWrapper.class.getName(),"==: "+ ItemStack.class.getName());
    }
}