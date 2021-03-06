package cc.carm.plugin.userprefix.configuration.prefix;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.lib.mineconfiguration.bukkit.data.ItemConfig;
import cc.carm.plugin.userprefix.manager.ServiceManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class PrefixConfig {


    private @Nullable File dataFile;
    private @Nullable FileConfiguration configuration;

    protected final @NotNull String identifier;

    protected final @NotNull String name;
    protected final @NotNull String content;

    protected final int weight;

    protected final @Nullable String permission;

    protected final @NotNull ItemConfig itemHasPermission;
    protected final @Nullable ItemConfig itemNoPermission;
    protected final @Nullable ItemConfig itemWhenUsing;

    public PrefixConfig(@NotNull String identifier, @NotNull String name,
                        @NotNull String content, int weight, @Nullable String permission,
                        @NotNull ItemConfig itemHasPermission,
                        @Nullable ItemConfig itemWhenUsing,
                        @Nullable ItemConfig itemNoPermission) {
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
    public ItemStack getItemHasPermission(@Nullable Player player) {
        return this.itemHasPermission.getItemStack(player);
    }

    @Nullable
    public ItemStack getItemNoPermission(@Nullable Player player) {
        if (this.itemNoPermission == null) return null;
        return this.itemNoPermission.getItemStack(player);
    }

    @Nullable
    public ItemStack getItemWhenUsing(@Nullable Player player) {
        if (this.itemWhenUsing == null) return getItemHasPermission(player);
        else return this.itemWhenUsing.getItemStack(player);
    }

    public boolean isPublic() {
        return getPermission() == null;
    }

    public boolean isVisible(Player player) {
        return this.itemWhenUsing != null || checkPermission(player);
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param player ??????
     * @return ????????????????????????????????????false????????????????????????????????????????????????????????????????????????????????????????????????true???
     */
    public boolean checkPermission(Player player) {
        return permission == null || ServiceManager.hasPermission(player, permission);
    }

}
