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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PrefixSelectGUI extends AutoPagedGUI {

    public static HashSet<Player> openingUsers = new HashSet<>();

    Player player;

    public PrefixSelectGUI(Player player) {
        super(GUIType.SIX_BY_NINE, PluginConfig.GUI.TITLE.get(), 10, 43);
        this.player = player;

        setPreviousPageSlot(18);
        setNextPageSlot(26);

        loadBackButton();
        loadItems();
    }

    public Player getPlayer() {
        return player;
    }

    public void loadBackButton() {
        PluginConfig.GUI.ITEMS.BACK.getOptional().ifPresent(item -> item.setupItems(player, this));
    }

    public void loadItems() {
        List<PrefixConfig> prefixList = new ArrayList<>();
        prefixList.add(UserPrefixAPI.getPrefixManager().getDefaultPrefix());
        prefixList.addAll(UserPrefixAPI.getPrefixManager().getVisiblePrefix(player)); //只需要读取看得见的

        PrefixConfig usingPrefix = UserPrefixAPI.getUserManager().getPrefix(getPlayer());

        for (PrefixConfig prefix : prefixList) {
            if (prefix.getIdentifier().equals(usingPrefix.getIdentifier())) {
                addItem(new GUIItem(prefix.getItemWhenUsing(player) != null ? prefix.getItemWhenUsing(player) : prefix.getItemHasPermission(player)));
            } else if (prefix.checkPermission(player)) {
                addItem(new GUIItem(prefix.getItemHasPermission(player)) {

                    @Override
                    public void onClick(Player clicker, ClickType type) {
                        player.closeInventory();
                        //再次检查，防止打开GUI后、选择前的时间段内权限消失
                        if (prefix.checkPermission(player)) {

                            // 发送消息与提示
                            PluginConfig.SOUNDS.PREFIX_CHANGE.playTo(player);
                            PluginMessages.SELECTED.send(player, prefix.getName());

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
        player.closeInventory(); // 防止冲突
        PluginConfig.SOUNDS.GUI_OPEN.playTo(player);
        new PrefixSelectGUI(player).openGUI(player);
        openingUsers.add(player);
    }


}
