package cc.carm.plugin.userprefix.manager;

import cc.carm.lib.easyplugin.gui.configuration.GUIActionConfiguration;
import cc.carm.lib.easyplugin.utils.ItemStackFactory;
import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.conf.PluginConfig;
import cc.carm.plugin.userprefix.conf.prefix.PrefixConfig;
import com.cryptomorin.xseries.XItemStack;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
            prefixDataFolder.mkdirs();
        }

        List<File> files = new ArrayList<>();
        collectYamlFiles(prefixDataFolder, files);

        if (files.isEmpty()) {
            Main.severe("配置文件夹中暂无任何前缀配置问，请检查。");
            Main.severe("There's no configured prefix.");
            Main.severe("Path: " + prefixDataFolder.getAbsolutePath());
            return;
        }

        HashMap<String, PrefixConfig> loaded = new HashMap<>();

        for (File file : files) {
            try {
                PrefixConfig prefix = addPrefix(file);
                Main.debugging("完成前缀加载 " + prefix.getIdentifier() + " : " + prefix.getName());
                loaded.put(prefix.getIdentifier(), prefix);
            } catch (Exception ex) {
                Main.severe("在加载前缀 " + file.getAbsolutePath() + " 时出错，请检查配置！");
                Main.severe("Error occurred when loading prefix #" + file.getAbsolutePath() + " !");
                ex.printStackTrace();
            }
        }

        this.prefixes = loaded;
    }

    /**
     * 递归收集文件夹下的所有YAML文件
     *
     * @param folder 目标文件夹
     * @param files  文件列表
     */
    private void collectYamlFiles(@NotNull File folder, @NotNull List<File> files) {
        File[] children = folder.listFiles();
        if (children == null) return;
        for (File child : children) {
            if (child.isDirectory()) {
                collectYamlFiles(child, files);
            } else if (child.isFile() && isYamlFile(child)) {
                files.add(child);
            }
        }
    }

    /**
     * 判断文件是否为YAML文件
     *
     * @param file 文件
     * @return 如果是.yml或.yaml文件返回true
     */
    private boolean isYamlFile(@NotNull File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".yml") || name.endsWith(".yaml");
    }

    public void loadDefaultPrefix() {
        this.defaultPrefix = new PrefixConfig(
                "default",
                PluginConfig.DEFAULT_PREFIX.NAME.getNotNull(),
                PluginConfig.DEFAULT_PREFIX.DESCRIPTION.getNotNull(),
                PluginConfig.DEFAULT_PREFIX.CONTENT.resolve(),
                PluginConfig.DEFAULT_PREFIX.PERIOD.resolve(),
                PluginConfig.DEFAULT_PREFIX.WEIGHT.getNotNull(),
                null,
                readActions(PluginConfig.DEFAULT_PREFIX.ACTIONS.get()),
                PluginConfig.DEFAULT_PREFIX.ITEM.NOT_USING.getNotNull(),
                PluginConfig.DEFAULT_PREFIX.ITEM.USING.get(), null
        );
        Main.debugging("  完成默认前缀加载 " + defaultPrefix.getName());
    }

    public List<PrefixConfig> getVisiblePrefix(Player player) {
        return getPrefixes().values().stream()
                .filter(c -> c.isVisible(player))
                .sorted(Comparator.comparingInt(PrefixConfig::getWeight))
                .collect(Collectors.toList());
    }

    /**
     * 根据group获取可见的前缀列表
     *
     * @param player 玩家
     * @param group  分组名称，为null时返回所有前缀
     * @return 过滤后的前缀列表
     */
    public List<PrefixConfig> getVisiblePrefix(Player player, @Nullable String group) {
        if (group == null || group.isEmpty()) {
            return getVisiblePrefix(player);
        }
        return getPrefixes().values().stream()
                .filter(c -> c.isVisible(player))
                .filter(c -> group.equalsIgnoreCase(c.getGroup()))
                .sorted(Comparator.comparingInt(PrefixConfig::getWeight))
                .collect(Collectors.toList());
    }

    /**
     * 获取所有已配置的group列表
     *
     * @return group名称集合
     */
    public Set<String> getGroups() {
        return getPrefixes().values().stream()
                .map(PrefixConfig::getGroup)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
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
        FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
        String identifier = conf.getString("identifier");
        if (identifier == null)
            throw new Exception("配置文件 " + file.getAbsolutePath() + " 中没有配置前缀的唯一标识。");

        String name = conf.getString("name");
        if (name == null) throw new Exception("配置文件 " + file.getAbsolutePath() + " 中没有配置前缀的显示名称。");


        List<String> content = new ArrayList<>();
        if (conf.isList("content")) {
            content = conf.getStringList("content");
        } else {
            String single = conf.getString("content");
            if (single != null) content.add(single);
        }


        return new PrefixConfig(
                identifier, name,
                conf.getStringList("description"),
                content,
                conf.getLong("period", -1L),
                conf.getInt("weight", 1),
                conf.getString("permission"),
                conf.getString("group"),
                readActions(conf.getStringList("actions")),
                readItem(
                        conf.getConfigurationSection("item.has-perm"),
                        new ItemStackFactory(Material.STONE)
                                .setDisplayName(name)
                                .addLore("§a➥ 点击切换到该前缀")
                                .toItemStack()
                ),
                readItem(conf.getConfigurationSection("item.using"), null),
                readItem(conf.getConfigurationSection("item.no-perm"), null)
        );
    }


    @Contract("_,!null->!null")
    protected static ItemStack readItem(@Nullable ConfigurationSection section,
                                        @Nullable ItemStack defaultValue) throws Exception {
        if (section == null) return defaultValue;
        else return XItemStack.deserialize(section);
    }

    protected static List<GUIActionConfiguration> readActions(@NotNull List<String> strings) {
        return strings.stream().map(GUIActionConfiguration::deserialize).filter(Objects::nonNull).collect(Collectors.toList());
    }

}
