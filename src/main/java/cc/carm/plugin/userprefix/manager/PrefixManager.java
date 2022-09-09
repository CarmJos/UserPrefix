package cc.carm.plugin.userprefix.manager;

import cc.carm.lib.easyplugin.gui.configuration.GUIActionConfiguration;
import cc.carm.lib.mineconfiguration.bukkit.data.ItemConfig;
import cc.carm.lib.mineconfiguration.bukkit.source.CraftSectionWrapper;
import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.conf.PluginConfig;
import cc.carm.plugin.userprefix.conf.prefix.PrefixConfig;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class PrefixManager {

    protected static final String FOLDER_NAME = "prefixes";

    protected @NotNull Map<String, PrefixConfig> prefixes = new HashMap<>();
    protected PrefixConfig defaultPrefix;

    public int loadPrefixes() {
        loadDefaultPrefix();
        loadConfiguredPrefixes();
        return prefixes.size();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void loadConfiguredPrefixes() {

        File prefixDataFolder = getStorageFolder();
        if (!prefixDataFolder.isDirectory() || !prefixDataFolder.exists()) {
            prefixDataFolder.mkdir();
        }

        String[] filesList = prefixDataFolder.list();
        if (filesList == null || filesList.length < 1) {
            Main.serve("配置文件夹中暂无任何前缀配置问，请检查。");
            Main.serve("There's no configured prefix.");
            Main.serve("Path: " + prefixDataFolder.getAbsolutePath());
            return;
        }

        List<File> files = Arrays.stream(filesList)
                .map(s -> new File(prefixDataFolder, s))
                .filter(File::isFile)
                .collect(Collectors.toList());

        HashMap<String, PrefixConfig> loaded = new HashMap<>();

        if (files.size() > 0) {
            for (File file : files) {
                try {
                    PrefixConfig prefix = addPrefix(file);
                    Main.debugging("完成前缀加载 " + prefix.getIdentifier() + " : " + prefix.getName());
                    loaded.put(prefix.getIdentifier(), prefix);
                } catch (Exception ex) {
                    Main.serve("在加载前缀 " + file.getAbsolutePath() + " 时出错，请检查配置！");
                    Main.serve("Error occurred when loading prefix #" + file.getAbsolutePath() + " !");
                    ex.printStackTrace();
                }
            }
        }

        this.prefixes = loaded;
    }

    public void loadDefaultPrefix() {
        this.defaultPrefix = new PrefixConfig(
                "default",
                PluginConfig.DEFAULT_PREFIX.NAME.getNotNull(),
                PluginConfig.DEFAULT_PREFIX.CONTENT.getNotNull(),
                PluginConfig.DEFAULT_PREFIX.WEIGHT.getNotNull(),
                null,
                readActions(PluginConfig.DEFAULT_PREFIX.ACTIONS.get()),
                PluginConfig.DEFAULT_PREFIX.ITEM.NOT_USING.getNotNull(),
                PluginConfig.DEFAULT_PREFIX.ITEM.USING.get(),
                null
        );
        Main.debugging("  完成默认前缀加载 " + defaultPrefix.getName());
    }

    public List<PrefixConfig> getVisiblePrefix(Player player) {
        return getPrefixes().values().stream()
                .filter(c -> c.isVisible(player))
                .sorted(Comparator.comparingInt(PrefixConfig::getWeight))
                .collect(Collectors.toList());
    }

    @NotNull
    public PrefixConfig getDefaultPrefix() {
        return defaultPrefix;
    }

    @NotNull
    public Map<String, PrefixConfig> getPrefixes() {
        return prefixes;
    }

    @Nullable
    public PrefixConfig getPrefix(String identifier) {
        if (identifier == null) {
            return null;
        } else if (identifier.equalsIgnoreCase("default")) {
            return getDefaultPrefix();
        } else {
            return getPrefixes().get(identifier);
        }
    }


    protected File getStorageFolder() {
        if (PluginConfig.CUSTOM_STORAGE.ENABLE.getNotNull()) {
            return new File(PluginConfig.CUSTOM_STORAGE.PATH.getNotNull());
        } else {
            return new File(Main.getInstance().getDataFolder() + File.separator + FOLDER_NAME);
        }
    }

    public static @NotNull PrefixConfig addPrefix(@NotNull File file) throws Exception {
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        String identifier = configuration.getString("identifier");
        if (identifier == null)
            throw new Exception("配置文件 " + file.getAbsolutePath() + " 中没有配置前缀的唯一标识。");

        String name = configuration.getString("name");
        if (name == null) throw new Exception("配置文件 " + file.getAbsolutePath() + " 中没有配置前缀的显示名称。");

        return new PrefixConfig(
                identifier, name,
                configuration.getString("content", "&r"),
                configuration.getInt("weight", 1),
                configuration.getString("permission"),
                readActions(configuration.getStringList("actions")),
                readItem(
                        configuration.getConfigurationSection("item.has-perm"),
                        new ItemConfig(Material.STONE, name, Arrays.asList(" ", "§a➥ 点击切换到该前缀"))
                ),
                readItem(configuration.getConfigurationSection("item.using"), null),
                readItem(configuration.getConfigurationSection("item.no-perm"), null)
        );
    }


    @Contract("_,!null->!null")
    protected static ItemConfig readItem(@Nullable ConfigurationSection section, @Nullable ItemConfig defaultValue) throws Exception {
        if (section == null) return defaultValue;
        else return ItemConfig.deserialize(CraftSectionWrapper.of(section));
    }

    protected static List<GUIActionConfiguration> readActions(@NotNull List<String> strings) {
        return strings.stream().map(GUIActionConfiguration::deserialize).filter(Objects::nonNull).collect(Collectors.toList());
    }


}
