package cc.carm.plugin.userprefix.model;

import cc.carm.plugin.userprefix.util.ColorParser;
import org.bukkit.inventory.ItemStack;

public class ConfiguredPrefix {

    String identifier;

    String name;
    String content;

    int weight;

    String permission;

    ItemStack itemHasPermission;
    ItemStack itemNoPermission;
    ItemStack itemWhenUsing;

    public ConfiguredPrefix(String identifier, String name, String content, int weight, String permission, ItemStack itemHasPermission, ItemStack itemNoPermission, ItemStack itemWhenUsing) {
        this.identifier = identifier;
        this.name = name;
        this.content = content;
        this.weight = weight;
        this.permission = permission;
        this.itemHasPermission = itemHasPermission;
        this.itemNoPermission = itemNoPermission;
        this.itemWhenUsing = itemWhenUsing;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return ColorParser.parseColor(content);
    }

    public int getWeight() {
        return weight;
    }

    public String getPermission() {
        return permission;
    }

    public ItemStack getItemHasPermission() {
        return itemHasPermission;
    }

    public ItemStack getItemNoPermission() {
        return itemNoPermission;
    }

    public ItemStack getItemWhenUsing() {
        return itemWhenUsing;
    }

    public boolean isVisibleNoPermission() {
        return this.itemNoPermission != null;
    }


}
