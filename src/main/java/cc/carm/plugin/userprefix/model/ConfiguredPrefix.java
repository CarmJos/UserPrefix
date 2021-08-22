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

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return ColorParser.parseColor(content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public ItemStack getItemHasPermission() {
        return itemHasPermission;
    }

    public void setItemHasPermission(ItemStack itemHasPermission) {
        this.itemHasPermission = itemHasPermission;
    }

    public ItemStack getItemNoPermission() {
        return itemNoPermission;
    }

    public void setItemNoPermission(ItemStack itemNoPermission) {
        this.itemNoPermission = itemNoPermission;
    }

    public ItemStack getItemWhenUsing() {
        return itemWhenUsing;
    }

    public void setItemWhenUsing(ItemStack itemWhenUsing) {
        this.itemWhenUsing = itemWhenUsing;
    }

    public boolean isVisibleNoPermission() {
        return this.itemNoPermission != null;
    }


}
