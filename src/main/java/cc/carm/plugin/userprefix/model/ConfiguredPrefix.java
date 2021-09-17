package cc.carm.plugin.userprefix.model;

import cc.carm.plugin.userprefix.util.ColorParser;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfiguredPrefix {

    String identifier;

    String name;
    String content;

    int weight;

    String permission;

    ItemStack itemHasPermission;
    ItemStack itemNoPermission;
    ItemStack itemWhenUsing;

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
