package cc.carm.plugin.userprefix.model;

import cc.carm.plugin.userprefix.util.ColorParser;
import cc.carm.plugin.userprefix.util.ItemStackFactory;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class ConfiguredPrefix {

    @Nullable
    private File dataFile;
    @Nullable
    private FileConfiguration configuration;

    String identifier;

    String name;
    String content;

    int weight;

    String permission;

    ItemStack itemHasPermission;
    ItemStack itemNoPermission;
    ItemStack itemWhenUsing;


    public ConfiguredPrefix(@NotNull File dataFile) {
        this.dataFile = dataFile;
        this.configuration = YamlConfiguration.loadConfiguration(dataFile);
        if (getConfiguration() != null) {
            this.identifier = getConfiguration().getString("identifier", "ERROR");
            this.name = getConfiguration().getString("name", "ERROR");
            this.content = getConfiguration().getString("content", "&r");
            this.permission = getConfiguration().getString("permission");
            this.weight = getConfiguration().getInt("weight", 1);

            this.itemHasPermission = (ItemStack) getConfiguration().get("itemHasPermission",
                    new ItemStackFactory(Material.STONE).setDisplayName(name).addLore(" ").addLore("§a➥ 点击切换到该前缀").toItemStack()
            );
            this.itemNoPermission = (ItemStack) getConfiguration().get("itemNoPermission", itemHasPermission);
            this.itemWhenUsing = (ItemStack) getConfiguration().get("itemUsing", itemHasPermission);
        }
    }

    public ConfiguredPrefix(@NotNull String identifier,
                            @NotNull String name,
                            @NotNull String content,
                            int weight, @Nullable String permission,
                            @NotNull ItemStack itemHasPermission,
                            @Nullable ItemStack itemNoPermission,
                            @Nullable ItemStack itemWhenUsing) {
        this.identifier = identifier;
        this.name = name;
        this.content = content;
        this.weight = weight;
        this.permission = permission;
        this.itemHasPermission = itemHasPermission;
        this.itemNoPermission = itemNoPermission;
        this.itemWhenUsing = itemWhenUsing;
    }

    @Nullable
    public FileConfiguration getConfiguration() {
        return configuration;
    }

    @NotNull
    public String getIdentifier() {
        return identifier;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getContent() {
        return ColorParser.parse(content);
    }

    public int getWeight() {
        return weight;
    }

    @Nullable
    public String getPermission() {
        return permission;
    }

    @NotNull
    public ItemStack getItemHasPermission() {
        return itemHasPermission;
    }

    @Nullable
    public ItemStack getItemNoPermission() {
        return itemNoPermission;
    }

    @Nullable
    public ItemStack getItemWhenUsing() {
        return itemWhenUsing;
    }

    public boolean isPublic() {
        return getPermission() == null;
    }

    public boolean isVisibleNoPermission() {
        return this.itemNoPermission != null;
    }


}
