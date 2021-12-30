package cc.carm.plugin.userprefix.manager;

import cc.carm.plugin.userprefix.Main;
import cc.carm.plugin.userprefix.configuration.PrefixConfig;
import cc.carm.plugin.userprefix.model.ConfiguredPrefix;
import cc.carm.plugin.userprefix.util.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PrefixManager {

	public static ConfiguredPrefix defaultPrefix;
	public static HashMap<String, ConfiguredPrefix> prefixes = new HashMap<>();

	private static final String FOLDER_NAME = "prefixes";

	public static void init() {
		loadPrefixes();
		Main.log("共加载了 " + prefixes.size() + " 个前缀。");
	}

	public static void loadPrefixes() {
		loadDefaultPrefix();
		loadConfiguredPrefixes();
	}

	public static void loadConfiguredPrefixes() {

		File prefixDataFolder = getStorageFolder();
		if (!prefixDataFolder.isDirectory() || !prefixDataFolder.exists()) {
			prefixDataFolder.mkdir();
		}

		String[] filesList = prefixDataFolder.list();
		if (filesList == null || filesList.length < 1) {
			Main.error("配置文件夹中暂无任何前缀配置问，请检查。");
			Main.error("There's no configured prefix.");
			Main.error("Path: " + prefixDataFolder.getAbsolutePath());
			return;
		}

		List<File> files = Arrays.stream(filesList)
				.map(s -> new File(prefixDataFolder, s))
				.filter(File::isFile)
				.collect(Collectors.toList());

		HashMap<String, ConfiguredPrefix> dataPrefixes = new HashMap<>();

		if (files.size() > 0) {
			for (File file : files) {
				try {
					ConfiguredPrefix prefix = new ConfiguredPrefix(file);
					Main.log("完成前缀加载 " + prefix.getIdentifier() + " : " + prefix.getName());
					Main.log("Successfully loaded " + prefix.getIdentifier() + " : " + prefix.getName());
					dataPrefixes.put(prefix.getIdentifier(), prefix);
				} catch (Exception ex) {
					Main.error("在加载前缀 " + file.getAbsolutePath() + " 时出错，请检查配置！");
					Main.error("Error occurred when loading prefix #" + file.getAbsolutePath() + " !");
					ex.printStackTrace();
				}
			}
		}

		PrefixManager.prefixes.clear();
		PrefixManager.prefixes = dataPrefixes;
	}

	public static void loadDefaultPrefix() {
		PrefixManager.defaultPrefix = null;
		ConfigurationSection defaultPrefixSection = ConfigManager.getPluginConfig()
				.getConfig().getConfigurationSection("defaultPrefix");
		if (defaultPrefixSection != null) {
			try {
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
				PrefixManager.defaultPrefix = new ConfiguredPrefix("default", name, content, 0, null, itemNotUsing, null, itemUsing);
			} catch (Exception ex) {
				Main.error("在加载默认前缀时出错，请检查配置！");
				Main.error("Error occurred when loading default prefix, please check the configuration.");
				ex.printStackTrace();
			}
		} else {
			PrefixManager.defaultPrefix = new ConfiguredPrefix("default", "默认前缀", "&r", 0, null,
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
		Main.log("Successfully loaded default prefix " + defaultPrefix.getName());
	}

	public static List<ConfiguredPrefix> getVisiblePrefix() {
		return PrefixManager.getPrefixes().values().stream()
				.filter(ConfiguredPrefix::isVisibleNoPermission)
				.sorted(Comparator.comparingInt(ConfiguredPrefix::getWeight))
				.collect(Collectors.toList());
	}

	@NotNull
	public static ConfiguredPrefix getDefaultPrefix() {
		return defaultPrefix;
	}

	@NotNull
	public static HashMap<String, ConfiguredPrefix> getPrefixes() {
		return prefixes;
	}

	@Nullable
	public static ConfiguredPrefix getPrefix(String identifier) {
		if (identifier == null) {
			return null;
		} else if (identifier.equalsIgnoreCase("default")) {
			return getDefaultPrefix();
		} else {
			return getPrefixes().get(identifier);
		}
	}

	private static File getStorageFolder() {
		if (PrefixConfig.CustomStorage.ENABLE.get()) {
			return new File(PrefixConfig.CustomStorage.PATH.get());
		} else {
			return new File(Main.getInstance().getDataFolder() + File.separator + FOLDER_NAME);
		}
	}


}
