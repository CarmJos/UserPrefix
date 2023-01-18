package cc.carm.plugin.userprefix.conf.prefix;

import cc.carm.lib.easyplugin.gui.configuration.GUIActionConfiguration;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.lib.mineconfiguration.bukkit.data.ItemConfig;
import cc.carm.plugin.userprefix.manager.ServiceManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PrefixConfig {

    protected final @NotNull String identifier;

    protected final @NotNull String name;
    protected final @NotNull String content;

    protected final int weight;

    protected final @Nullable String permission;

    protected final @NotNull List<GUIActionConfiguration> actions;

    protected final @NotNull ItemConfig itemHasPermission;
    protected final @Nullable ItemConfig itemNoPermission;
    protected final @Nullable ItemConfig itemWhenUsing;

    public PrefixConfig(@NotNull String identifier, @NotNull String name,
                        @NotNull String content, int weight, @Nullable String permission,
                        @NotNull List<GUIActionConfiguration> actions,
                        @NotNull ItemConfig itemHasPermission,
                        @Nullable ItemConfig itemWhenUsing,
                        @Nullable ItemConfig itemNoPermission) {
        this.identifier = identifier;
        this.name = name;
        this.content = content;
        this.weight = weight;
        this.permission = permission;
        this.actions = actions;
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
        if (this.itemWhenUsing == null) return null;
        else return this.itemWhenUsing.getItemStack(player);
    }

    public boolean isPublic() {
        return getPermission() == null;
    }

    public void executeActions(@NotNull Player player) {
        this.actions.forEach(action -> action.executeAction(player));
    }

    public boolean isVisible(Player player) {
        return this.itemNoPermission != null || checkPermission(player);
    }

    /**
     * 判断某玩家是否有权限使用该前缀
     *
     * @param player 玩家
     * @return 若前缀标识不存在，则返回false；若前缀为默认前缀，或该前缀无权限，或玩家有该前缀的权限，则返回true。
     */
    public boolean checkPermission(Player player) {
        return permission == null || ServiceManager.hasPermission(player, permission);
    }

}
