package cc.carm.plugin.userprefix.manager;

import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.model.ConfiguredPrefix;
import cc.carm.plugin.userprefix.util.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PrefixManager {

    public static ConfiguredPrefix defaultPrefix;
    public static HashMap<String, ConfiguredPrefix> prefixes = new HashMap<>();


    public static void init() {
        loadConfiguredPrefixes();
        Main.log("共加载了 " + prefixes.size() + " 个前缀。");
    }

    public static void loadConfiguredPrefixes() {
        loadDefaultPrefix();

        ConfigurationSection prefixesSection = ConfigManager.getConfig().getConfigurationSection("prefixes");
        if (prefixesSection == null || prefixesSection.getKeys(false).isEmpty()) {
            Main.log("配置文件中暂无任何前缀配置，请检查。");
            return;
        }
        HashMap<String, ConfiguredPrefix> dataPrefixes = new HashMap<>();
        for (String prefixIdentifier : prefixesSection.getKeys(false)) {
            ConfigurationSection configuredPrefixSection = prefixesSection.getConfigurationSection(prefixIdentifier);
            if (configuredPrefixSection == null) continue;

            String name = configuredPrefixSection.getString("name", "前缀名配置错误");
            String content = configuredPrefixSection.getString("content", "&r");
            String permission = configuredPrefixSection.getString("permission");
            int weight = configuredPrefixSection.getInt("weight", 1);

            ItemStack itemHasPermission = configuredPrefixSection.getItemStack("itemHasPermission",
                    new ItemStackFactory(Material.STONE).setDisplayName(name).addLore(" ").addLore("§a➥ 点击切换到该前缀").toItemStack()
            );
            ItemStack itemNoPermission = configuredPrefixSection.getItemStack("itemNoPermission", itemHasPermission);
            ItemStack itemUsing = configuredPrefixSection.getItemStack("itemUsing", itemHasPermission);


            Main.log("完成前缀加载 " + prefixIdentifier + " : " + name);

            dataPrefixes.put(prefixIdentifier, new ConfiguredPrefix(prefixIdentifier, name, content, weight, permission, itemHasPermission, itemNoPermission, itemUsing));
        }

        prefixes = dataPrefixes;
    }

    public static void loadDefaultPrefix() {
        ConfigurationSection defaultPrefixSection = ConfigManager.getConfig().getConfigurationSection("defaultPrefix");
        if (defaultPrefixSection != null) {
            String name = defaultPrefixSection.getString("name", "默认前缀");
            String content = defaultPrefixSection.getString("content", "&r");
            ItemStack itemNotUsing = defaultPrefixSection.getItemStack(
                    "itemNotUsing",
                    new ItemStackFactory(Material.NAME_TAG)
                            .setDisplayName("&f默认前缀")
                            .addLore(" ")
                            .addLore("§a➥ 点击切换到该前缀")
                            .toItemStack()
            );
            ItemStack itemUsing = defaultPrefixSection.getItemStack("itemUsing",
                    new ItemStackFactory(Material.NAME_TAG)
                            .setDisplayName("&f默认前缀")
                            .addLore(" ")
                            .addLore("§a✔ 您正在使用该前缀")
                            .addEnchant(Enchantment.DURABILITY, 1, false)
                            .addFlag(ItemFlag.HIDE_ENCHANTS)
                            .toItemStack()
            );
            defaultPrefix = new ConfiguredPrefix("default", name, content, 0, null, itemNotUsing, null, itemUsing);
        } else {
            defaultPrefix = new ConfiguredPrefix("default", "默认前缀", "&r", 0, null,
                    new ItemStackFactory(Material.NAME_TAG)
                            .setDisplayName("&f默认前缀")
                            .addLore(" ")
                            .addLore("§a➥ 点击切换到该前缀")
                            .toItemStack(),
                    null,
                    new ItemStackFactory(Material.NAME_TAG)
                            .setDisplayName("&f默认前缀")
                            .addLore(" ")
                            .addLore("§a✔ 您正在使用该前缀")
                            .addEnchant(Enchantment.DURABILITY, 1, false)
                            .addFlag(ItemFlag.HIDE_ENCHANTS)
                            .toItemStack()
            );
        }

        Main.log("完成默认前缀加载 " + defaultPrefix.getName());
    }

    public static List<ConfiguredPrefix> getVisiblePrefix() {
        return PrefixManager.getPrefixes().values().stream()
                .filter(ConfiguredPrefix::isVisibleNoPermission)
                .sorted(Comparator.comparingInt(ConfiguredPrefix::getWeight))
                .collect(Collectors.toList());
    }

    public static ConfiguredPrefix getDefaultPrefix() {
        return defaultPrefix;
    }

    public static HashMap<String, ConfiguredPrefix> getPrefixes() {
        return prefixes;
    }

    public static ConfiguredPrefix getPrefix(String identifier) {
        if (identifier == null || identifier.equalsIgnoreCase("default")) {
            return getDefaultPrefix();
        } else {
            return getPrefixes().get(identifier);
        }
    }


}
