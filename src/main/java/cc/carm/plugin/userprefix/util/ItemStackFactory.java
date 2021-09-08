package cc.carm.plugin.userprefix.util;


import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemStackFactory {
    ItemStack item;

    private ItemStackFactory() {
    }

    public ItemStackFactory(ItemStack is) {
        this.item = is.clone();
    }

    public ItemStackFactory(Material type) {
        this(type, 1);
    }

    public ItemStackFactory(Material type, int amount) {
        this(type, amount, (short) 0);
    }

    public ItemStackFactory(Material type, int amount, short data) {
        this.item = new ItemStack(type, amount, data);
    }

    public ItemStackFactory(Material type, int amount, int data) {
        this(type, amount, (short) data);
    }

    public ItemStack toItemStack() {
        return this.item;
    }

    public ItemStackFactory setType(Material type) {
        this.item.setType(type);
        return this;
    }

    public ItemStackFactory setDurability(int i) {
        this.item.setDurability((short) i);
        return this;
    }

    public ItemStackFactory setAmount(int a) {
        this.item.setAmount(a);
        return this;
    }

    public ItemStackFactory setDisplayName(@NotNull String name) {
        ItemMeta im = this.item.getItemMeta();
        if (im != null) {
            im.setDisplayName(ColorParser.parseColor(name));
            this.item.setItemMeta(im);
        }
        return this;
    }

    public ItemStackFactory setLore(@NotNull List<String> loreList) {
        ItemMeta im = this.item.getItemMeta();
        if (im != null) {
            im.setLore(
                    loreList.stream()
                            .map(ColorParser::parseColor)
                            .collect(Collectors.toList())
            );
            this.item.setItemMeta(im);
        }
        return this;
    }

    public ItemStackFactory addLore(@NotNull String s) {
        ItemMeta im = this.item.getItemMeta();
        if (im != null) {
            List<String> lore = im.getLore() != null ? im.getLore() : new ArrayList<>();
            lore.add(ColorParser.parseColor(s));
            im.setLore(lore);
            this.item.setItemMeta(im);
        }
        return this;
    }

    public ItemStackFactory addEnchant(@NotNull Enchantment enchant, int level, boolean ignoreLevelRestriction) {
        ItemMeta im = this.item.getItemMeta();
        if (im != null) {
            im.addEnchant(enchant, level, ignoreLevelRestriction);
            this.item.setItemMeta(im);
        }

        return this;
    }

    public ItemStackFactory removeEnchant(@NotNull Enchantment enchant) {
        ItemMeta im = this.item.getItemMeta();
        if (im != null) {
            im.removeEnchant(enchant);
            this.item.setItemMeta(im);
        }
        return this;
    }

    public ItemStackFactory addFlag(@NotNull ItemFlag flag) {
        ItemMeta im = this.item.getItemMeta();
        if (im != null) {
            im.addItemFlags(flag);
            this.item.setItemMeta(im);
        }

        return this;
    }

    public ItemStackFactory removeFlag(@NotNull ItemFlag flag) {
        ItemMeta im = this.item.getItemMeta();
        if (im != null) {
            im.removeItemFlags(flag);
            this.item.setItemMeta(im);
        }
        return this;
    }

    public ItemStackFactory setUnbreakable(boolean unbreakable) {
        ItemMeta im = this.item.getItemMeta();
        if (im != null) {
            im.setUnbreakable(unbreakable);
            this.item.setItemMeta(im);
        }
        return this;
    }
}
