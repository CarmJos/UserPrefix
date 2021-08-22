package cc.carm.plugin.userprefix.util;


import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public ItemStackFactory setDisplayName(String name) {
        ItemMeta im = this.item.getItemMeta();
        im.setDisplayName(name.replace("&", "§").replace("§§", "&&"));
        this.item.setItemMeta(im);
        return this;
    }

    public ItemStackFactory setLore(List<String> lores) {
        ItemMeta im = this.item.getItemMeta();
        List<String> lores_ = new ArrayList();
        Iterator var4 = lores.iterator();

        while (var4.hasNext()) {
            String lore = (String) var4.next();
            lores_.add(lore.replace("&", "§").replace("§§", "&&"));
        }

        im.setLore(lores_);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemStackFactory addLore(String name) {
        ItemMeta im = this.item.getItemMeta();
        Object lores;
        if (im.hasLore()) {
            lores = im.getLore();
        } else {
            lores = new ArrayList();
        }

        ((List) lores).add(name.replace("&", "§").replace("§§", "&&"));
        im.setLore((List) lores);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemStackFactory addEnchant(Enchantment ench, int level, boolean ignoreLevelRestriction) {
        ItemMeta im = this.item.getItemMeta();
        im.addEnchant(ench, level, ignoreLevelRestriction);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemStackFactory removeEnchant(Enchantment ench) {
        ItemMeta im = this.item.getItemMeta();
        im.removeEnchant(ench);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemStackFactory addFlag(ItemFlag flag) {
        ItemMeta im = this.item.getItemMeta();
        im.addItemFlags(new ItemFlag[]{flag});
        this.item.setItemMeta(im);
        return this;
    }

    public ItemStackFactory removeFlag(ItemFlag flag) {
        ItemMeta im = this.item.getItemMeta();
        im.removeItemFlags(new ItemFlag[]{flag});
        this.item.setItemMeta(im);
        return this;
    }

    public ItemStackFactory setUnbreakable(boolean unbreakable) {
        ItemMeta im = this.item.getItemMeta();
        im.setUnbreakable(unbreakable);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemStackFactory setSkullOwner(String name) {
        if (this.item.getType() == Material.PLAYER_HEAD || this.item.getType() == Material.PLAYER_WALL_HEAD) {
            SkullMeta im = (SkullMeta) this.item.getItemMeta();
            im.setOwner(name);
            this.item.setItemMeta(im);
        }

        return this;
    }
}
