package cc.carm.plugin.userprefix.ui;

import cc.carm.lib.easyplugin.gui.GUIItem;
import cc.carm.lib.easyplugin.gui.GUIType;
import cc.carm.lib.easyplugin.gui.paged.AutoPagedGUI;
import cc.carm.plugin.userprefix.UserPrefixAPI;
import cc.carm.plugin.userprefix.conf.PluginConfig;
import cc.carm.plugin.userprefix.conf.PluginMessages;
import cc.carm.plugin.userprefix.conf.prefix.PrefixConfig;
import cc.carm.plugin.userprefix.event.UserPrefixChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class PrefixSelectGUI extends AutoPagedGUI {

    public static HashSet<Player> openingUsers = new HashSet<>();

    protected final Player player;
    protected final @Nullable String group;

    public PrefixSelectGUI(Player player) {
        this(player, null);
    }

    public PrefixSelectGUI(Player player, @Nullable String group) {
        super(GUIType.SIX_BY_NINE, PluginConfig.GUI.TITLE.get(), 10, 43);
        this.player = player;
        this.group = group;

        setPreviousPageSlot(18);
        setNextPageSlot(26);

        loadExtraIcons();
        loadItems();
    }

    public Player getPlayer() {
        return player;
    }

    @Nullable
    public String getGroup() {
        return group;
    }

    public void loadExtraIcons() {
        PluginConfig.GUI.ITEMS.getNotNull().getItems().values().forEach(v -> v.setupItems(player, this));
    }

    public void loadItems() {
        List<PrefixConfig> prefixList = new ArrayList<>();
        prefixList.add(UserPrefixAPI.getPrefixManager().getDefaultPrefix());
        prefixList.addAll(UserPrefixAPI.getPrefixManager().getVisiblePrefix(player, group));

        PrefixConfig usingPrefix = UserPrefixAPI.getUserManager().getPrefix(getPlayer());

        for (PrefixConfig prefix : prefixList) {
            if (prefix.getIdentifier().equals(usingPrefix.getIdentifier())) {
                addItem(new GUIItem(Optional
                        .ofNullable(prefix.getItemWhenUsing(player))
                        .orElse(prefix.getItemHasPermission(player))
                ));
            } else if (prefix.checkPermission(player)) {
                addItem(new GUIItem(prefix.getItemHasPermission(player)) {

                    @Override
                    public void onClick(Player clicker, ClickType type) {
                        player.closeInventory();
                        if (prefix.checkPermission(player)) {
                            PluginConfig.SOUNDS.PREFIX_CHANGE.playTo(player);
                            PluginMessages.SELECTED.sendTo(player, prefix.getName());

                            UserPrefixChangeEvent.call(player, usingPrefix, prefix, config -> {
                                if (config == null) return;
                                UserPrefixAPI.getUserManager().setPrefix(player, config, true);
                            });

                        } else {
                            PluginConfig.SOUNDS.GUI_CLICK.playTo(player);
                        }
                    }
                });
            } else {
                addItem(new GUIItem(prefix.getItemNoPermission(player)) {
                    @Override
                    public void onClick(Player clicker, ClickType type) {
                        PluginConfig.SOUNDS.GUI_CLICK.playTo(player);
                    }
                });
            }
        }
    }

    @Override
    public void onClose() {
        removeOpening(player);
    }

    public static void removeOpening(Player player) {
        openingUsers.remove(player);
    }

    public static void closeAll() {
        for (Player player : new HashSet<>(openingUsers)) {
            player.closeInventory();
        }
        openingUsers.clear();
    }

    public static void open(Player player) {
        open(player, null);
    }

    public static void open(Player player, @Nullable String group) {
        PluginConfig.SOUNDS.GUI_OPEN.playTo(player);
        new PrefixSelectGUI(player, group).openGUI(player);
        openingUsers.add(player);
    }
}
